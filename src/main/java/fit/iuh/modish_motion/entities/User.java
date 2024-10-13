package fit.iuh.modish_motion.entities;

import java.util.Date;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class User {
    private int id;
    private String name;
    private String phone_number;
    private String email;
    private boolean gender;
    private String address;
    private Date dob;
}
