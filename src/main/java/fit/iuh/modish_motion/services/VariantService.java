package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.VariantDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VariantService {
    List<VariantDTO> findAll();
    Optional<VariantDTO> findById(String id);
    VariantDTO save(VariantDTO variant);
    void deleteById(String id);
    Page<VariantDTO> findByPage(Pageable pageable);
    void updateQuantity(String variantId, int quantityChange);
}