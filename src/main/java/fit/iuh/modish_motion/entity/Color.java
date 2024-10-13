package fit.iuh.modish_motion.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "colors")
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "color", length = 255)
    private String color;

    // Quan hệ 1-n với Variant
    @OneToMany(mappedBy = "color", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Variant> variants;

    // Constructors, getters và setters
    public Color() {}

    public Color(String color) {
        this.color = color;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
