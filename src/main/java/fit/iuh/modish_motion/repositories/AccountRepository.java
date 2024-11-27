
package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.entities.Account;
import fit.iuh.modish_motion.entities.User;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByUsername(String username);

    Account findByUsernameAndPassword(String username, String password);

    Optional<Account> findByUsername(String username);

    Optional<Account> findByUser(User user);

    Page<Account> findByIsAdmin(boolean isAdmin, Pageable pageable);
    List<Account> findByUsernameContaining(String username);

//    Page<Account> findByUsernameContainingOrId(String username, int id, Pageable pageable);
//
//    Page<Account> findByUsernameContainingAndIsAdmin(boolean isAdmin, String username, Pageable pageable);
}