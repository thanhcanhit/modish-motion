package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.AccountDTO;
import org.springframework.data.domain.Page;
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

    Optional<AccountDTO> findByUserNameAndPassword(String username, String password);

    Optional<AccountDTO> getAccountWithUserDetails(Integer accountId);
}