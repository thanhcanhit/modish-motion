package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.entities.Account;
import fit.iuh.modish_motion.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import fit.iuh.modish_motion.repositories.AccountRepository;
import fit.iuh.modish_motion.dto.AccountDTO;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<AccountDTO> findAll() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(account -> AccountDTO.fromEntity(account))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AccountDTO> findById(Integer id) {
        return accountRepository.findById(id)
                .map(account -> AccountDTO.fromEntity(account));
    }

    public boolean authenticate(String username, String password) {
        // Tìm người dùng trong cơ sở dữ liệu theo username
        Account account = accountRepository.findByUsername(username);

        // Kiểm tra nếu người dùng không tồn tại
        if (account == null) {
            return false;
        }
        if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    @Override
    public AccountDTO save(AccountDTO accountDTO) {
        Account account = accountDTO.toEntity();
        Account savedAccount = accountRepository.save(account);
        return AccountDTO.fromEntity(savedAccount);
    }

    @Override
    public void deleteById(Integer id) {
        accountRepository.deleteById(id);
    }

    @Override
    public Page<AccountDTO> findByPage(Pageable pageable) {
        return accountRepository.findAll(pageable)
                .map(account -> AccountDTO.fromEntity(account));
    }
}