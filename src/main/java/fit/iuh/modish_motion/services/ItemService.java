package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.entities.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> findAll();
    Optional<Item> findById(String id);
    Item save(Item item);
    void deleteById(String id);
    Page<Item> findByPage(Pageable pageable);
}