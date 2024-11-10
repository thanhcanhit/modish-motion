package fit.iuh.modish_motion.dto;

import lombok.*;
import fit.iuh.modish_motion.entities.Order;
import fit.iuh.modish_motion.entities.OrderDetail;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDTO {
    private int id;
    private int status;
    private Date orderAt;
    private Date cancelledAt;
    private double totalDue;
    private UserDTO customer;
    private boolean paymentConfirm;
    private List<OrderDetailDTO> orderDetails;

    public static OrderDTO fromEntity(Order order, List<OrderDetailDTO> list) {
        return new OrderDTO(
            order.getId(),
            order.getStatus(),
            order.getOrderAt(),
            order.getCancelledAt(),
            order.getTotalDue(),
            UserDTO.fromEntity(order.getCustomer()),
            order.isPaymentConfirm(),
            list
        );
    }

    public Order toEntity() {
        Order order = new Order();
        order.setId(this.id);
        order.setStatus(this.status);
        order.setOrderAt(this.orderAt);
        order.setCancelledAt(this.cancelledAt);
        order.setTotalDue(this.totalDue);
        order.setCustomer(this.customer.toEntity());
        order.setPaymentConfirm(this.paymentConfirm);
        return order;
    }
}