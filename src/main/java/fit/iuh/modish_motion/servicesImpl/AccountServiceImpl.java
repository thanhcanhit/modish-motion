package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.configs.PasswordEncoder;
import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.entities.Account;
import fit.iuh.modish_motion.repositories.UserRepository;
import fit.iuh.modish_motion.services.AccountService;
import fit.iuh.modish_motion.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Override
    public int count() {
        return (int) accountRepository.count();
    }

    @Override
    public Optional<AccountDTO> findByUserNameAndPassword(String username, String password) {
        Optional<Account> account = accountRepository.findByUsername(username);

        if (account.isPresent()) {
            Account acc = account.get();
            if (password.isEmpty() || acc.getPassword().equals(password)) { // Chấp nhận không kiểm tra password
                System.out.println("Account found: " + acc);
                return account.map(AccountDTO::fromEntity);
            } else {
                System.out.println("Password does not match");
            }
        } else {
            System.out.println("Account not found with username: " + username);
        }
        return Optional.empty();
    }


    @Override
    public Optional<AccountDTO> findByUsername(String username) {
        return accountRepository.findByUsername(username)
                .map(account -> AccountDTO.fromEntity(account));
    }

    @Override
    public Page<AccountDTO> findByRole(boolean isAdmin, Pageable pageable) {
        return accountRepository.findByIsAdmin(isAdmin, pageable)
                .map(account -> AccountDTO.fromEntity(account));
    }

    @Override
    public List<AccountDTO> findByUsernameContain(String keyword) {
        return accountRepository.findByUsernameContaining(keyword)
                .stream()
                .map(account -> AccountDTO.fromEntity(account))
                .collect(Collectors.toList());
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

    @Override
    public boolean authenticate(String username, String password) {
        Account account = accountRepository.findByUsernameAndPassword(username, password);
        if (account != null) {
            System.out.println("User authenticated successfully");
            return true;
        } else {
            System.out.println("Authentication failed");
            return false;
        }
    }

    public void changePassword(AccountDTO accountDTO, String oldPassword, String newPassword) {
        Account account = accountRepository.findById(accountDTO.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }
}