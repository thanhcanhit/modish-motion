package fit.iuh.modish_motion.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@
public class Order {
    @Column
    private int id;
    private int status;
    private Date order_at;
    private Date cancelled_at;
    private double total_due;
    private int customer_id;
    private boolean payment_confirm;
}
