package fit.iuh.modish_motion.services.impl;

import fit.iuh.modish_motion.entities.Variant;
import fit.iuh.modish_motion.repositories.VariantRepository;
import fit.iuh.modish_motion.services.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VariantServiceImpl implements VariantService {

    @Autowired
    private VariantRepository variantRepository;

    @Override
    public List<Variant> findAll() {
        return variantRepository.findAll();
    }

    @Override
    public Optional<Variant> findById(String id) {
        return variantRepository.findById(id);
    }

    @Override
    public Variant save(Variant variant) {
        return variantRepository.save(variant);
    }

    @Override
    public void deleteById(String id) {
        variantRepository.deleteById(id);
    }

    @Override
    public Page<Variant> findByPage(Pageable pageable) {
        return variantRepository.findAll(pageable);
    }
}
