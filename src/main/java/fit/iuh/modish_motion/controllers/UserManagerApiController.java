package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.AccountDTO;
import fit.iuh.modish_motion.dto.UserAccountDTO;
import fit.iuh.modish_motion.services.AccountService;
import fit.iuh.modish_motion.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    // API lấy thông tin tài khoản theo ID
    @GetMapping("/account/{id}")
    public ResponseEntity<AccountDTO> getAccountDetails(@PathVariable Integer id) {
        Optional<AccountDTO> account = accountService.findById(id);
        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            accountService.deleteById(id);
            userService.deleteById(id);
            return ResponseEntity.ok("Xóa thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể xóa tài khoản");
        }
    }
    @PutMapping("/account/{id}")
    public ResponseEntity<String> updateAccount(@PathVariable Integer id, @RequestBody UserAccountDTO request) {
        try {
            userService.updateAccount(id, request);
            return ResponseEntity.ok("Cập nhật thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể cập nhật tài khoản");
        }
    }
}
