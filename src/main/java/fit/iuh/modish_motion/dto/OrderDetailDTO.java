package fit.iuh.modish_motion.dto;

import lombok.*;
import fit.iuh.modish_motion.entities.Order;
import fit.iuh.modish_motion.entities.Variant;
import fit.iuh.modish_motion.entities.OrderDetail;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetailDTO {
    private Integer id;
    private Order order;
    private VariantDTO variant;
    private int quantity;

    public static OrderDetailDTO fromEntity(OrderDetail orderDetail) {
        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setId(orderDetail.getId());
        dto.setOrder(orderDetail.getOrder());
        dto.setVariant( new VariantDTO().fromEntity(orderDetail.getVariant()));
        dto.setQuantity(orderDetail.getQuantity());
        return dto;
    }

    public OrderDetail toEntity() {
        OrderDetail orderDetail = new OrderDetail();
        if (this.id != null) {
            orderDetail.setId(this.id);
        }
        orderDetail.setOrder(this.order);
        orderDetail.setVariant(this.variant.toEntity());
        orderDetail.setQuantity(this.quantity);
        return orderDetail;
    }
}