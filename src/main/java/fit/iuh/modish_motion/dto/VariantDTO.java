package fit.iuh.modish_motion.dto;

import fit.iuh.modish_motion.entities.Item;
import fit.iuh.modish_motion.entities.Variant;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VariantDTO {
    private String id;
    private List<String> imageUrls; // Change to List<String> to hold multiple URLs
    private double price;
    private ColorDTO color;
    private String itemId; // Use itemId instead of full ItemDTO to avoid circular reference
    private int availableQuantity;
    private String name;
    private SizeDTO size;

    public static VariantDTO fromEntity(Variant variant) {
        // Split the image URLs if they are stored as a comma-separated string in the entity
        List<String> imagePaths = variant.getImageUrl() != null
                ? Arrays.asList(variant.getImageUrl().split(","))
                : null;

        return new VariantDTO(
                variant.getId(),
                imagePaths,
                variant.getPrice(),
                ColorDTO.fromEntity(variant.getColor()),
                variant.getItem() != null ? variant.getItem().getId() : null, // Only store itemId
                variant.getAvailableQuantity(),
                variant.getName(),
                SizeDTO.fromEntity(variant.getSize())
        );
    }


    public Variant toEntity() {
        Variant variant = new Variant();
        variant.setId(this.id);
        // Join the list of image URLs into a comma-separated string for storage
        variant.setImageUrl(this.imageUrls != null
                ? String.join(",", this.imageUrls)
                : null);
        variant.setPrice(this.price);
        variant.setColor(this.color != null ? this.color.toEntity() : null);
        variant.setItem(new Item(this.itemId)); // Set item entity directly
        variant.setAvailableQuantity(this.availableQuantity);
        variant.setName(this.name);
        variant.setSize(this.size != null ? this.size.toEntity() : null);
        return variant;
    }
}
