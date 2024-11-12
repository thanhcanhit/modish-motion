package fit.iuh.modish_motion.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/information")
public class InformationController {
    @GetMapping("/about-us")
    public String aboutUs(Model model) {

        return "information/about-us";
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        return "information/blog";
    }

    @GetMapping("/cookies-policy")
    public String cookiesPolicy(Model model) {
        return "information/cookies-policy";
    }

    @GetMapping("/loyalty-customer-policy")
    public String loyalty(Model model) {
        return "information/loyalty-customer-policy";
    }
    @GetMapping("/online-delivery-policy")
    public String onlineDelivery(Model model) {
        return "information/online-delivery-policy";
    }
    @GetMapping("/privacy-policy")
    public String privacy(Model model) {
        return "information/privacy-policy";
    }
    @GetMapping("/return-warranty-policy")
    public String returnWarranty(Model model) {
        return "information/return-warranty-policy";
    }
    @GetMapping("/size-chart")
    public String sizeChart(Model model) {
        return "information/size-chart";
    }
    @GetMapping("/uniform-policy")
    public String uniform(Model model) {
        return "information/uniform-policy";
    }
    @GetMapping("/modish-motion-sale")
    public String modishMotionSale(Model model) {
        return "information/modish-motion-sale";
    }
}
