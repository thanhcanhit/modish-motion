package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.entities.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    @Query("SELECT i FROM Item i WHERE size(i.variants) > 0")
    Page<Item> findItemsWithVariants(Pageable pageable);

    List<Item> findByCategoryId(int categoryId);
}