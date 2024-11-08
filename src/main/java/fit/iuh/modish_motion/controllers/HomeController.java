package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.servicesImpl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final UserServiceImpl userServiceImpl;

    public HomeController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
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
}
