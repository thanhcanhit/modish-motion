package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.services.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public static final double SHIPPING_FEE = 20000;
    public static final double FREE_SHIPPING_THRESHOLD = 500000;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String getCart(Model model) {
        var cart = cartService.getCart();
        
        // Tính tổng giá trị đơn hàng
        double totalAmount = cart.getItems().stream()
                .mapToDouble(item -> item.getVariant().getPrice() * item.getQuantity())
                .sum();

        // Tính phí vận chuyển
        double shippingFee = totalAmount >= FREE_SHIPPING_THRESHOLD ?
                0 : SHIPPING_FEE;
        
        // Cập nhật thông tin vào model
        model.addAttribute("cart", cart);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("freeShippingThreshold", FREE_SHIPPING_THRESHOLD);
        model.addAttribute("totalAmount", totalAmount);
        
        return "cart";
    }
}
