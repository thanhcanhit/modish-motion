package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.services.AccountService;
import fit.iuh.modish_motion.services.UserService;
import fit.iuh.modish_motion.servicesImpl.AccountServiceImpl;
import fit.iuh.modish_motion.servicesImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final AccountService accountService;

    public LoginController() {
        this.userService = new UserServiceImpl();
        this.accountService = new AccountServiceImpl();
    }


    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password, Model model) {

        // Xác thực người dùng
        boolean isAuthenticated = accountService.authenticate(username, password);

        if (isAuthenticated) {
            // Nếu đăng nhập thành công, chuyển đến trang user profile
            return "redirect:/user";
        } else {
            // Nếu thất bại, hiển thị thông báo lỗi trên trang đăng nhập
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        return "admin";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        return "login";
    }

    @GetMapping("/user")
    public String userPage(Model model) {
        return "profile";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        return "login";  // trả về trang login.html, trong đó đã gọi showSignUp()
    }

}
