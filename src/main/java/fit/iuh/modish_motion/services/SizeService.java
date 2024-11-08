package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.SizeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SizeService {
    List<SizeDTO> findAll();
    Optional<SizeDTO> findById(Integer id);
    SizeDTO save(SizeDTO size);
    void deleteById(Integer id);
    Page<SizeDTO> findByPage(Pageable pageable);
}