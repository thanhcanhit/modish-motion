
package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.entities.Account;
import fit.iuh.modish_motion.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(Integer id) {
        return accountRepository.findById(id);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public void deleteById(Integer id) {
        accountRepository.deleteById(id);
    }

    public Page<Account> findByPage(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }
}