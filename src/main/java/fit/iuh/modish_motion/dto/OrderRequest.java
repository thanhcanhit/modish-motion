package fit.iuh.modish_motion.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private List<OrderItemRequest> items;
    private String paymentMethod;
    private double shippingFee;

    @Data
    public static class OrderItemRequest {
        private VariantDTO variant;
        private int quantity;
    }
} 