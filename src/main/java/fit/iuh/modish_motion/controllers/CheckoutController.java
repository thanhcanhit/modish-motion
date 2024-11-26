package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.*;
import fit.iuh.modish_motion.services.AccountService;
import fit.iuh.modish_motion.services.EmailService;
import fit.iuh.modish_motion.services.OrderService;
import fit.iuh.modish_motion.services.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private final AccountService accountService;
    private final OrderService orderService;
    private final EmailService emailService;
    private final VariantService variantService;

    @Autowired
    public CheckoutController(AccountService accountService,
                              OrderService orderService,
                              EmailService emailService,
                              VariantService variantService) {
        this.accountService = accountService;
        this.orderService = orderService;
        this.emailService = emailService;
        this.variantService = variantService;
    }

    @GetMapping
    public String getCheckout(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        AccountDTO currentUser = accountService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("auth", currentUser);
        return "checkout";
    }

    @PostMapping("/api/orders")
    @ResponseBody
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            AccountDTO currentUser = accountService.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Create new order
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setCustomer(currentUser.getUser());
            orderDTO.setOrderAt(new Date());
            orderDTO.setStatus(1); // Pending status
            orderDTO.setPaymentConfirm(false);
            orderDTO.setTotalDue(calculateTotal(orderRequest));

            // Create order details
            List<OrderDetailDTO> orderDetails = new ArrayList<>();
            for (OrderRequest.OrderItemRequest item : orderRequest.getItems()) {
                // Get fresh variant data from database
                VariantDTO variant = variantService.findById(String.valueOf(item.getVariant().getId()))
                        .orElseThrow(() -> new RuntimeException("Variant not found"));

                OrderDetailDTO detail = new OrderDetailDTO();
                detail.setVariant(variant);
                detail.setQuantity(item.getQuantity());
                orderDetails.add(detail);
            }

            // Save order with details
            OrderDTO savedOrder = orderService.createOrder(orderDTO, orderDetails);

            // Send confirmation email
            try {
                Context context = new Context();
                // Order information
                context.setVariable("orderId", savedOrder.getId());
                context.setVariable("orderDate", savedOrder.getOrderAt());
                context.setVariable("totalAmount", savedOrder.getTotalDue());
                context.setVariable("shippingFee", orderRequest.getShippingFee());
                context.setVariable("paymentMethod", orderRequest.getPaymentMethod());

                // Customer information
                context.setVariable("customerName", currentUser.getUser().getName());
                context.setVariable("phoneNumber", currentUser.getUser().getPhoneNumber());
                context.setVariable("shippingAddress", currentUser.getUser().getAddress());

                // Product information
                List<Map<String, Object>> orderItems = orderDetails.stream()
                        .map(detail -> {
                            Map<String, Object> item = new HashMap<>();
                            item.put("productName", detail.getVariant().getName());
                            item.put("quantity", detail.getQuantity());
                            item.put("price", detail.getVariant().getPrice() * detail.getQuantity());
                            item.put("color", detail.getVariant().getColor().getColor());
                            item.put("size", detail.getVariant().getSize().getSize());
                            item.put("imageUrl", detail.getVariant().getImageUrls().get(0));
                            return item;
                        })
                        .collect(Collectors.toList());
                context.setVariable("orderItems", orderItems);

                emailService.sendHtmlEmail(
                        currentUser.getUser().getEmail(),
                        "Đơn hàng #" + savedOrder.getId() + " đã được xác nhận",
                        "email/order-success",
                        context
                );
                System.out.println("Confirmation email sent successfully");
            } catch (Exception e) {
                // Log email error but don't fail the order
                System.err.println("Failed to send confirmation email: " + e.getMessage());
                e.printStackTrace();
            }

            return ResponseEntity.ok().body(Map.of(
                    "orderId", savedOrder.getId(),
                    "message", "Order created successfully"
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private double calculateTotal(OrderRequest orderRequest) {
        double itemsTotal = orderRequest.getItems().stream()
                .mapToDouble(item -> item.getVariant().getPrice() * item.getQuantity())
                .sum();
        return itemsTotal + orderRequest.getShippingFee();
    }
} 