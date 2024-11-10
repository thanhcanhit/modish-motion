package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.services.AccountService;
import fit.iuh.modish_motion.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    private final UserService userService;
    private final AccountService accountService;

    public LoginController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }


}
