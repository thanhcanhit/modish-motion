package fit.iuh.modish_motion.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "image_url", length = 1500)
    private String imageUrl;
    private double price;
    @ManyToOne(optional = true)
    @JoinColumn(name = "color_id")
    private Color color;
    @ManyToOne
    @JoinColumn(name = "item_id")
    @JsonIgnore
    private Item item;
    @Column(name = "available_quantity")
    private int availableQuantity;
    @Column(name = "name")
    private String name;
    @ManyToOne(optional = true)
    @JoinColumn(name = "size_id")
    private Size size;

}
