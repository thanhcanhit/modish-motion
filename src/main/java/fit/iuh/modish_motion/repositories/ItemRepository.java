
package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    // Additional query methods can be defined here
}