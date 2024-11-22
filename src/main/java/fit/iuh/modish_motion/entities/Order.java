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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "status")
    private int status;
    @Column(name = "order_at")
    private Date orderAt;
    @Column(name = "cancelled_at")
    private Date cancelledAt;
    @Column(name = "total_due")
    private double totalDue;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;
    @Column(name = "payment_confirm")
    private boolean paymentConfirm;
}
