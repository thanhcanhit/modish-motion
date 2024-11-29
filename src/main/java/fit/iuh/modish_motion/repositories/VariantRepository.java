package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.entities.Item;
import fit.iuh.modish_motion.entities.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantRepository extends JpaRepository<Variant, String> {
    // Additional query methods can be defined here
    @Query(value = "SELECT * FROM variants v WHERE v.item_id = :itemId", nativeQuery = true)
    List<Variant> findByItemId(@Param("itemId") String itemId);
}