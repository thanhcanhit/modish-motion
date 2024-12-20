package fit.iuh.modish_motion.entities;

import fit.iuh.modish_motion.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name; 
    @Column(name = "phone_number")
    private String phoneNumber;
    private String email;
    private boolean gender;
    private String address;

    @Temporal(TemporalType.DATE)
    private Date dob;
    @Column(name = "google_id")
    private String googleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private AuthProvider provider;
}