package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.CategoryDTO;
import fit.iuh.modish_motion.dto.ItemDTO;
import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.services.CategoryService;
import fit.iuh.modish_motion.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    // Sử dụng interface với Autowired thay vì Impl
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ItemService itemService;

    @GetMapping("/cat")
    public ResponseEntity<String> getData(Model model) {
        List<CategoryDTO> cats = categoryService.findAll();
        StringBuilder responseBuilder = new StringBuilder("<h1>Category List</h1><ul>");

        for (CategoryDTO cat : cats) {
            responseBuilder.append("<li>ID: ").append(cat.getId())
                    .append(", Name: ").append(cat.getCategoryName())
                    .append("</li>");
        }

        responseBuilder.append("</ul>");

        return ResponseEntity.ok(responseBuilder.toString());
    }

    @GetMapping("/items")
    public ResponseEntity<String> getItems(Model model) {
        List<ItemDTO> items = itemService.findAll();
        StringBuilder responseBuilder = new StringBuilder("<h1>Items List</h1><ul>");

        for (ItemDTO item : items) {
            responseBuilder.append("<li>ID: ").append(item.getId())
                    .append(", Name: ").append(item.getName())
                    .append(", Cat name: ").append(item.getCategory().getCategoryName())
                    .append("</li>");
        }

        responseBuilder.append("</ul>");

        return ResponseEntity.ok(responseBuilder.toString());
    }
}
