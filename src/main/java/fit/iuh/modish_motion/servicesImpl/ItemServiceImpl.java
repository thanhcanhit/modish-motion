package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.entities.Item;
import fit.iuh.modish_motion.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fit.iuh.modish_motion.repositories.ItemRepository;
import fit.iuh.modish_motion.dto.ItemDTO;
import java.util.stream.Collectors;
import java.util.Map;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<ItemDTO> findAll() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .filter(item -> item.getVariants() != null && !item.getVariants().isEmpty())
                .map(ItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ItemDTO> findById(String id) {
        return itemRepository.findById(id)
                .filter(item -> item.getVariants() != null && !item.getVariants().isEmpty())
                .map(ItemDTO::fromEntity);
    }

    @Override
    public ItemDTO save(ItemDTO itemDTO) {
        Item item = itemDTO.toEntity();
        Item savedItem = itemRepository.save(item);
        return ItemDTO.fromEntity(savedItem);
    }

    @Override
    public void deleteById(String id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDTO> findRandomItemsByCategory(int categoryId, int count) {
        return itemRepository.findRandomItemsByCategory(categoryId, count)
                .stream()
                .filter(item -> item.getVariants() != null && !item.getVariants().isEmpty())
                .map(ItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> findByCategoryId(int categoryId) {
        List<Item> items = itemRepository.findByCategoryId(categoryId);
        return items.stream()
                .filter(item -> item.getVariants() != null && !item.getVariants().isEmpty())
                .map(ItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> searchItemsByName(String name) {
        List<Item> items = itemRepository.findByNameContaining(name);
        return items.stream()
                .filter(item -> item.getVariants() != null && !item.getVariants().isEmpty())
                .map(ItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> findByFilters(Map<String, List<String>> filters) {
        List<Item> items = itemRepository.findAll(); // Fetch all items initially
        // Apply filters
        if (filters.containsKey("colors")) {
            List<String> colors = (List<String> )filters.get("colors");
            items = items.stream()
                    .filter(item -> item.getVariants().stream().anyMatch(variant -> colors.contains(variant.getColor().getColor())))
                    .collect(Collectors.toList());
        }
        if (filters.containsKey("sizes")) {
            List<String> sizes = filters.get("sizes");
            items = items.stream()
                    .filter(item -> item.getVariants().stream().anyMatch(variant -> sizes.contains(variant.getSize().getSize())))
                    .collect(Collectors.toList());
        }
        if (filters.containsKey("priceRange")) {
            String priceRange = filters.get("priceRange").get(0);
            String[] range = priceRange.split("-");
            double minPrice = Double.parseDouble(range[0]);
            double maxPrice = Double.parseDouble(range[1]);
            items = items.stream()
                    .filter(item -> item.getVariants().stream().anyMatch(variant -> variant.getPrice() >= minPrice && variant.getPrice() <= maxPrice))
                    .collect(Collectors.toList());
        }
        return items.stream()
                .filter(item -> item.getVariants() != null && !item.getVariants().isEmpty())
                .map(ItemDTO::fromEntity)
                .collect(Collectors.toList());
    }
}