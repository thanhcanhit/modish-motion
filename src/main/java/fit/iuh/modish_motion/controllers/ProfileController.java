package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.services.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Optional;

@Controller
public class ProfileController {
    private final AccountService accountService;

    public ProfileController(AccountService accountService) {
        this.accountService = accountService;
    }
    @GetMapping("/profile")
    public String userProfile(Model model, HttpSession session) {
        AccountDTO currentUser = (AccountDTO) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "login";
        }
        model.addAttribute("user", currentUser);
        return "profile";
    }
}
