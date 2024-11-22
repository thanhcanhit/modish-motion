package fit.iuh.modish_motion.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorCode", 500);
        model.addAttribute("errorMessage", "Something went wrong on our end. Please try again later.");
        return "not-found";
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String handle404(Model model) {
        model.addAttribute("errorCode", 404);
        model.addAttribute("errorMessage", "The page you are looking for could not be found.");
        return "not-found";
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public String handle403(Model model) {
        model.addAttribute("errorCode", 403);
        model.addAttribute("errorMessage", "You do not have permission to access this page.");
        return "not-found";
    }
}
