package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.ItemDTO;
import fit.iuh.modish_motion.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<ItemDTO> findAll();
    Optional<ItemDTO> findById(String id);
    ItemDTO save(ItemDTO item);

    void update(ItemDTO item);
    void deleteById(String id);
    List<ItemDTO> findRandomItemsByCategory(int categoryId, int count);

    List<ItemDTO> findRandomItems(int count);

    List<ItemDTO> findByCategoryId(int categoryId, int page, int size);

    List<ItemDTO> findByCategoryId(int categoryId);
    Page<ItemDTO> findByPage(Pageable pageable);
    List<ItemDTO> findRelatedItems(int categoryId, String itemId, int limit);
    List<ItemDTO> findByCategoryIdAndFilter(int categoryId, List<String> colors, List<String> sizes, int page, int size);
    List<ItemDTO> searchItemsByName(String name);
    long countByCategoryId(int categoryId);
    long countByCategoryIdAndFilter(int categoryId, List<String> colors, List<String> sizes);
}
