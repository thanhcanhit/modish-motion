package fit.iuh.modish_motion.dto;

import fit.iuh.modish_motion.enums.AuthProvider;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import fit.iuh.modish_motion.entities.User;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private int id;
    private String name;
    private String phoneNumber;
    private String email;
    private boolean gender;
    private String address;
    private Date dob;
    private String googleId;
    private AuthProvider provider;

    public static UserDTO fromEntity(User user) {
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getPhoneNumber(),
            user.getEmail(),
            user.isGender(),
            user.getAddress(),
            user.getDob(),
            user.getGoogleId(),
            user.getProvider()
        );
    }

    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setName(this.name);
        user.setPhoneNumber(this.phoneNumber);
        user.setEmail(this.email);
        user.setGender(this.gender);
        user.setAddress(this.address);
        user.setDob(this.dob);
        user.setGoogleId(this.googleId);
        user.setProvider(this.provider);
        return user;
    }
}