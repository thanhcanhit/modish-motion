package fit.iuh.modish_motion.dto;

import fit.iuh.modish_motion.entities.Variant;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VariantDTO {
    private String id;
    private String imageUrl;
    private double price;
    private ColorDTO color;
    private ItemDTO item;
    private int availableQuantity;
    private String name;
    private SizeDTO size;

    public static VariantDTO fromEntity(Variant variant) {
        return new VariantDTO(
                variant.getId(),
                variant.getImageUrl(),
                variant.getPrice(),
                ColorDTO.fromEntity(variant.getColor()),
                ItemDTO.fromEntity(variant.getItem()),
                variant.getAvailableQuantity(),
                variant.getName(),
                SizeDTO.fromEntity(variant.getSize())
        );
    }

    public Variant toEntity() {
        Variant variant = new Variant();
        variant.setId(this.id);
        variant.setImageUrl(this.imageUrl);
        variant.setPrice(this.price);
        variant.setColor(this.color.toEntity());
        variant.setItem(this.item.toEntity());
        variant.setAvailableQuantity(this.availableQuantity);
        variant.setName(this.name);
        variant.setSize(this.size.toEntity());
        return variant;
    }
}