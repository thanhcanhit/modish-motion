package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.ItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<ItemDTO> findAll();
    Optional<ItemDTO> findById(String id);
    ItemDTO save(ItemDTO item);
    void deleteById(String id);
    Page<ItemDTO> findByPage(Pageable pageable);
    List<ItemDTO> findRandomItemsByCategory(int categoryId, int count);
    List<ItemDTO> findByCategoryId(int categoryId);
    List<ItemDTO> findRelatedItems(int categoryId, String itemId, int limit);
}