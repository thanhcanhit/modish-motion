package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.ColorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ColorService {
    List<ColorDTO> findAll();
    Optional<ColorDTO> findById(Integer id);
    ColorDTO save(ColorDTO color);
    void deleteById(Integer id);
    Page<ColorDTO> findByPage(Pageable pageable);
}