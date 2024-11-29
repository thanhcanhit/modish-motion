package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.services.JwtService;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {
    @Override
    public String generateToken(UserDTO userDTO) {
        return "";
    }
}
