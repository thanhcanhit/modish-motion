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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

import java.util.Map;

@Controller
@SessionAttributes("selectedCategoryId")
public class HomeController {

    private final CategoryService categoryService;
    private final ItemService itemService;

    @Autowired
    public HomeController(CategoryService categoryService, ItemService itemService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        // Danh sách các ảnh banner
        List<Map<String, String>> banners = List.of(
                Map.of("imageUrl", "/images/home/imageBanner1.png", "link", ""),
                Map.of("imageUrl", "/images/home/imageBanner2.png", "link", ""),
                Map.of("imageUrl", "/images/home/imageBanner3.png", "link", ""),
                Map.of("imageUrl", "/images/home/imageBanner4.jpg", "link", "")
        );
        model.addAttribute("banners", banners);

        // Lấy 4 danh mục ngẫu nhiên
        List<CategoryDTO> randomCategories1 = categoryService.findRandomCategories(4);
        model.addAttribute("categories1", randomCategories1);
        List<CategoryDTO> randomCategories2 = categoryService.findRandomCategories(4);
        model.addAttribute("categories2", randomCategories2);

        // Nếu không có categoryId, chọn danh mục mặc định là danh mục đầu tiên
        CategoryDTO selectedCategory1 =  randomCategories1.get(0);
        CategoryDTO selectedCategory2 =  randomCategories1.get(1);
        CategoryDTO selectedCategory3 =  randomCategories1.get(2);
        CategoryDTO selectedCategory4 = randomCategories1.get(3);

        CategoryDTO selectedCategory5 = randomCategories2.get(0);
        CategoryDTO selectedCategory6 =randomCategories2.get(1);
        CategoryDTO selectedCategory7 = randomCategories2.get(2);
        CategoryDTO selectedCategory8 = randomCategories2.get(3);


        // Lấy 4 sản phẩm ngẫu nhiên cho phần "Sản phẩm ưa chuộng"
        List<ItemDTO> popularItems1 = itemService.findRandomItemsByCategory(selectedCategory1.getId(), 4);
        model.addAttribute("popularItems1", popularItems1);
        List<ItemDTO> popularItems2 = itemService.findRandomItemsByCategory(selectedCategory2.getId(), 4);
        model.addAttribute("popularItems2", popularItems2);
        List<ItemDTO> popularItems3 = itemService.findRandomItemsByCategory(selectedCategory3.getId(), 4);
        model.addAttribute("popularItems3", popularItems3);
        List<ItemDTO> popularItems4 = itemService.findRandomItemsByCategory(selectedCategory4.getId(), 4);
        model.addAttribute("popularItems4", popularItems4);


        // Lấy 20 sản phẩm ngẫu nhiên cho danh mục đã chọn trong phần "Gợi ý sản phẩm"
        List<ItemDTO> suggestedItems1 = itemService.findRandomItemsByCategory(selectedCategory5.getId(), 20);
        model.addAttribute("suggestedItems1", suggestedItems1);
        List<ItemDTO> suggestedItems2 = itemService.findRandomItemsByCategory(selectedCategory6.getId(), 20);
        model.addAttribute("suggestedItems2", suggestedItems2);
        List<ItemDTO> suggestedItems3 = itemService.findRandomItemsByCategory(selectedCategory7.getId(), 20);
        model.addAttribute("suggestedItems3", suggestedItems3);
        List<ItemDTO> suggestedItems4 = itemService.findRandomItemsByCategory(selectedCategory8.getId(), 20);
        model.addAttribute("suggestedItems4", suggestedItems4);

        // Truyền ID danh mục đã chọn vào view
        model.addAttribute("selectedCategoryId1", selectedCategory1.getId());
        model.addAttribute("selectedCategoryId5", selectedCategory5.getId());

        return "home";
    }


    @GetMapping("/products")
    public ResponseEntity<List<ItemDTO>> getProductsByCategory(@RequestParam Integer categoryId) {
        // Lấy 20 sản phẩm ngẫu nhiên cho danh mục được chọn
        List<ItemDTO> items = itemService.findRandomItemsByCategory(categoryId, 20);
        return ResponseEntity.ok(items);
    }
}

