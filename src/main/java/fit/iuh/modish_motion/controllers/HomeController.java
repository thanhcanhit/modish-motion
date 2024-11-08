package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.servicesImpl.AccountServiceImpl;
import fit.iuh.modish_motion.servicesImpl.UserServiceImpl;
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
    /* Cách làm không đúng, đúng ra là phải sử dụng
        interface UserService với Autowired, xem file CategoryController
    * */
    private final UserServiceImpl userServiceImpl;
    private final AccountServiceImpl accountServiceImpl;

    public HomeController(UserServiceImpl userServiceImpl, AccountServiceImpl accountServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.accountServiceImpl = accountServiceImpl;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        return "home";
    }


    @GetMapping("/data")
    public ResponseEntity<String> getData(Model model) {
        List<UserDTO> users = userServiceImpl.findAll(); // Gọi service để lấy dữ liệu người dùng dưới dạng DTO
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
        Page<UserDTO> users = userServiceImpl.findByPage(pageable); // Gọi service để lấy dữ liệu người dùng dưới dạng DTO
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
        return accountServiceImpl.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
