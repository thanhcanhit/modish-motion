package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.OrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);

    @GetMapping
    public String getCheckout(Model model) {
        logger.info("Accessing checkout page");
        return "checkout";
    }

    @PostMapping("/api/orders")
    @ResponseBody
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            logger.info("Creating order: {}", orderRequest);
            return ResponseEntity.ok().body(Map.of(
                "orderId", "123",
                "message", "Order created successfully"
            ));
        } catch (Exception e) {
            logger.error("Error creating order", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
} 