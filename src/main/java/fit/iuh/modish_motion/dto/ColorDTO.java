package fit.iuh.modish_motion.dto;

import lombok.*;
import fit.iuh.modish_motion.entities.Color;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ColorDTO {
    private Integer id;
    private String color;

    public static ColorDTO fromEntity(Color color) {
        return new ColorDTO(
            color.getId(),
            color.getColor()
        );
    }

    public Color toEntity() {
        Color color = new Color();
        color.setId(this.id);
        color.setColor(this.color);
        return color;
    }
}