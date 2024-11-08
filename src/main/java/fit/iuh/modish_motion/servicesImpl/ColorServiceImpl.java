
package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.entities.Color;
import fit.iuh.modish_motion.services.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import fit.iuh.modish_motion.repositories.ColorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ColorServiceImpl implements ColorService {

    @Autowired
    private ColorRepository colorRepository;

    @Override
    public List<Color> findAll() {
        return colorRepository.findAll();
    }

    @Override
    public Optional<Color> findById(Integer id) {
        return colorRepository.findById(id);
    }

    @Override
    public Color save(Color color) {
        return colorRepository.save(color);
    }

    @Override
    public void deleteById(Integer id) {
        colorRepository.deleteById(id);
    }

    @Override
    public Page<Color> findByPage(Pageable pageable) {
        return colorRepository.findAll(pageable);
    }
}