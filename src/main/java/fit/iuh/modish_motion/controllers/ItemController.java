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

import java.util.Optional;

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
        ItemDTO item  = itemService.findById(id.toString()).isPresent() ? itemService.findById(id.toString()).get() : null;
        model.addAttribute("product", item);

        return "itemDetail";
    }
}