package fit.iuh.modish_motion.servicesImpl;

import fit.iuh.modish_motion.dto.OrderDTO;
import fit.iuh.modish_motion.dto.OrderDetailDTO;
import fit.iuh.modish_motion.dto.ProductStatisticDTO;
import fit.iuh.modish_motion.dto.VariantDTO;
import fit.iuh.modish_motion.services.OrderDetailService;
import fit.iuh.modish_motion.services.OrderService;
import fit.iuh.modish_motion.services.ProductStatisticService;
import fit.iuh.modish_motion.services.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductStatisticServiceImpl implements ProductStatisticService {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final VariantService variantService;

    @Autowired
    public ProductStatisticServiceImpl(OrderService orderService, OrderDetailService orderDetailService,
                                       VariantService variantService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.variantService = variantService;
    }

    @Override
    public List<ProductStatisticDTO> getTopSellingProductsByDate(Date date) {
        // Lấy tất cả các đơn hàng có trạng thái thành công trong ngày
        List<OrderDTO> orders = orderService.findOrdersByDateAndStatus(date); // Giả sử phương thức này lấy các đơn hàng trong ngày có trạng thái thành công

        // Map các sản phẩm (variant) với tổng số lượng bán được
        Map<VariantDTO, Integer> productSales = new HashMap<>();

        // Duyệt qua từng đơn hàng và tính toán tổng số lượng bán ra cho từng sản phẩm
        for (OrderDTO order : orders) {
            // Lấy chi tiết đơn hàng từ OrderDetailService
            List<OrderDetailDTO> orderDetails = orderDetailService.findByOrderId(order.getId());

            for (OrderDetailDTO orderDetail : orderDetails) {
                VariantDTO variant = orderDetail.getVariant();
                int quantity = orderDetail.getQuantity();

                // Cập nhật tổng số lượng bán của mỗi sản phẩm
                productSales.put(variant, productSales.getOrDefault(variant, 0) + quantity);
            }
        }

        // Tạo danh sách các ProductStatisticDTO từ map và tính tổng doanh thu cho mỗi sản phẩm
        List<ProductStatisticDTO> result = productSales.entrySet().stream()
                .map(entry -> {
                    VariantDTO variant = entry.getKey();
                    int totalQuantity = entry.getValue();
                    double pricePerUnit = variant.getPrice();
                    double totalRevenue = totalQuantity * pricePerUnit;
                    return new ProductStatisticDTO(variant.getId(), variant.getName(), pricePerUnit, totalQuantity, totalRevenue);
                })
                .sorted(Comparator.comparingDouble(ProductStatisticDTO::getProductTotalAmount).reversed()) // Sắp xếp theo doanh thu
                .limit(10) // Lấy top 10 sản phẩm bán chạy nhất
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public List<ProductStatisticDTO> getTopSellingProductsByMonth(int year, int month) {
        // Tính toán startDate và endDate của tháng được yêu cầu
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

        // Chuyển đổi sang Date để sử dụng trong câu truy vấn
        Date startDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Lấy danh sách các đơn hàng thành công trong tháng
        List<OrderDTO> orders = orderService.findOrdersByDateAndStatusBetween(startDate, endDate); // Phương thức này sẽ trả về các đơn hàng có trạng thái thành công trong khoảng thời gian từ startDate đến endDate

        // Map các sản phẩm (variant) với tổng số lượng bán được
        Map<VariantDTO, Integer> productSales = new HashMap<>();

        // Duyệt qua từng đơn hàng và tính toán tổng số lượng bán ra cho từng sản phẩm
        for (OrderDTO order : orders) {
            // Lấy chi tiết đơn hàng từ OrderDetailService
            List<OrderDetailDTO> orderDetails = orderDetailService.findByOrderId(order.getId());

            for (OrderDetailDTO orderDetail : orderDetails) {
                VariantDTO variant = orderDetail.getVariant();
                int quantity = orderDetail.getQuantity();

                // Cập nhật tổng số lượng bán của mỗi sản phẩm
                productSales.put(variant, productSales.getOrDefault(variant, 0) + quantity);
            }
        }

        // Tạo danh sách các ProductStatisticDTO từ map và tính tổng doanh thu cho mỗi sản phẩm
        List<ProductStatisticDTO> result = productSales.entrySet().stream()
                .map(entry -> {
                    VariantDTO variant = entry.getKey();
                    int totalQuantity = entry.getValue();
                    double pricePerUnit = variant.getPrice();
                    double totalRevenue = totalQuantity * pricePerUnit;
                    return new ProductStatisticDTO(variant.getId(), variant.getName(), pricePerUnit, totalQuantity, totalRevenue);
                })
                .sorted(Comparator.comparingDouble(ProductStatisticDTO::getProductTotalAmount).reversed()) // Sắp xếp theo doanh thu
                .limit(10) // Lấy top 10 sản phẩm bán chạy nhất
                .collect(Collectors.toList());

        return result;
    }


}
