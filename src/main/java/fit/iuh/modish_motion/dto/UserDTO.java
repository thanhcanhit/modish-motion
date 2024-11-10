package fit.iuh.modish_motion.dto;

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

    public static UserDTO fromEntity(User user) {
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getPhoneNumber(),
            user.getEmail(),
            user.isGender(),
            user.getAddress(),
            user.getDob()
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
        return user;
    }
}