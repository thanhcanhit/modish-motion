package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.UserAccountDTO;
import fit.iuh.modish_motion.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> findAll();
    Optional<UserDTO> findById(Integer id);
    Optional<UserDTO> findByEmail(String email);
    UserDTO save(UserDTO user);
    void deleteById(Integer id);
    Page<UserDTO> findByPage(Pageable pageable);
    void updateUser(Integer id, UserDTO user);
    String registerUser(UserAccountDTO userDTO);

    void updateAccount(Integer id, UserAccountDTO request);

    void updateAddress(Integer id, String address, String phoneNumber);

}