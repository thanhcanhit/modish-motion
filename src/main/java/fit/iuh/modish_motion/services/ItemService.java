package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.ItemDTO;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<ItemDTO> findAll();
    Optional<ItemDTO> findById(String id);
    ItemDTO save(ItemDTO item);
    void deleteById(String id);
    List<ItemDTO> findRandomItemsByCategory(int categoryId, int count);
    List<ItemDTO> findByCategoryId(int categoryId);
    List<ItemDTO> findRelatedItems(int categoryId, String itemId, int limit);
    List<ItemDTO> searchItemsByName(String name);
    long countByCategoryId(int categoryId);
}
