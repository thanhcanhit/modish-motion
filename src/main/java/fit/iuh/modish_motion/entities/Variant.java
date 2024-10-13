package fit.iuh.modish_motion.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "variants")
public class Variant {
    @Id
    private String id;
    private String name;
    private String imageUrl;
    private double price;
    @ManyToOne
    @JoinColumn(name = "id")
    private Color color;
    @ManyToOne
    @JoinColumn(name = "id")
    private Item item;;
    private int availble_quantity;

    public Variant(String id) {
        this.id = id;
    }

    public Variant(String id, String name, String imageUrl, double price, Color color, Item item, int availble_quantity) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.color = color;
        this.item = item;
        this.availble_quantity = availble_quantity;
    }

    public Variant() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAvailble_quantity() {
        return availble_quantity;
    }

    public void setAvailble_quantity(int availble_quantity) {
        this.availble_quantity = availble_quantity;
    }

    @Override
    public String toString() {
        return "Variant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", price=" + price +
                ", color=" + color +
                ", item=" + item +
                ", availble_quantity=" + availble_quantity +
                '}';
    }
}
