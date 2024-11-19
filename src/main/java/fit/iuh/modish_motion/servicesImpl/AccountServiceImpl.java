package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.dto.UserDTO;
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



//    @Override
//    public Optional<AccountDTO> findByUserNameAndPassword(String username, String password) {
//        Account account = accountRepository.findByUsernameAndPassword(username, password);
//        if (account == null) {
//            return Optional.empty();
//        }
//        return Optional.of(AccountDTO.fromEntity(account));
//    }
@Override
public Optional<AccountDTO> findByUserNameAndPassword(String username, String password) {
    System.out.println("Finding account by username: " + username + " and password: " + password);
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

//    @Override
//    public Optional<Account> findByUsernameAndPassword(String username, String password) {
//        Optional<AccountDTO> optionalAccount = accountRepository.findByUsername(username);
//
//        // Kiểm tra mật khẩu nếu tài khoản tồn tại
//        if (optionalAccount.isPresent()) {
//            Account account = optionalAccount.get().toEntity();
//            if (account.getPassword().equals(password)) { // So sánh mật khẩu
//                return Optional.of(account);
//            }
//        }
//
//        return Optional.empty();

//    }
}