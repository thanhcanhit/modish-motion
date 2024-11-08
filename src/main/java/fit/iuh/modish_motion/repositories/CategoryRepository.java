
package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Additional query methods can be defined here
}