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
import org.springframework.web.bind.annotation.RequestParam;

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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            Model model) {
        CategoryDTO category = categoryService.findByName(categoryName);
        if (category != null) {
            List<ItemDTO> items = itemService.findByCategoryId(category.getId(), page, size);
            long totalItems = itemService.countByCategoryId(category.getId());
            boolean hasMore = (page + 1) * size < totalItems;

            model.addAttribute("items", items);
            model.addAttribute("category", category);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalItems", totalItems);
            model.addAttribute("hasMore", hasMore);
        }
        return "category";
    }

    /**
     * Handles the request to load more items by category.
     *
     * @param categoryName the name of the category to load items from
     * @param page         the page number to load, defaults to 0
     * @param size         the number of items per page, defaults to 9
     * @param model        the model to add attributes to
     * @return the name of the fragment to render
     */
    @GetMapping("/{categoryName}/loadMore")
    public String loadMoreItemsByCategory(
            @PathVariable("categoryName") String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = true) List<String> colors,
            @RequestParam(required = true) List<String> sizes,
            Model model) {
        CategoryDTO category = categoryService.findByName(categoryName);
        if (category != null) {
            // kiểm tra xem có colors và sizes không
            if (colors.isEmpty() || sizes.isEmpty()) {
                List<ItemDTO> items = itemService.findByCategoryId(category.getId(), page, size);
                long totalItems = itemService.countByCategoryId(category.getId());
                boolean hasMore = (page + 1) * size < totalItems;

                model.addAttribute("items", items);
                model.addAttribute("currentPage", page);
                model.addAttribute("totalItems", totalItems);
                model.addAttribute("hasMore", hasMore);
                String allColors = "";
                for (ItemDTO item : items) {
                    for (int i = 0; i < item.getVariants().size(); i++) {
                        // allColors += String.format("{id: %d, color: '%s', hex: '%s'}",
                        // item.getVariants().get(i).getId(),
                        // item.getVariants().get(i).getColor().getColor(),
                        // item.getVariants().get(i).getColor().getHex());
                        String tmp = "{id: " + item.getVariants().get(i).getId() + ", color: '"
                                + item.getVariants().get(i).getColor().getColor() + "', hex: '"
                                + item.getVariants().get(i).getColor().getHex() + "'}";
                        // check xem đã có chưa
                        if (allColors.indexOf(tmp) == -1) {
                            allColors += tmp;
                            if (i < item.getVariants().size() - 1) {
                                allColors += "; ";
                            }
                        }
                    

                    }
                }
                String allSizes = "";
                for (ItemDTO item : items) {
                    for (int i = 0; i < item.getVariants().size(); i++) {
                        // allSizes += String.format("{id: %d, size: '%s'}",
                        // item.getVariants().get(i).getId(),
                        // item.getVariants().get(i).getSize().getSize());
                        // check xem đã có chưa
                        String tmp = "{id: " + item.getVariants().get(i).getId() + ", size: '"
                                + item.getVariants().get(i).getSize().getSize() + "'}";
                        if (allSizes.indexOf(tmp) == -1) {
                            allSizes += tmp;
                            if (i < item.getVariants().size() - 1) {
                                allSizes += "; ";
                            }
                        }
                    }
                }
                model.addAttribute("allColors", allColors);
                model.addAttribute("allSizes", allSizes);

                // Thêm log để kiểm tra
                System.out.println("Loaded " + items.size() + " items for page " + page);
                return "fragments/productItems :: productItems";
            }

            List<ItemDTO> items = itemService.findByCategoryIdAndFilter(category.getId(), colors, sizes, page, size);
            long totalItems = itemService.countByCategoryIdAndFilter(category.getId(), colors, sizes);
            boolean hasMore = (page + 1) * size < totalItems;

            // Lấy ra tòan bộ các màu và kích thước của sản phẩm, trả về dạng chuỗi, với
            // color là {id, color, hex}
            // và size là {id, size}
            String allColors = "";
            for (ItemDTO item : items) {
                for (int i = 0; i < item.getVariants().size(); i++) {
                    allColors += String.format("{id: %d, color: '%s', hex: '%s'}", item.getVariants().get(i).getId(),
                            item.getVariants().get(i).getColor().getColor(),
                            item.getVariants().get(i).getColor().getHex());
                    if (i < item.getVariants().size() - 1) {
                        allColors += ", ";
                    }
                }
            }
            String allSizes = "";
            for (ItemDTO item : items) {
                for (int i = 0; i < item.getVariants().size(); i++) {
                    allSizes += String.format("{id: %d, size: '%s'}", item.getVariants().get(i).getId(),
                            item.getVariants().get(i).getSize().getSize());
                    if (i < item.getVariants().size() - 1) {
                        allSizes += ", ";
                    }
                }
            }

            model.addAttribute("items", items);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalItems", totalItems);
            model.addAttribute("hasMore", hasMore);
            model.addAttribute("allColors", allColors);
            model.addAttribute("allSizes", allSizes);

            // Thêm log để kiểm tra
            System.out.println("Loaded " + items.size() + " items for page " + page);
        }

    return"fragments/productItems :: productItems";
}

// Viết lại cái loadMore, nhận thêm các đối số size và color và filter để lọc
// sản phẩm (tham số để trên URL)
// color, filter có thể không có, nếu không có thì mặc định là null, có thì là 1
// chuỗi
// Mỗi lần tìm sẽ load đủ item cho 1 trang, không cần phân trang
// Để trả về fragment productItems
// Gợi ý: sử dụng itemService.findByCategoryIdAndFilter
// Gợi ý: sử dụng model.addAttribute để truyền dữ liệu vào fragment

}
