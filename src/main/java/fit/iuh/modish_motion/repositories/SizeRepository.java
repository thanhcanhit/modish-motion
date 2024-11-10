
package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
    // Additional query methods can be defined here
}