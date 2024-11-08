
package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.entities.Size;
import fit.iuh.modish_motion.repositories.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    public List<Size> findAll() {
        return sizeRepository.findAll();
    }

    public Optional<Size> findById(Integer id) {
        return sizeRepository.findById(id);
    }

    public Size save(Size size) {
        return sizeRepository.save(size);
    }

    public void deleteById(Integer id) {
        sizeRepository.deleteById(id);
    }

    public Page<Size> findByPage(Pageable pageable) {
        return sizeRepository.findAll(pageable);
    }
}