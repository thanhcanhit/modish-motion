package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.entities.Category;
import fit.iuh.modish_motion.entities.Item;
import fit.iuh.modish_motion.repositories.CategoryRepository;
import fit.iuh.modish_motion.repositories.ItemRepository;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

import java.util.List;
import java.util.Map;

@Controller
public class HomeControllerFake {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public HomeControllerFake(CategoryRepository categoryRepository, ItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }

    @GetMapping("/homeFake")
    public String homePage(Model model,@RequestParam(required = false) Integer categoryId){
        // Danh sách các ảnh banner
        List<Map<String, String>> banners = List.of(
                Map.of("imageUrl", "/images/home/imageBanner1.png", "link", ""),
                Map.of("imageUrl", "/images/home/imageBanner2.png", "link", ""),
                Map.of("imageUrl", "/images/home/imageBanner3.png", "link", ""),
                Map.of("imageUrl", "/images/home/imageBanner4.jpg", "link", "")
        );
        model.addAttribute("banners", banners);
        List<Category> randomCategories = categoryRepository.findRandomCategories(4);
        model.addAttribute("categories", randomCategories);

        // Chọn danh mục đầu tiên làm mặc định
        Category defaultCategory = randomCategories.get(0);

        // Lấy 4 sản phẩm ngẫu nhiên cho phần "Sản phẩm ưa chuộng"
        List<Item> popularItems = itemRepository.findRandomItemsByCategory(defaultCategory.getId(), 4);
        model.addAttribute("popularItems", popularItems);

        // Lấy 20 sản phẩm ngẫu nhiên cho danh mục mặc định trong phần "Gợi ý sản phẩm"
        List<Item> suggestedItems = itemRepository.findRandomItemsByCategory(defaultCategory.getId(), 20);
        model.addAttribute("suggestedItems", suggestedItems);

        // Truyền ID danh mục mặc định vào view
        model.addAttribute("selectedCategoryId", defaultCategory.getId());

        return "homeFake";
    }

    @GetMapping("/homeFake/products")
    public ResponseEntity<List<Item>> getProductsByCategory(@RequestParam Integer categoryId) {
        // Lấy 20 sản phẩm ngẫu nhiên cho danh mục được chọn
        List<Item> items = itemRepository.findRandomItemsByCategory(categoryId, 20);
        return ResponseEntity.ok(items);
    }
}

