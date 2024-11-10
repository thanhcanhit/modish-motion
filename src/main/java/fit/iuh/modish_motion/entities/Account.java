package fit.iuh.modish_motion.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    private Integer id;
    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")  // Use a proper foreign key reference
    private User user;
    private String username;
    private String password;
    @Column(name = "is_admin")
    private boolean isAdmin;
}
