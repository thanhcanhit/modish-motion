package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.entities.User;
import fit.iuh.modish_motion.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    // Trang danh sách người dùng
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
