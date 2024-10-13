package fit.iuh.modish_motion.entities;

import java.util.Date;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @OneToOne
    private int id;
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String email;
    private boolean gender;
    private String address;
    private Date dob;
}
