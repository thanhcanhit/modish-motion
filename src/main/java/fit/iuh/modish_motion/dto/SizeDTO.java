package fit.iuh.modish_motion.dto;

import lombok.*;
import fit.iuh.modish_motion.entities.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SizeDTO {
    private Integer id;
    private String size;

    public static SizeDTO fromEntity(Size size) {
        if (size == null) return null;
        return new SizeDTO(
            size.getId(),
            size.getSize()
        );
    }

    public Size toEntity() {
        Size size = new Size();
        size.setId(this.id);
        size.setSize(this.size);
        return size;
    }
}