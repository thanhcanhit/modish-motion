
package fit.iuh.modish_motion.repositories;

import fit.iuh.modish_motion.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    // Additional query methods can be defined here
}