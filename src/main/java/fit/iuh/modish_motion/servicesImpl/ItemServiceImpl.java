package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.entities.Item;
import fit.iuh.modish_motion.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fit.iuh.modish_motion.repositories.ItemRepository;
import fit.iuh.modish_motion.dto.ItemDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<ItemDTO> findRelatedItems(int categoryId, String itemId, int limit) {
        return itemRepository.findRelatedItems(categoryId, itemId, limit)
                .stream()
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
    public long countByCategoryId(int categoryId) {
        return itemRepository.countByCategoryId(categoryId);
    }
    // Remove methods related to filtering by size and color
}
