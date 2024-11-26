package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.dto.ItemDTO;
import fit.iuh.modish_motion.dto.VariantDTO;
import fit.iuh.modish_motion.entities.Item;
import fit.iuh.modish_motion.entities.Variant;
import fit.iuh.modish_motion.repositories.VariantRepository;
import fit.iuh.modish_motion.services.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VariantServiceImpl implements VariantService {

    @Autowired
    private VariantRepository variantRepository;

    @Override
    public List<VariantDTO> findAll() {
        List<Variant> variants = variantRepository.findAll();
        return variants.stream()
                .map(VariantDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<VariantDTO> findByItemId(String itemId) {
        List<Variant> variants = variantRepository.findByItemId(itemId);
        return variants.stream()
                .map(VariantDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VariantDTO> findById(String id) {
        return variantRepository.findById(id)
                .map(VariantDTO::fromEntity);
    }

    @Override
    public VariantDTO save(VariantDTO variantDTO) {
        Variant variant = variantDTO.toEntity();
        Variant savedVariant = variantRepository.save(variant);
        return VariantDTO.fromEntity(savedVariant);
    }

    @Override
    public void update(VariantDTO variant) {
        Variant existingVariant = variantRepository.findById(variant.getId())
                .orElseThrow(() -> new RuntimeException("Variant not found"));
        existingVariant.setColor(variant.getColor().toEntity());
        existingVariant.setSize(variant.getSize().toEntity());
        existingVariant.setPrice(variant.getPrice());
        existingVariant.setAvailableQuantity(variant.getAvailableQuantity());
        existingVariant.setImageUrl(variant.toEntity().getImageUrl());
        variantRepository.save(existingVariant);
    }

    @Override
    public void deleteById(String id) {
        variantRepository.deleteById(id);
    }

    @Override
    public Page<VariantDTO> findByPage(Pageable pageable) {
        return variantRepository.findAll(pageable)
                .map(VariantDTO::fromEntity);
    }

    @Override
    @Transactional
    public void updateQuantity(String variantId, int quantityChange) {
        Variant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found"));
                
        int newQuantity = variant.getAvailableQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new RuntimeException("Not enough stock");
        }
        
        variant.setAvailableQuantity(newQuantity);
        variantRepository.save(variant);
    }
}
