package fit.iuh.modish_motion.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class UserAccountDTO {
    private String name;
    private String phoneNumber;
    private String email;
    private boolean gender;
    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    // Thuộc tính của Account
    private String username;
    private String password;
    private boolean isAdmin;
}
