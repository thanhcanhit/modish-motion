package fit.iuh.modish_motion.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ErrorController {
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorCode", 500);
        model.addAttribute("errorTitle", "Internal Server Error");
        model.addAttribute("errorMessage", "Something went wrong on our end. Please try again later.");
        return "error-page";
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @GetMapping("/error/404")
    public String handle404(Model model) {
        model.addAttribute("errorCode", 404);
        model.addAttribute("errorTitle", "Page Not Found");
        model.addAttribute("errorMessage", "The page you are looking for could not be found.");
        return "error-page";
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @GetMapping("/error/403")
    public String handle403(Model model) {
        model.addAttribute("errorCode", 403);
        model.addAttribute("errorTitle", "Access Denied");
        model.addAttribute("errorMessage", "You do not have permission to access this page.");
        return "error-page";
    }
}
