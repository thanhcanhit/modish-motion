package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.entities.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SizeService {
    List<Size> findAll();
    Optional<Size> findById(Integer id);
    Size save(Size size);
    void deleteById(Integer id);
    Page<Size> findByPage(Pageable pageable);
}