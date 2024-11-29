package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.ColorDTO;
import fit.iuh.modish_motion.dto.ItemDTO;
import fit.iuh.modish_motion.dto.SizeDTO;
import fit.iuh.modish_motion.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ItemController {
    private final ItemService itemService;
    private static final int PAGE_SIZE = 12;

    @Autowired
    ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public String getProductById(Model model, @PathVariable Integer id) {
        Optional<ItemDTO> itemSearch = itemService.findById(id.toString());
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

        List<ColorDTO> allColors = itemService.findAllColorsBySearchQuery(query);
        List<SizeDTO> allSizes = itemService.findAllSizesBySearchQuery(query);
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
