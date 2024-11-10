package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.entities.Size;
import fit.iuh.modish_motion.repositories.SizeRepository;
import fit.iuh.modish_motion.services.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fit.iuh.modish_motion.dto.SizeDTO;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
public class SizeServiceImpl implements SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public List<SizeDTO> findAll() {
        List<Size> sizes = sizeRepository.findAll();
        return sizes.stream()
                .map(SizeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SizeDTO> findById(Integer id) {
        return sizeRepository.findById(id)
                .map(SizeDTO::fromEntity);
    }

    @Override
    public SizeDTO save(SizeDTO sizeDTO) {
        Size size = sizeDTO.toEntity();
        Size savedSize = sizeRepository.save(size);
        return SizeDTO.fromEntity(savedSize);
    }

    @Override
    public void deleteById(Integer id) {
        sizeRepository.deleteById(id);
    }

    @Override
    public Page<SizeDTO> findByPage(Pageable pageable) {
        return sizeRepository.findAll(pageable)
                .map(SizeDTO::fromEntity);
    }
}
