package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.AccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<AccountDTO> findAll();
    Optional<AccountDTO> findById(Integer id);
    AccountDTO save(AccountDTO account);
    void deleteById(Integer id);
    Page<AccountDTO> findByPage(Pageable pageable);
    boolean authenticate(String username, String password);
    int count();
    Optional<AccountDTO> findByUserNameAndPassword(String username, String password);
    Optional<AccountDTO> findByUsername(String username);
    Page<AccountDTO> findByRole(boolean isAdmin, Pageable pageable);
    List<AccountDTO> findByUsernameContain(String keyword);
    void changePassword(AccountDTO accountDTO, String oldPassword, String newPassword);
}