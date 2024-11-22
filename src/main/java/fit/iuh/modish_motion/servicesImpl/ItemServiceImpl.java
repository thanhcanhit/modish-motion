package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.entities.Item;
import fit.iuh.modish_motion.repositories.CategoryRepository;
import fit.iuh.modish_motion.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fit.iuh.modish_motion.repositories.ItemRepository;
import fit.iuh.modish_motion.dto.ItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CategoryRepository categoryRepository;

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
    public void update(ItemDTO itemDTO) {
        Item existingItem = itemRepository.findById(itemDTO.getId())
                .orElseThrow(() -> new RuntimeException("Item not found"));
        existingItem.setName(itemDTO.getName());
        existingItem.setPromotionPrice(itemDTO.getPromotionPrice());
        existingItem.setTags(itemDTO.getTags());
        existingItem.setCategory(categoryRepository.findById(itemDTO.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        existingItem.setCharacteristic(itemDTO.getCharacteristic());
        existingItem.setGender(itemDTO.getGender());
        itemRepository.save(existingItem);
    }


    @Override
    public void deleteById(String id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDTO> findRandomItemsByCategory(int categoryId, int count) {
        return itemRepository.findRandomItemsByCategory(categoryId, count)
                .stream()
          .map(ItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> findRandomItems(int count) {
        return itemRepository.findRandomItems(count)
                .stream()
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
    
    public List<ItemDTO> findByCategoryId(int categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> itemsPage = itemRepository.findByCategoryId(categoryId, pageable);
        return itemsPage.stream()
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
    public Page<ItemDTO> findByPage(Pageable pageable) {
            return itemRepository.findAll(pageable)
                    .map(ItemDTO::fromEntity);

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

    @Override
    public List<ItemDTO> findByCategoryIdAndFilter(int categoryId, List<String> colors, List<String> sizes, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Check xem có phần tử nào trong colors và sizes không

        if (colors.isEmpty() && sizes.isEmpty()) {
            return findByCategoryId(categoryId, page, size);
        }

        return itemRepository.findByCategoryIdAndFilter(categoryId, colors, sizes, pageable)
                .stream()
                .map(ItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public long countByCategoryIdAndFilter(int categoryId, List<String> colors, List<String> sizes) {
        return itemRepository.countByCategoryIdAndFilter(categoryId, colors, sizes);
    }
}
