    package fit.iuh.modish_motion.dto;

    import fit.iuh.modish_motion.entities.Variant;
    import lombok.*;
    import fit.iuh.modish_motion.entities.Item;

    import java.util.LinkedHashMap;
    import java.util.List;
    import java.util.Objects;
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
        private List<VariantDTO> displayVariants;

        public static ItemDTO fromEntity(Item item) {
            List<VariantDTO> allVariants = item.getVariants().stream()
                    .filter(Objects::nonNull)
                    .map(VariantDTO::fromEntity)
                    .collect(Collectors.toList());


            List<VariantDTO> uniqueColorVariants = allVariants.stream()
                    .filter(variant -> variant.getColor() != null)
                    .collect(Collectors.toMap(
                            variant -> variant.getColor().getId(),
                            variant -> variant,
                            (existing, replacement) -> existing,
                            LinkedHashMap::new
                    ))
                    .values().stream()
                    .limit(4)
                    .collect(Collectors.toList());

            // Build the ItemDTO object

            return new ItemDTO(
                    item.getId(),
                    item.getName(),
                    item.getCharacteristic(),
                    item.getPromotionPrice(),
                    item.getTags(),
                    CategoryDTO.fromEntity(item.getCategory()),
                    item.getGender(),
                    item.getQuantitySold(),
                    allVariants,
                    uniqueColorVariants
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

            // Map variants without setting the item reference first
            List<Variant> variants = this.variants.stream()
                    .filter(variantDTO -> variantDTO != null)
                    .map(variantDTO -> variantDTO.toEntity()) // Pass null for item to avoid recursion
                    .collect(Collectors.toList());

            // Now set the item reference in each variant
            variants.forEach(variant -> variant.setItem(item));
            item.setVariants(variants);

            return item;
        }

    }
