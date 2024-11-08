package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.entities.Size;
import fit.iuh.modish_motion.repositories.SizeRepository;
import fit.iuh.modish_motion.services.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeServiceImpl implements SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public List<Size> findAll() {
        return sizeRepository.findAll();
    }

    @Override
    public Optional<Size> findById(Integer id) {
        return sizeRepository.findById(id);
    }

    @Override
    public Size save(Size size) {
        return sizeRepository.save(size);
    }

    @Override
    public void deleteById(Integer id) {
        sizeRepository.deleteById(id);
    }

    @Override
    public Page<Size> findByPage(Pageable pageable) {
        return sizeRepository.findAll(pageable);
    }
}
