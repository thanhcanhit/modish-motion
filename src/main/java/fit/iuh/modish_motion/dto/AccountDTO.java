package fit.iuh.modish_motion.dto;

import lombok.*;
import fit.iuh.modish_motion.entities.Account;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountDTO {
    private UserDTO user;
    private String username;
    private String password;
    private boolean isAdmin;

    public static AccountDTO fromEntity(Account account) {
        return new AccountDTO(
            UserDTO.fromEntity(account.getUser()),
            account.getUsername(),
            account.getPassword(),
            account.isAdmin()
        );
    }

    public Account toEntity() {
        Account account = new Account();
        account.setUser(this.user.toEntity());
        account.setUsername(this.username);
        account.setPassword(this.password);
        account.setAdmin(this.isAdmin);
        return account;
    }
}