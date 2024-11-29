package fit.iuh.modish_motion.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.dto.UserAccountDTO;
import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.entities.User;
import fit.iuh.modish_motion.services.AccountService;
import fit.iuh.modish_motion.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
public class AdminController {

    @Autowired
    private UserService userService;
    private AccountService accountService;

    public AdminController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String adminRedirect() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Kiểm tra quyền của người dùng
            boolean isAdmin = authentication.getAuthorities()
                    .stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
            if (isAdmin) {
                return "redirect:/admin/dashboard"; // Điều hướng tới dashboard nếu là admin
            } else {
                return "redirect:/not-found"; // Điều hướng tới not-found nếu không phải admin
            }
        }

        return "redirect:/login"; // Điều hướng tới login nếu chưa đăng nhập
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(@RequestParam(value = "tab", defaultValue = "products") String tab,
                            Model model) {
        model.addAttribute("selectedTab", tab);

        if ("users".equals(tab)) {
            // Thêm danh sách tài khoản vào model
            List<AccountDTO> accounts = accountService.findAll();
            model.addAttribute("accounts", accounts);
        }
        return "dashboard";
    }
    @GetMapping("/admin/check-auth")
    public ResponseEntity<?> checkAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Authentication details: " + auth);
    }
@GetMapping("/admin/dashboard/users")
public String getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "") String filter,
        Model model) {
    int totalAccounts = accountService.count();
    Page<AccountDTO> accounts = accountService.findByPage(PageRequest.of(page, size));

    if ("Admin".equals(filter)) {
        accounts = accountService.findByRole(true, PageRequest.of(page, size));
    } else if ("Normal User".equals(filter)) {
        accounts = accountService.findByRole(false, PageRequest.of(page, size));
    }

    model.addAttribute("accounts", accounts.getContent());
    model.addAttribute("totalAccounts", totalAccounts);
    model.addAttribute("totalPages", accounts.getTotalPages());
    model.addAttribute("currentPage", page);

    model.addAttribute("filter", filter);

    return "dashboard-users";
}
}
