
package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Additional query methods can be defined here
    @Query(value = "SELECT * FROM categories ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Category> findRandomCategories(@Param("count") int count);
}