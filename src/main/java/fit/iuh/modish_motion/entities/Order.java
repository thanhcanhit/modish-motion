package fit.iuh.modish_motion.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Column(name = "id")
    @Id
    private int id;
    @Column(name = "status")
    private int status;
    @Column(name = "order_at")
    private Date order_at;
    @Column(name = "cancelled_at")
    private Date cancelled_at;
    @Column(name = "total_due")
    private double total_due;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;
    @Column(name = "payment_confirm")
    private boolean payment_confirm;
}
