package fit.iuh.modish_motion.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import fit.iuh.modish_motion.dto.UserDTO;
import jakarta.transaction.Transactional;
import org.apache.http.auth.AuthenticationException;

public interface GoogleLoginService {
    UserDTO loginWithGoogle(String idToken) throws AuthenticationException;

    GoogleIdToken.Payload verifyGoogleToken(String idToken) throws Exception;

    @Transactional
    UserDTO createNewGoogleUser(String email, String name, String googleId);

    String generateUniqueUsername(String email);

    String generateRandomPassword();
}
