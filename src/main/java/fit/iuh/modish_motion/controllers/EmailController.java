package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.servicesImpl.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-register-success")
    public ResponseEntity<?> sendRegisterSuccessEmail(@RequestParam String to, 
                                                    @RequestParam String userName) {
        try {
            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("userEmail", to);

            emailService.sendHtmlEmail(
                to,
                "Chào mừng đến với Modish Motion",
                "email/register-success",
                context
            );
            
            return ResponseEntity.ok("Email sent successfully");
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError().body("Failed to send email: " + e.getMessage());
        }
    }

    @PostMapping("/send-order-success")
    public ResponseEntity<?> sendOrderSuccessEmail(@RequestParam String to,
                                                 @RequestParam String customerName,
                                                 @RequestParam String orderId,
                                                 @RequestParam String shippingAddress,
                                                 @RequestParam String phoneNumber,
                                                 @RequestParam Double totalAmount) {
        try {
            Context context = new Context();
            context.setVariable("customerName", customerName);
            context.setVariable("orderId", orderId);
            context.setVariable("shippingAddress", shippingAddress);
            context.setVariable("phoneNumber", phoneNumber);
            context.setVariable("totalAmount", totalAmount);

            emailService.sendHtmlEmail(
                to,
                "Xác nhận đơn hàng - Modish Motion",
                "email/order-success",
                context
            );
            
            return ResponseEntity.ok("Email sent successfully");
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError().body("Failed to send email: " + e.getMessage());
        }
    }

    @GetMapping("/test-send")
    public ResponseEntity<?> testSendEmail(
            @RequestParam String toEmail,
            @RequestParam(defaultValue = "Test Email") String subject,
            @RequestParam(defaultValue = "Test User") String userName) {
        try {
            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("userEmail", toEmail);

            emailService.sendHtmlEmail(
                toEmail,
                subject,
                "email/register-success",
                context
            );
            
            return ResponseEntity.ok(String.format("Email sent successfully to %s", toEmail));
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError()
                .body("Failed to send email: " + e.getMessage());
        }
    }
} 