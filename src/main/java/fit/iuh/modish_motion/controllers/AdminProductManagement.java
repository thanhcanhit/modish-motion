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
                               @RequestParam(required = false) String search,
                               @RequestParam(required = false) String selectedItemId,
                               Model model) {
        Pageable pageable = PageRequest.of(page, 10);

        // Tìm kiếm hoặc phân trang sản phẩm
        Page<ItemDTO> items;
        if (search != null && !search.isEmpty()) {
            items = itemService.searchByName(search, pageable);
        } else {
            items = itemService.findByPage(pageable);
        }

        // Lấy danh sách biến thể nếu có sản phẩm được chọn
        List<VariantDTO> variants = List.of(); // Mặc định danh sách biến thể rỗng
        if (selectedItemId != null && !selectedItemId.isEmpty()) {
            variants = variantService.findByItemId(selectedItemId);
        }

        // Thêm dữ liệu vào model
        model.addAttribute("items", items.getContent()); // Danh sách sản phẩm
        model.addAttribute("totalPages", items.getTotalPages()); // Tổng số trang sản phẩm
        model.addAttribute("currentPage", page); // Trang hiện tại
        model.addAttribute("search", search); // Giá trị tìm kiếm
        model.addAttribute("selectedItemId", selectedItemId); // ID sản phẩm được chọn
        model.addAttribute("variants", variants); // Danh sách biến thể
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
        return itemService.findByIdWithoutVariant(id).orElse(new ItemDTO());
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
