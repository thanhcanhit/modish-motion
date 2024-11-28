package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.dto.UserAccountDTO;
import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.services.AccountService;
import fit.iuh.modish_motion.services.EmailService;
import fit.iuh.modish_motion.services.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class SignupController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private LoginController loginController;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("userAccount", new UserAccountDTO()); // Truyền DTO mới
        return "login"; // Tên template Thymeleaf
    }

    @PostMapping("/signuprequest")
    public String signup(@ModelAttribute("userAccount") UserAccountDTO userAccountDTO, Model model, RedirectAttributes redirectAttributes) {
        String rawPassword = userAccountDTO.getPassword();
        String result = userService.registerUser(userAccountDTO);

        if (result.equals("Success")) {
            try {
                Context context = new Context();
                context.setVariable("userName", userAccountDTO.getName());
                context.setVariable("userEmail", userAccountDTO.getEmail());

                emailService.sendHtmlEmail(
                        userAccountDTO.getEmail(),
                        "Chào mừng đến với Modish Motion",
                        "email/register-success",
                        context
                );
            } catch (MessagingException e) {
                // Log error but continue with registration
                System.err.println("Failed to send welcome email: " + e.getMessage());
            }
            return loginController.login(
                    userAccountDTO.getUsername(),
                    rawPassword,
                    model,
                    redirectAttributes
            );
        }

        redirectAttributes.addFlashAttribute("errorMessage", result);
        return "redirect:/login";
    }
}
