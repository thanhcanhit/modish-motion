package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.dto.OrderRequest;
import fit.iuh.modish_motion.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private final AccountService accountService;

    @Autowired
    CheckoutController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public String getCheckout(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        // Lấy thông tin người dùng từ SecurityContext
        AccountDTO currentUser = accountService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("currentUser = " + currentUser);

        // Thêm thông tin người dùng vào model với key là "auth"
        model.addAttribute("auth", currentUser);
        return "checkout";
    }

    @PostMapping("/api/orders")
    @ResponseBody
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            return ResponseEntity.ok().body(Map.of(
                    "orderId", "123",
                    "message", "Order created successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
} 