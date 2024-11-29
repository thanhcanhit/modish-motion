package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.dto.UserAccountDTO;
import fit.iuh.modish_motion.entities.Account;
import fit.iuh.modish_motion.entities.User;
import fit.iuh.modish_motion.repositories.AccountRepository;
import fit.iuh.modish_motion.repositories.UserRepository;
import fit.iuh.modish_motion.services.AccountService;
import fit.iuh.modish_motion.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fit.iuh.modish_motion.dto.UserDTO;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> findById(Integer id) {
        return userRepository.findById(id)
                .map(UserDTO::fromEntity);
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        User user = userDTO.toEntity();
        User savedUser = userRepository.save(user);
        return UserDTO.fromEntity(savedUser);
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public Page<UserDTO> findByPage(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserDTO::fromEntity);
    }

    @Override
    public void updateUser(Integer id, UserDTO user) {
        Optional<UserDTO> userDTO = findById(id);
        if (userDTO.isPresent()) {
            userDTO.get().setName(user.getName());
            userDTO.get().setEmail(user.getEmail());
            userDTO.get().setDob(user.getDob());
            userDTO.get().setGender(user.isGender());
            userDTO.get().setAddress(user.getAddress());
            userDTO.get().setPhoneNumber(user.getPhoneNumber());
            userRepository.save(userDTO.get().toEntity());
        }
    }

    @Override
    public String registerUser(UserAccountDTO userAccountDTO) {
        if (accountRepository.existsByUsername(userAccountDTO.getUsername())) {
            return "Username already exists";
        }

        if (userRepository.existsByEmail(userAccountDTO.getEmail())) {
            return "Email already exists";
        }
        User user = new User();
        user.setName(userAccountDTO.getName());
        user.setPhoneNumber(userAccountDTO.getPhoneNumber());
        user.setEmail(userAccountDTO.getEmail());
        user.setGender(userAccountDTO.isGender());
        user.setAddress(userAccountDTO.getAddress());
        user.setDob(userAccountDTO.getDob());
        User savedUser = userRepository.save(user);

        Account account = new Account();
        account.setUser(savedUser);
        account.setUsername(userAccountDTO.getUsername());
        account.setPassword(passwordEncoder.encode(userAccountDTO.getPassword())); // Mã hóa password
        account.setAdmin(false);

        accountRepository.save(account);
        return "Success";
    }

    @Override
    public void updateAccount(Integer id, UserAccountDTO request) {
        // Cập nhật User
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tài khoản không tồn tại"));
        Account account = accountRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException("Tài khoản không tồn tại"));
        user.setName(request.getName());
        account.setUser(user);
        account.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setDob(request.getDob());
        account.setPassword(request.getPassword());
        account.setAdmin(request.isAdmin());
        user.setGender(request.isGender());
        userRepository.save(user);
    }

    @Override
    public void updateAddress(Integer id, String address, String phoneNumber) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tài khoản không tồn tại"));
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
    }
}
