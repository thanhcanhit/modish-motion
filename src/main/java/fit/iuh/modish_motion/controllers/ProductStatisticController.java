package fit.iuh.modish_motion.controllers;

import fit.iuh.modish_motion.dto.ProductStatisticDTO;
import fit.iuh.modish_motion.services.ProductStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/dashboard/dashboard-statistics")
public class ProductStatisticController {

    @Autowired
    private ProductStatisticService productStatisticService;

    @GetMapping
    public String getTopSellingProducts(@RequestParam(required = false, defaultValue = "2024/11/20") String date,
                                        @RequestParam(required = false) Integer year,
                                        @RequestParam(required = false) Integer month,
                                        Model model) {
        List<ProductStatisticDTO> topSellingProductsByDate = null;
        List<ProductStatisticDTO> topSellingProductsByMonth = null;
        Date parsedDate = null;

        // Sử dụng SimpleDateFormat để phân tích chuỗi ngày theo định dạng yyyy/MM/dd
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); // Cập nhật định dạng ngày ở đây
        try {
            if (date != null) {
                parsedDate = formatter.parse(date); // Parse ngày theo định dạng mới
            }
        } catch (ParseException e) {
            // Xử lý lỗi nếu ngày không hợp lệ
            e.printStackTrace();
            // Bạn có thể thêm logic xử lý lỗi như trả về thông báo lỗi hoặc mặc định giá trị ngày
            parsedDate = new Date(); // Nếu lỗi, sử dụng ngày hiện tại làm mặc định
        }

        // Nếu ngày được cung cấp, lấy năm và tháng từ ngày đó
        if (parsedDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);

            // Nếu không có giá trị year hoặc month trong request, sẽ tự động lấy giá trị từ ngày
            year = year != null ? year : calendar.get(Calendar.YEAR);
            month = month != null ? month : calendar.get(Calendar.MONTH) + 1;
        }

        // Lấy sản phẩm bán chạy theo ngày
        if (parsedDate != null) {
            topSellingProductsByDate = productStatisticService.getTopSellingProductsByDate(parsedDate);
        }

        // Lấy sản phẩm bán chạy theo tháng
        if (year != null && month != null) {
            topSellingProductsByMonth = productStatisticService.getTopSellingProductsByMonth(year, month);
        }

        // Đưa dữ liệu vào model
        model.addAttribute("topSellingProductsByDate", topSellingProductsByDate);
        model.addAttribute("topSellingProductsByMonth", topSellingProductsByMonth);
        model.addAttribute("date", date);
        model.addAttribute("year", year);
        model.addAttribute("month", month);

        return "dashboard-statistics";  // Trả về view name
    }
}
