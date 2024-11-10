package fit.iuh.modish_motion.dto;

import lombok.*;
import fit.iuh.modish_motion.entities.Item;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemDTO {
    private String id;
    private String name;
    private String characteristic;
    private double promotionPrice;
    private String tags;
    private CategoryDTO category;
    private String gender;
    private int quantitySold;
    private List<VariantDTO> variants;

    public static ItemDTO fromEntity(Item item) {
        return new ItemDTO(
            item.getId(),
            item.getName(),
            item.getCharacteristic(),
            item.getPromotionPrice(),
            item.getTags(),
            CategoryDTO.fromEntity(item.getCategory()),
            item.getGender(),
            item.getQuantitySold(),
            item.getVariants().stream().map(VariantDTO::fromEntity).collect(Collectors.toList())
        );
    }

    public Item toEntity() {
        Item item = new Item();
        item.setId(this.id);
        item.setName(this.name);
        item.setCharacteristic(this.characteristic);
        item.setPromotionPrice(this.promotionPrice);
        item.setTags(this.tags);
        item.setCategory(this.category.toEntity());
        item.setGender(this.gender);
        item.setQuantitySold(this.quantitySold);
        item.setVariants(this.variants.stream().map(VariantDTO::toEntity).collect(Collectors.toList()));
        return item;
    }
}