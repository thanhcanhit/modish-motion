package fit.iuh.modish_motion.dto;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private VariantDTO variant;
    private int quantity;
    private double subtotal;

    public void updateSubtotal() {
        this.subtotal = this.variant.getPrice() * this.quantity;
    }
} 