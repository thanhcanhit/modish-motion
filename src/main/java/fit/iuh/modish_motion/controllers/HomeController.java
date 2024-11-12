package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.entities.Category;
import fit.iuh.modish_motion.repositories.CategoryRepository;
import fit.iuh.modish_motion.services.AccountService;
import fit.iuh.modish_motion.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {
    private final UserService userService;
    private final AccountService accountService;
    private final CategoryRepository categoryRepository;

    @Autowired
    public HomeController(UserService userService,
                          AccountService accountService,
                          CategoryRepository categoryRepository) {
        this.userService = userService;
        this.accountService = accountService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        return "home";
    }

    @GetMapping("/data")
    public ResponseEntity<String> getData(Model model) {
        List<UserDTO> users = userService.findAll(); // Gọi service để lấy dữ liệu người dùng dưới dạng DTO
        StringBuilder responseBuilder = new StringBuilder("<h1>User List</h1><ul>");

        for (UserDTO user : users) {
            responseBuilder.append("<li>ID: ").append(user.getId())
                    .append(", Name: ").append(user.getName())
                    .append(", Email: ").append(user.getEmail())
                    .append("</li>");
        }

        responseBuilder.append("</ul>");

        return ResponseEntity.ok(responseBuilder.toString());
    }

    @GetMapping("/datapage")
    public ResponseEntity<String> getPage(@PageableDefault(size = 10) Pageable pageable) {
        Page<UserDTO> users = userService.findByPage(pageable); // Gọi service để lấy dữ liệu người dùng dưới dạng DTO
        StringBuilder responseBuilder = new StringBuilder("<h1>User List</h1><ul>");

        for (UserDTO user : users) {
            responseBuilder.append("<li>ID: ").append(user.getId())
                    .append(", Name: ").append(user.getName())
                    .append(", Email: ").append(user.getEmail())
                    .append("</li>");
        }

        responseBuilder.append("</ul>");

        return ResponseEntity.ok(responseBuilder.toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Integer id) {
        return accountService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
