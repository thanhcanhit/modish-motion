
package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.entities.Color;
import fit.iuh.modish_motion.repositories.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColorService {

    @Autowired
    private ColorRepository colorRepository;

    public List<Color> findAll() {
        return colorRepository.findAll();
    }

    public Optional<Color> findById(Integer id) {
        return colorRepository.findById(id);
    }

    public Color save(Color color) {
        return colorRepository.save(color);
    }

    public void deleteById(Integer id) {
        colorRepository.deleteById(id);
    }

    public Page<Color> findByPage(Pageable pageable) {
        return colorRepository.findAll(pageable);
    }
}