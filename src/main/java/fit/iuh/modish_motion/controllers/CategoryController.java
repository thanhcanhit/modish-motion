package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.CategoryDTO;
import fit.iuh.modish_motion.dto.ItemDTO;
import fit.iuh.modish_motion.services.CategoryService;
import fit.iuh.modish_motion.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ItemService itemService;

    @Autowired
    public CategoryController(CategoryService categoryService, ItemService itemService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryName}")
    public String getItemsByCategory(
            @PathVariable("categoryName") String categoryName,
            Model model) {
        CategoryDTO category = categoryService.findByName(categoryName);
        if (category != null) {
            List<ItemDTO> items = itemService.findByCategoryId(category.getId());
//            long totalItems = itemService.countByCategoryId(category.getId());
            long totalItems = items.size();
            model.addAttribute("items", items);
            model.addAttribute("category", category);
            model.addAttribute("totalItems", totalItems);
        }
        return "category";
    }
}
