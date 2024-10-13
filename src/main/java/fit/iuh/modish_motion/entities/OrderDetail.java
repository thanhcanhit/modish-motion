package fit.iuh.modish_motion.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class OrderDetail {
    private int id;
    private int order_id;
    private int variant_id;
    private int quantity;
}
