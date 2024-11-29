package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.AuthResponseDTO;
import fit.iuh.modish_motion.dto.TokenDTO;
import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.services.GoogleLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import fit.iuh.modish_motion.dto.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fit.iuh.modish_motion.exception.AuthenticationException;
import fit.iuh.modish_motion.services.JwtService;


@RestController
@RequestMapping("/api/auth")
@Slf4j
public class GoogleAuthController {

    @Autowired
    private GoogleLoginService googleLoginService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/google")
    public ResponseEntity<?> authenticateGoogle(@RequestBody TokenDTO tokenDTO) {
        try {
            log.info("Received Google authentication request");
            if (tokenDTO == null || tokenDTO.getToken() == null) {
                log.error("Token is null");
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Invalid request", "Token is required"));
            }

            UserDTO userDTO = googleLoginService.loginWithGoogle(tokenDTO.getToken());
            log.info("Google login successful for user: {}", userDTO.getEmail());

            String jwtToken = jwtService.generateToken(userDTO);
            log.info("JWT token generated successfully");

            return ResponseEntity.ok(new AuthResponseDTO(jwtToken));

        } catch (Exception e) {
            log.error("Error during Google authentication", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Authentication failed", e.getMessage()));
        }
    }
}