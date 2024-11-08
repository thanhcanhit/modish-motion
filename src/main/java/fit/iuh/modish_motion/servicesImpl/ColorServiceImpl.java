package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.entities.Color;
import fit.iuh.modish_motion.services.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import fit.iuh.modish_motion.repositories.ColorRepository;
import fit.iuh.modish_motion.dto.ColorDTO;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
public class ColorServiceImpl implements ColorService {

    @Autowired
    private ColorRepository colorRepository;

    @Override
    public List<ColorDTO> findAll() {
        List<Color> colors = colorRepository.findAll();
        return colors.stream()
                .map(ColorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ColorDTO> findById(Integer id) {
        return colorRepository.findById(id)
                .map(ColorDTO::fromEntity);
    }

    @Override
    public ColorDTO save(ColorDTO colorDTO) {
        Color color = colorDTO.toEntity();
        Color savedColor = colorRepository.save(color);
        return ColorDTO.fromEntity(savedColor);
    }

    @Override
    public void deleteById(Integer id) {
        colorRepository.deleteById(id);
    }

    @Override
    public Page<ColorDTO> findByPage(Pageable pageable) {
        return colorRepository.findAll(pageable)
                .map(ColorDTO::fromEntity);
    }
}