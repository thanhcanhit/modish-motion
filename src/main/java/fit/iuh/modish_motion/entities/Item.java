package fit.iuh.modish_motion.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "items")
public class Item {
    @Id
    private String id;
    private String name;
    @Column(length = 500)
    private String characteristic;
    private double promotionPrice;
    private String tags;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private String gender;
    private int quantitySold;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    private List<Variant> variants;

    public Item(String itemId) {
        this.id = itemId;
    }
}
