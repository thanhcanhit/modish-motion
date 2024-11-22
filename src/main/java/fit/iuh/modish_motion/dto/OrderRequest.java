package fit.iuh.modish_motion.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private CustomerInfo customerInfo;
    private List<OrderItem> items;
    private String paymentMethod;
    private double shippingFee;

    @Data
    public static class CustomerInfo {
        private String name;
        private String phone;
        private String email;
        private String address;
        private String note;
    }

    @Data
    public static class OrderItem {
        private VariantInfo variant;
        private int quantity;
    }

    @Data
    public static class VariantInfo {
        private Long id;
        private String name;
        private double price;
        private ColorInfo color;
        private SizeInfo size;
        private List<String> imageUrls;
        private Long itemId;
    }

    @Data
    public static class ColorInfo {
        private String color;
        private String hex;
    }

    @Data
    public static class SizeInfo {
        private String size;
    }
} 