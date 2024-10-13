package fit.iuh.modish_motion.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Table(name = "variants")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Variant {
    @Id
    private String id;

    @Column(name = "image_url")
    private String imageUrl;
    private double price;
    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @Column(name = "available_quantity")
    private int availableQuantity;
}
