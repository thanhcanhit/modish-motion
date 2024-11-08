package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> findAll();
    Optional<Account> findById(Integer id);
    Account save(Account account);
    void deleteById(Integer id);
    Page<Account> findByPage(Pageable pageable);
}