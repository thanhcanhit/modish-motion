
package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.entities.Variant;
import fit.iuh.modish_motion.repositories.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VariantService {

    @Autowired
    private VariantRepository variantRepository;

    public List<Variant> findAll() {
        return variantRepository.findAll();
    }

    public Optional<Variant> findById(String id) {
        return variantRepository.findById(id);
    }

    public Variant save(Variant variant) {
        return variantRepository.save(variant);
    }

    public void deleteById(String id) {
        variantRepository.deleteById(id);
    }

    public Page<Variant> findByPage(Pageable pageable) {
        return variantRepository.findAll(pageable);
    }
}