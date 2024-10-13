package fit.iuh.modish_motion.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @Column(name = "order_id")
    private int order_id;
    @ManyToOne
    @JoinColumn(name = "variant_id")
    private Variant variant;
    @Column(name = "quantity")
    private int quantity;
}
