package fit.iuh.modish_motion.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage(Model model){
        // Danh sách các ảnh banner
        List<Map<String, String>> banners = List.of(
                Map.of("imageUrl", "/images/home/imageBanner1.png", "link", ""),
                Map.of("imageUrl", "/images/home/imageBanner2.png", "link", ""),
                Map.of("imageUrl", "/images/home/imageBanner3.png", "link", ""),
                Map.of("imageUrl", "/images/home/imageBanner4.jpg", "link", "")
        );
        model.addAttribute("banners", banners);

        return "homeFake";
    }
}
