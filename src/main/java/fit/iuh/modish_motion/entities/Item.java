package fit.iuh.modish_motion.entities;


import jakarta.persistence.*;

import java.util.List;

@Entity(name = "items")
public class Item {
    @Id
    private String id;
    private String name;
    private String characteristic;
    private double promotion_price;
    private String tags;
    @ManyToOne
    @JoinColumn(name = "id")
    private Category category;
    private String gender;
    private int quantity_sold;

    @OneToMany(mappedBy = "item")
    private List<Variant> variants;

    public Item() {

    }

    public Item(String id) {
        this.id = id;
    }

    public Item(String id, String name, String characteristic, double promotion_price, String tags, Category category, String gender, int quantity_sold, List<Variant> variants) {

        this.id = id;
        this.name = name;
        this.characteristic = characteristic;
        this.promotion_price = promotion_price;
        this.tags = tags;
        this.category = category;
        this.gender = gender;
        this.quantity_sold = quantity_sold;
        this.variants = variants;
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

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public double getPromotion_price() {
        return promotion_price;
    }

    public void setPromotion_price(double promotion_price) {
        this.promotion_price = promotion_price;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getQuantity_sold() {
        return quantity_sold;
    }

    public void setQuantity_sold(int quantity_sold) {
        this.quantity_sold = quantity_sold;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", characteristic='" + characteristic + '\'' +
                ", promotion_price=" + promotion_price +
                ", tags='" + tags + '\'' +
                ", category=" + category +
                ", gender='" + gender + '\'' +
                ", quantity_sold=" + quantity_sold +
                ", variants=" + variants +
                '}';
    }
}
