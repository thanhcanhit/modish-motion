
package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

}