package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.CategoryDTO;
import fit.iuh.modish_motion.dto.ColorDTO;
import fit.iuh.modish_motion.dto.ItemDTO;
import fit.iuh.modish_motion.dto.SizeDTO;
import fit.iuh.modish_motion.services.CategoryService;
import fit.iuh.modish_motion.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

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
            @RequestParam(required = false) List<Integer> colors,
            @RequestParam(required = false) List<Integer> sizes,
            @RequestParam(required = false) String sortBy,
            Model model
    ) {
        CategoryDTO category = categoryService.findByName(categoryName);
        List<CategoryDTO> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        if (category != null) {
            List<ItemDTO> items = itemService.findByCategoryId(category.getId());

            // Extract unique colors and sizes from variants
            Map<String, List<ColorDTO>> groupedColors = new HashMap<>();
            Map<String, List<SizeDTO>> groupedSizes = new HashMap<>();

            items.forEach(item -> {
                if (item.getVariants() != null) {
                    item.getVariants().forEach(variant -> {
                        if (variant.getColor() != null) {
                            groupedColors.computeIfAbsent(variant.getColor().getColor(), k -> new ArrayList<>()).add(variant.getColor());
                        }
                        if (variant.getSize() != null) {
                            groupedSizes.computeIfAbsent(variant.getSize().getSize(), k -> new ArrayList<>()).add(variant.getSize());
                        }
                    });
                }
            });

            // Apply filters if present
            if (colors != null && !colors.isEmpty() ||
                    sizes != null && !sizes.isEmpty()) {

                items = items.stream()
                        .filter(item -> {
                            // Check if any variant matches all filters
                            return item.getVariants().stream().anyMatch(variant -> {
                                boolean colorMatch = colors == null || colors.isEmpty() ||
                                        (variant.getColor() != null &&
                                                colors.contains(variant.getColor().getId()));

                                boolean sizeMatch = sizes == null || sizes.isEmpty() ||
                                        (variant.getSize() != null &&
                                                sizes.contains(variant.getSize().getId()));

                                return colorMatch && sizeMatch;
                            });
                        })
                        .collect(Collectors.toList());
            }

            // Apply sorting if present
            if (sortBy != null) {
                switch (sortBy) {
                    case "priceAsc":
                        items.sort(Comparator.comparing(item -> item.getVariants().get(0).getPrice()));
                        break;
                    case "priceDesc":
                        items.sort(Comparator.comparing(item -> item.getVariants().get(0).getPrice(), Comparator.reverseOrder()));
                        break;
                    // Add more sorting options as needed
                }
            }

            model.addAttribute("items", items);
            model.addAttribute("category", category);
            model.addAttribute("groupedColors", groupedColors);
            model.addAttribute("groupedSizes", groupedSizes);
        }
        return "category";
    }
}