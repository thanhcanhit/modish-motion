package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.services.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.security.core.Authentication;

import java.util.Optional;

@Controller
public class ProfileController {
    private final AccountService accountService;

    public ProfileController(AccountService accountService) {
        this.accountService = accountService;
    }
    @GetMapping("/profile")
    public String userProfile(@RequestParam(value = "tab", defaultValue = "profile") String tab, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            System.out.println("User not authenticated: " + auth);
            return "redirect:/login"; // Chuyển hướng về trang login
        }

        // Lấy thông tin người dùng từ SecurityContext
        AccountDTO currentUser = accountService.findByUsername(auth.getName())
                .orElse(null);
        // Thêm thông tin người dùng vào model
        model.addAttribute("user", currentUser);
        model.addAttribute("selectedTab", tab);
        return "profile";
    }
}