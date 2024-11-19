
package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.entities.Account;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsernameAndPassword(String username, String password);

    Optional<Account> findByUsername(String username);
}