package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.entities.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

    // Additional query methods can be defined here
    @Query(value = "SELECT * FROM items i WHERE i.category_id = :categoryId AND EXISTS (SELECT 1 FROM variants v WHERE v.item_id = i.id) ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Item> findRandomItemsByCategory(@Param("categoryId") int categoryId, @Param("count") int count);

    @Query("SELECT i FROM Item i WHERE size(i.variants) > 0")
    Page<Item> findItemsWithVariants(Pageable pageable);

    List<Item> findByCategoryId(int categoryId);
}