package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.ColorDTO;
import fit.iuh.modish_motion.dto.ItemDTO;
import fit.iuh.modish_motion.dto.SizeDTO;
import org.springframework.data.domain.Page;

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
    Page<ItemDTO> findByCategoryIdAndFilter(
            int categoryId,
            List<Integer> colors,
            List<Integer> sizes,
            double minPrice,
            double maxPrice,
            int page,
            int size
    );

    Page<ItemDTO> searchItemsByNameAndFilter(
            String name,
            List<Integer> colors,
            List<Integer> sizes,
            double minPrice,
            double maxPrice,
            int page,
            int size
    );

    long countByFilters(
            int categoryId,
            List<Integer> colors,
            List<Integer> sizes,
            double minPrice,
            double maxPrice
    );

    List<ColorDTO> findAllColorsByCategoryId(int categoryId);
    List<SizeDTO> findAllSizesByCategoryId(int categoryId);
    List<ColorDTO> findAllColorsBySearchQuery(String query);
    List<SizeDTO> findAllSizesBySearchQuery(String query);
}
