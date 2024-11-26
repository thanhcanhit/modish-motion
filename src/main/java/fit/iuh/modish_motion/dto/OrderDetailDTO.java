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
    private int id;
    private Order order;
    private VariantDTO variant;
    private int quantity;

    public static OrderDetailDTO fromEntity(OrderDetail orderDetail) {
        return new OrderDetailDTO(
            orderDetail.getId(),
            orderDetail.getOrder(),
            new VariantDTO().fromEntity(orderDetail.getVariant()),
            orderDetail.getQuantity()
        );
    }

    public OrderDetail toEntity() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(this.id);
        orderDetail.setOrder(this.order);
        orderDetail.setVariant(this.variant.toEntity());
        orderDetail.setQuantity(this.quantity);
        return orderDetail;
    }
}