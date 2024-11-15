package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.ItemDTO;
import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.services.ItemService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // Add this import

import java.util.*;

@Controller
@RequestMapping("/product")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public String getProductById(Model model, @PathVariable Integer id) {
        Optional<ItemDTO> itemSearch = itemService.findById(id.toString());
        ItemDTO item  = itemSearch.orElse(null);
        model.addAttribute("product", item);
        System.out.println(item);

        return "itemDetail";
    }

    @GetMapping("/filter")
    public String filterItems(@RequestParam Map<String, String> params, Model model) {
        Map<String, List<String>> filters = new HashMap<>();
        if (params.containsKey("colors")) {
            filters.put("colors", Arrays.asList(params.get("colors").split(",")));
        }
        if (params.containsKey("sizes")) {
            filters.put("sizes", Arrays.asList(params.get("sizes").split(",")));
        }
        if (params.containsKey("priceRange")) {
            filters.put("priceRange", Collections.singletonList(params.get("priceRange")));
        }
        List<ItemDTO> filteredItems = itemService.findByFilters(filters);
        model.addAttribute("items", filteredItems);
        return "search"; // Return the search template with filtered items
    }
}
