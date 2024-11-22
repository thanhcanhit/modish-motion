package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.ItemDTO;
import fit.iuh.modish_motion.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final ItemService itemService;

    @Autowired
    public SearchController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/search")
    public String searchItems(@RequestParam("query") String query, Model model) {
        List<ItemDTO> items = itemService.searchItemsByName(query);
        model.addAttribute("items", items);
        model.addAttribute("searchQuery", query);
        return "search";
    }
}
