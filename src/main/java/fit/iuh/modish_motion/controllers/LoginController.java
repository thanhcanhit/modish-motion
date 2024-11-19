package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.services.AccountService;
import fit.iuh.modish_motion.services.UserService;
import fit.iuh.modish_motion.servicesImpl.AccountServiceImpl;
import fit.iuh.modish_motion.servicesImpl.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {
    private final AccountService accountService;
    @Autowired
    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }


    @PostMapping("/loginrequest")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model) {

        //boolean isAuthenticated = accountService.authenticate(username, password);
        Optional<AccountDTO> accountDTO = accountService.findByUserNameAndPassword(username, password);
        if (accountDTO.isPresent()) {
            AccountDTO account = accountDTO.get();

            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority(account.isAdmin() ? "ADMIN" : "USER")
            );
            Authentication auth = new UsernamePasswordAuthenticationToken(username, password, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/";  // Điều hướng về trang chủ
        }
        return "login";  // Hiển thị trang đăng nhập nếu chưa đăng nhập
    }

    @GetMapping("/user")
    public String userPage(Model model) {
        return "redirect:/profile";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        return "login";  // trả về trang login.html, trong đó đã gọi showSignUp()
    }

}
