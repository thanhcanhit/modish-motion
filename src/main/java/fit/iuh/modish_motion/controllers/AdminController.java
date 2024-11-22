package fit.iuh.modish_motion.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.entities.User;
import fit.iuh.modish_motion.services.AccountService;
import fit.iuh.modish_motion.services.UserService;
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
    public String getAccountList(Model model) {
        List<AccountDTO> accounts = accountService.findAll();
        model.addAttribute("accounts", accounts);
        return "dashboard-users";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<UserDTO> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    // Trang thêm người dùng mới
    @GetMapping("/users/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/add-user";
    }

    // Xử lý thêm người dùng
    @PostMapping("/users/add")
    public String addUser(@ModelAttribute("user") User user) {
        Optional<UserDTO> userDTO = userService.findById(user.getId());
        userService.save(userDTO.get());
        return "redirect:/admin/users";
    }

    // Trang chỉnh sửa thông tin người dùng
    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Integer id, Model model) {
        Optional<UserDTO> user = userService.findById(id);
        model.addAttribute("user", user);
        return "admin/edit-user";
    }

    // Xử lý cập nhật thông tin người dùng
    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Integer id, @ModelAttribute("user") User user) {
        userService.updateUser(id, UserDTO.fromEntity(user));
        return "redirect:/admin/users";
    }

    // Xóa người dùng
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

}
