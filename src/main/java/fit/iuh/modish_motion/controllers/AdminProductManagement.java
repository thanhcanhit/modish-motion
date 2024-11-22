package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.ItemDTO;
import fit.iuh.modish_motion.dto.VariantDTO;
import fit.iuh.modish_motion.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/products")
public class AdminProductManagement {

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
    public String getAdminPage(@RequestParam(defaultValue = "0") int page,
                               Model model) {
        // Tạo Pageable cho phân trang
        Pageable pageable = PageRequest.of(page, 10);
        Page<ItemDTO> items = itemService.findByPage(pageable);


        // Đảm bảo items không null
//        if (items == null) items = ;
        model.addAttribute("items", items.getContent()); // Danh sách các item
        model.addAttribute("totalPages", items.getTotalPages()); // Tổng số trang
        model.addAttribute("items", items);
        model.addAttribute("currentPage", page); // Trang hiện tại
        model.addAttribute("variants", List.of()); // Biến thể rỗng khi không chọn sản phẩm
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        model.addAttribute("colors", colorService.findAll());

        return "admin/admin";
    }

    @GetMapping("/{id}")
    public String getItemWithVariants(@PathVariable String id,
                                      @RequestParam int page,
                                      Model model) {
        // Tạo Pageable cho phân trang
        Pageable pageable = PageRequest.of(page, 10);
        Page<ItemDTO> items = itemService.findByPage(pageable);
        List<VariantDTO> variants = variantService.findByItemId(id);

        if (variants == null) variants = List.of();

        model.addAttribute("items", items.getContent()); // Danh sách các item
        model.addAttribute("totalPages", items.getTotalPages()); // Tổng số trang
        model.addAttribute("currentPage", page); // Trang hiện tại
        model.addAttribute("variants", variants);
        model.addAttribute("selectedItemId", id);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        model.addAttribute("colors", colorService.findAll());

        return "admin/admin";
    }
    @GetMapping("/{id}/variants/get")
    @ResponseBody
    public VariantDTO getVariant(@PathVariable String id, @RequestParam String variantId) {
        return variantService.findById(variantId).orElse(new VariantDTO());
    }



    // Lấy thông tin sản phẩm để mở modal
    @GetMapping("/get")
    @ResponseBody
    public ItemDTO getProduct(@RequestParam String id) {
        return itemService.findById(id).orElse(new ItemDTO());
    }



    // Lưu sản phẩm
    @PostMapping("/save")
    @ResponseBody
    public String saveProduct(@RequestBody ItemDTO itemDTO) {
        if (itemDTO.getId().trim().isEmpty()) {
            itemDTO.setId(UUID.randomUUID().toString());
            itemDTO.setVariants(List.of());
            itemService.save(itemDTO);
        } else {
            itemService.update(itemDTO);
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
        if (variantDTO.getId().trim().isEmpty()) {
            variantDTO.setId(UUID.randomUUID().toString());
            variantService.save(variantDTO);
        } else {
            variantService.update(variantDTO);
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
