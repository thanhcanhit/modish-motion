package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.configs.PasswordEncoder;
import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.services.AccountService;
import fit.iuh.modish_motion.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.security.core.Authentication;

import java.util.Optional;

@Controller
public class ProfileController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
    @PostMapping("/profile/change-password")
    public String changePassword(String oldPassword,
                                 String newPassword,
                                 String confirmPassword) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            System.out.println("User not authenticated: " + auth);
            return "redirect:/login"; // Chuyển hướng về trang login
        }

        // Lấy thông tin người dùng từ SecurityContext
        AccountDTO currentUser = accountService.findByUsername(auth.getName())
                .orElse(null);
        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            return "redirect:/profile?tab=password&error=oldPassword";
        }

        // Kiểm tra độ mạnh của mật khẩu (regex)
        String passwordRegex = "^[A-Z][A-Za-z\\d.,?#@]{7,}$";
        if (!newPassword.matches(passwordRegex)) {
            return "redirect:/profile?tab=password&error=invalidNewPassword";
        }

        // Mã hóa mật khẩu mới và lưu
        accountService.changePassword(currentUser, oldPassword, newPassword);

        return "redirect:/profile?tab=password&success";
    }
    @PostMapping("/profile/change-address")
    public String changeAddress(@RequestParam String newAddress,
                                @RequestParam String newPhoneNumber) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }

        // Lấy thông tin tài khoản hiện tại
        AccountDTO currentUser = accountService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        userService.updateAddress(currentUser.getUser().getId(), newAddress, newPhoneNumber);

        return "redirect:/profile?tab=address";
    }
}