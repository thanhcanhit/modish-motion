package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(Integer id);
    User save(User user);
    void deleteById(Integer id);
    Page<User> findByPage(Pageable pageable);
}