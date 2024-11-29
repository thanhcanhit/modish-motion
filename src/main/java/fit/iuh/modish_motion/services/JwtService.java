package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.UserDTO;

public interface JwtService {
    String generateToken(UserDTO userDTO);
}