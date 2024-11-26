package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.entities.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepository extends JpaRepository<Variant, String> {
    // Additional query methods can be defined here
}