package fit.iuh.modish_motion.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductStatisticDTO {
    private String productCode;
    private String productName;
    private double productPrice;
    private int productQuantity;
    private double productTotalAmount;
}
