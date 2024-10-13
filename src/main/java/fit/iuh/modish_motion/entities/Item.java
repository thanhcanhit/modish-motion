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
    private String characteristic;
    private double promotion_price;
    private String tags;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private String gender;
    private int quantity_sold;

    @OneToMany(mappedBy = "item")
    private List<Variant> variants;
}
