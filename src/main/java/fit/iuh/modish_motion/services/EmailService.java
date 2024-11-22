package fit.iuh.modish_motion.services;

import jakarta.mail.MessagingException;
import org.thymeleaf.context.Context;

public interface EmailService {
    /**
     * Send HTML email using a template
     * @param to recipient email address
     * @param subject email subject
     * @param templateName name of the template file
     * @param context Thymeleaf context containing variables for the template
     * @throws MessagingException if there's an error sending the email
     */
    void sendHtmlEmail(String to, String subject, String templateName, Context context) 
            throws MessagingException;
} 