package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.ItemDTO;

import java.util.List;
import java.util.Optional;
import java.util.Map;

public interface ItemService {
    List<ItemDTO> findAll();
    Optional<ItemDTO> findById(String id);
    ItemDTO save(ItemDTO item);
    void deleteById(String id);
    List<ItemDTO> findRandomItemsByCategory(int categoryId, int count);
    List<ItemDTO> findByCategoryId(int categoryId);
    List<ItemDTO> searchItemsByName(String name);
    List<ItemDTO> findByFilters(Map<String, List<String>> filters);
}