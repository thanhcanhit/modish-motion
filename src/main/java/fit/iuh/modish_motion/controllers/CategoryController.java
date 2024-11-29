package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.CategoryDTO;
import fit.iuh.modish_motion.dto.ColorDTO;
import fit.iuh.modish_motion.dto.ItemDTO;
import fit.iuh.modish_motion.dto.SizeDTO;
import fit.iuh.modish_motion.services.CategoryService;
import fit.iuh.modish_motion.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ItemService itemService;
    private static final int PAGE_SIZE = 12;

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
    public String getItemByCategoryAndFilter(
            @PathVariable("categoryName") String categoryName,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "colors", required = false) List<Integer> colors,
            @RequestParam(value = "sizes", required = false) List<Integer> sizes,
            @RequestParam(value = "price", defaultValue = "0-1000000000") String price,
            Model model) {

        // Get category or redirect if not found
        CategoryDTO category = categoryService.findByName(categoryName);
        if (category == null) {
            return "redirect:/";
        }

            // Initialize empty lists if null (when coming from header)


            // Parse price range
            String[] priceRange = price.split("-");
            double minPrice = Double.parseDouble(priceRange[0]);
            double maxPrice = priceRange.length > 1 ?
                    Double.parseDouble(priceRange[1]) : Double.MAX_VALUE;

            // Get paginated and filtered items
            Page<ItemDTO> itemPage = itemService.findByCategoryIdAndFilter(
                    category.getId(), colors, sizes, minPrice, maxPrice, page, PAGE_SIZE
            );

            // Get all available colors and sizes for the category
            List<ColorDTO> allColors = itemService.findAllColorsByCategoryId(category.getId());
            List<SizeDTO> allSizes = itemService.findAllSizesByCategoryId(category.getId());
        colors = colors != null ? colors : new ArrayList<>();
        sizes = sizes != null ? sizes : new ArrayList<>();

            // Add attributes to model
            model.addAttribute("items", itemPage.getContent());
            model.addAttribute("category", category);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", itemPage.getTotalPages());
            model.addAttribute("totalItems", itemPage.getTotalElements());
            model.addAttribute("allColors", allColors);
            model.addAttribute("allSizes", allSizes);
            model.addAttribute("selectedColors", colors);
            model.addAttribute("selectedSizes", sizes);
            model.addAttribute("selectedPrice", price);
            model.addAttribute("hasMore", page < itemPage.getTotalPages());

            return "category";


    }

    @GetMapping("/search")
    public String searchItems(
            @RequestParam("query") String query,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "colors", required = false) List<Integer> colors,
            @RequestParam(value = "sizes", required = false) List<Integer> sizes,
            @RequestParam(value = "price", defaultValue = "0-1000000000") String price,
            Model model) {

        String[] priceRange = price.split("-");
        double minPrice = Double.parseDouble(priceRange[0]);
        double maxPrice = priceRange.length > 1 ? Double.parseDouble(priceRange[1]) : Double.MAX_VALUE;

        Page<ItemDTO> itemPage = itemService.searchItemsByNameAndFilter(
                query, colors, sizes, minPrice, maxPrice, page, PAGE_SIZE
        );

        List<ColorDTO> allColors = itemService.findAllColorsByCategoryId(0); // Assuming 0 for all categories
        List<SizeDTO> allSizes = itemService.findAllSizesByCategoryId(0); // Assuming 0 for all categories
        colors = colors != null ? colors : new ArrayList<>();
        sizes = sizes != null ? sizes : new ArrayList<>();

        model.addAttribute("items", itemPage.getContent());
        model.addAttribute("searchQuery", query);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", itemPage.getTotalPages());
        model.addAttribute("totalItems", itemPage.getTotalElements());
        model.addAttribute("allColors", allColors);
        model.addAttribute("allSizes", allSizes);
        model.addAttribute("selectedColors", colors);
        model.addAttribute("selectedSizes", sizes);
        model.addAttribute("selectedPrice", price);
        model.addAttribute("hasMore", page < itemPage.getTotalPages());

        return "search";
    }
}
