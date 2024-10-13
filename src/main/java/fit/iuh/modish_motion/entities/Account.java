package fit.iuh.modish_motion.entities;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Account {
    private int user_id;
    private String username;
    private String password;
    private boolean isAdmin;
}
