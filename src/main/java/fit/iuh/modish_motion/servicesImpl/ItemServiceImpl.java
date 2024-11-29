package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.dto.ColorDTO;
import fit.iuh.modish_motion.dto.SizeDTO;
import fit.iuh.modish_motion.entities.Item;
import fit.iuh.modish_motion.repositories.CategoryRepository;
import fit.iuh.modish_motion.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import fit.iuh.modish_motion.repositories.ItemRepository;
import fit.iuh.modish_motion.dto.ItemDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    private boolean isValidItem(Item item) {
        return item != null && item.getVariants() != null && !item.getVariants().isEmpty();
    }

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
    public Optional<ItemDTO> findByIdWithoutVariant(String id) {
        return itemRepository.findById(id)
                .map(ItemDTO::fromEntity);
    }

    @Override
    public ItemDTO save(ItemDTO itemDTO) {
        Item item = itemDTO.toEntity();
        Item savedItem = itemRepository.save(item);
        return ItemDTO.fromEntity(savedItem);
    }

    @Override
    public Page<ItemDTO> searchByName(String name, Pageable pageable) {
        return itemRepository.searchByName(name,pageable)
                .map(ItemDTO::fromEntity);
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
    public Page<ItemDTO> findByCategoryIdAndFilter(
            int categoryId,
            List<Integer> colors,
            List<Integer> sizes,
            double minPrice,
            double maxPrice,
            int page,
            int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Item> itemPage = itemRepository.findByCategoryIdAndFilter(
                categoryId, colors, sizes, minPrice, maxPrice, pageable
        );

        // Filter out items without variants and create new Page
        List<ItemDTO> validItems = itemPage.getContent().stream()
                .filter(item -> item.getVariants() != null && !item.getVariants().isEmpty())
                .map(ItemDTO::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(
                validItems,
                pageable,
                itemPage.getTotalElements()
        );
    }

    @Override
    public Page<ItemDTO> searchItemsByNameAndFilter(
            String name,
            List<Integer> colors,
            List<Integer> sizes,
            double minPrice,
            double maxPrice,
            int page,
            int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Item> itemPage = itemRepository.searchByNameAndFilter(
                name, colors, sizes, minPrice, maxPrice, pageable
        );

        List<ItemDTO> validItems = itemPage.getContent().stream()
                .filter(item -> item.getVariants() != null && !item.getVariants().isEmpty())
                .map(ItemDTO::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(
                validItems,
                pageable,
                itemPage.getTotalElements()
        );
    }

    @Override
    public long countByFilters(
            int categoryId,
            List<Integer> colors,
            List<Integer> sizes,
            double minPrice,
            double maxPrice) {
        return itemRepository.countByFilters(categoryId, colors, sizes, minPrice, maxPrice);
    }

    @Override
    public List<ColorDTO> findAllColorsByCategoryId(int categoryId) {
        return itemRepository.findByCategoryId(categoryId).stream()
                .flatMap(item -> item.getVariants().stream())
                .map(variant -> variant.getColor())
                .distinct()
                .map(ColorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<SizeDTO> findAllSizesByCategoryId(int categoryId) {
        return itemRepository.findByCategoryId(categoryId).stream()
                .flatMap(item -> item.getVariants().stream())
                .map(variant -> variant.getSize())
                .distinct()
                .map(SizeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ColorDTO> findAllColorsBySearchQuery(String query) {
        return itemRepository.findByNameContaining2(query).stream()
                .flatMap(item -> item.getVariants().stream())
                .map(variant -> variant.getColor())
                .distinct()
                .map(ColorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<SizeDTO> findAllSizesBySearchQuery(String query) {
        return itemRepository.findByNameContaining2(query).stream()
                .flatMap(item -> item.getVariants().stream())
                .map(variant -> variant.getSize())
                .distinct()
                .map(SizeDTO::fromEntity)
                .collect(Collectors.toList());
    }
}