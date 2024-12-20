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
    private String hex;

    public static ColorDTO fromEntity(Color color) {
        if (color == null) return null;
        return new ColorDTO(
            color.getId(),
            color.getColor(),
                color.getHex()
        );
    }

    public Color toEntity() {
        Color color = new Color();
        color.setId(this.id);
        color.setColor(this.color);
        color.setHex(this.hex);
        return color;
    }
}