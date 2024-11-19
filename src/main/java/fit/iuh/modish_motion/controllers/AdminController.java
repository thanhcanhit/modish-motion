package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.*;
import fit.iuh.modish_motion.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/adminFake")
public class AdminController {
    @Autowired
    private ItemService itemService;

    @Autowired
    private VariantService variantService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SizeService sizeService;
    @Autowired
    private ColorService colorService;

    // Hiển thị trang quản lý sản phẩm
    @GetMapping
    public String getAdminPage(Model model, @RequestParam(defaultValue = "") String search) {
        List<ItemDTO> items = search.isEmpty()
                ? itemService.findRandomItems(10)
                : itemService.findByCategoryId(Integer.parseInt(search));

        // Đảm bảo items không null
        if (items == null) items = List.of();

        model.addAttribute("items", items);
        model.addAttribute("variants", List.of()); // Biến thể rỗng khi không chọn sản phẩm
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("search", search);

        return "admin/admin";
    }

    @GetMapping("/{id}")
    public String getItemWithVariants(@PathVariable String id, Model model, @RequestParam(defaultValue = "") String search) {
        List<ItemDTO> items = search.isEmpty()
                ? itemService.findRandomItems(10)
                : itemService.findByCategoryId(Integer.parseInt(search));

        List<VariantDTO> variants = variantService.findByItemId(id);

        // Đảm bảo items và variants không null
        if (items == null) items = List.of();
        if (variants == null) variants = List.of();

        model.addAttribute("items", items);
        model.addAttribute("variants", variants);
        model.addAttribute("selectedItemId", id);
        model.addAttribute("search", search);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        model.addAttribute("colors", colorService.findAll());

        return "admin/admin";
    }



    // Lấy thông tin sản phẩm để mở modal
    @GetMapping("/get")
    @ResponseBody
    public ItemDTO getProduct(@RequestParam String id) {
        return itemService.findById(id).orElse(new ItemDTO());
    }

    // Lấy danh sách biến thể cho một sản phẩm
    @GetMapping("/{id}/variants")
    @ResponseBody
    public List<VariantDTO> getVariants(@PathVariable String id) {
         return variantService.findByItemId(id).stream().toList();
    }


    // Lưu sản phẩm
    @PostMapping("/save")
    @ResponseBody
    public String saveProduct(@RequestBody ItemDTO itemDTO) {
        if (itemDTO.getId() == null || itemDTO.getId().isEmpty()) {
            itemService.save(itemDTO); // Thêm sản phẩm mới
        } else {
            itemService.save(itemDTO); // Sửa sản phẩm
        }
        return "success";
    }

    // Xóa sản phẩm
    @PostMapping("/delete")
    @ResponseBody
    public String deleteProduct(@RequestParam String id) {
        itemService.deleteById(id);
        return "success";
    }

    // Lưu biến thể
    @PostMapping("/{id}/variants/save")
    @ResponseBody
    public String saveVariant(@PathVariable String id, @RequestBody VariantDTO variantDTO) {
        variantDTO.setItemId(id);
        if (variantDTO.getId() == null || variantDTO.getId().isEmpty()) {
            variantService.save(variantDTO); // Thêm biến thể mới
        } else {
            variantService.save(variantDTO); // Sửa biến thể
        }
        return "success";
    }

    // Xóa biến thể
    @PostMapping("/{id}/variants/delete")
    @ResponseBody
    public String deleteVariant(@PathVariable String id, @RequestParam String variantId) {
        variantService.deleteById(variantId);
        return "success";
    }
}
