package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.ItemDTO;
import fit.iuh.modish_motion.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public String getProductById(Model model, @PathVariable String id) {
        Optional<ItemDTO> itemSearch = itemService.findById(id);
        ItemDTO item = itemSearch.orElse(null);
        model.addAttribute("product", item);

        if (item != null && item.getCategory() != null) {
            List<ItemDTO> relatedProducts = itemService.findRelatedItems(
                item.getCategory().getId(), 
                item.getId(), 
                4
            );
            model.addAttribute("relatedProducts", relatedProducts);
        }

        return "itemDetail";
    }

    @GetMapping("/search")
    public String searchItems(@RequestParam("query") String query, Model model) {
        List<ItemDTO> items = itemService.searchItemsByName(query);
        long totalItems = items.size();
        model.addAttribute("items", items);
        model.addAttribute("searchQuery", query);
        model.addAttribute("totalItems", totalItems);
        return "search";
    }
}
