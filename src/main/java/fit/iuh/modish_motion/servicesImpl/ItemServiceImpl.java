package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.entities.Item;
import fit.iuh.modish_motion.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import fit.iuh.modish_motion.repositories.ItemRepository;
import fit.iuh.modish_motion.dto.ItemDTO;
import java.util.stream.Collectors;

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
                .map(ItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ItemDTO> findById(String id) {
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
    public void deleteById(String id) {
        itemRepository.deleteById(id);
    }

    @Override
    public Page<ItemDTO> findByPage(Pageable pageable) {
        return itemRepository.findItemsWithVariants(pageable)
                .map(ItemDTO::fromEntity);
    }

    @Override
    public List<ItemDTO> findRandomItemsByCategory(int categoryId, int count) {
        return itemRepository.findRandomItemsByCategory(categoryId, count)
                .stream()
                .map(ItemDTO::fromEntity)
                .collect(Collectors.toList());
    }
}