package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.entities.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ColorService {
    List<Color> findAll();
    Optional<Color> findById(Integer id);
    Color save(Color color);
    void deleteById(Integer id);
    Page<Color> findByPage(Pageable pageable);
}