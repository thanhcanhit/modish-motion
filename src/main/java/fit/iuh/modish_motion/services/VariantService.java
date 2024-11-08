package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.entities.Variant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VariantService {
    List<Variant> findAll();
    Optional<Variant> findById(String id);
    Variant save(Variant variant);
    void deleteById(String id);
    Page<Variant> findByPage(Pageable pageable);
}