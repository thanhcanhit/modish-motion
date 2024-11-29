package fit.iuh.modish_motion.servicesImpl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import fit.iuh.modish_motion.configs.PasswordEncoder;
import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.dto.UserDTO;
import fit.iuh.modish_motion.entities.Account;
import fit.iuh.modish_motion.exception.AuthenticationException;
import fit.iuh.modish_motion.entities.User;
import fit.iuh.modish_motion.enums.AuthProvider;
import fit.iuh.modish_motion.repositories.AccountRepository;
import fit.iuh.modish_motion.repositories.UserRepository;
import fit.iuh.modish_motion.services.AccountService;
import fit.iuh.modish_motion.services.GoogleLoginService;
import fit.iuh.modish_motion.services.UserService;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class GoogleLoginServiceImpl implements GoogleLoginService {
    @org.springframework.beans.factory.annotation.Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Override
    @Transactional(readOnly = true)
    public UserDTO loginWithGoogle(String idToken) throws AuthenticationException {
        try {
            log.info("Attempting to verify Google token");
            GoogleIdToken.Payload payload = verifyGoogleToken(idToken);
            if (payload == null) {
                log.error("Token verification failed - payload is null");
                throw new AuthenticationException("Invalid Google ID token");
            }
            log.info("Token verified successfully");

            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String googleId = payload.getSubject();

            log.info("User info from Google - Email: {}, Name: {}", email, name);

            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                log.info("Existing user found with email: {}", email);
                return UserDTO.fromEntity(existingUser.get());
            } else {
                log.info("Creating new user for email: {}", email);
                return createNewGoogleUser(email, name, googleId);
            }
        } catch (Exception e) {
            log.error("Google authentication failed", e);
            throw new AuthenticationException("Google authentication failed: " + e.getMessage());
        }
    }

    @Override
    public GoogleIdToken.Payload verifyGoogleToken(String idToken) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                JSON_FACTORY)  // Sử dụng JSON_FACTORY đã định nghĩa
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);
        return googleIdToken != null ? googleIdToken.getPayload() : null;
    }

    @Transactional
    @Override
    public UserDTO createNewGoogleUser(String email, String name, String googleId) {
        try {
            log.info("Starting to create new Google user for email: {}", email);

            // Create and save User first
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setGoogleId(googleId);
            user.setProvider(AuthProvider.GOOGLE);
            // Thêm các giá trị mặc định
            user.setGender(false);
            user = userRepository.save(user);
            log.info("User saved successfully with ID: {}", user.getId());

            // Create Account
            Account account = new Account();
            account.setUser(user);
            account.setUsername(generateUniqueUsername(email));
            String randomPassword = generateRandomPassword();
            account.setPassword(passwordEncoder.encode(randomPassword));
            account.setAdmin(false);

            // Save both entities
            account = accountRepository.save(account);
            log.info("Account saved successfully for user ID: {}", account.getId());

            return UserDTO.fromEntity(user);
        } catch (Exception e) {
            log.error("Error in createNewGoogleUser: ", e);
            throw new RuntimeException("Failed to create new Google user", e);
        }
    }

    @Override
    public String generateUniqueUsername(String email) {
        String baseUsername = email.split("@")[0];
        String username = baseUsername;
        int counter = 1;

        while (accountService.findByUsername(username).isPresent()) {
            username = baseUsername + counter++;
        }

        return username;
    }

    @Override
    public String generateRandomPassword() {
        return UUID.randomUUID().toString();
    }
}
