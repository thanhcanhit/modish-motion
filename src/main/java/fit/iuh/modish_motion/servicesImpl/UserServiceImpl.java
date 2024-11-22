package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.entities.User;
import fit.iuh.modish_motion.repositories.UserRepository;
import fit.iuh.modish_motion.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fit.iuh.modish_motion.dto.UserDTO;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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

}
