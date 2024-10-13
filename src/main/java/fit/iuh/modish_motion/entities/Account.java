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
    @OneToOne
    @JoinColumn(name = "id")
    private User user;
    private String password;
    @Column(name = "is_admin")
    private boolean isAdmin;
}
