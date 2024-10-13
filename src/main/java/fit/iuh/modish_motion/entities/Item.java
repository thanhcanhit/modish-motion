package fit.iuh.modish_motion.entities;


public class Item {

    private String id;
    private String name;
    private String characteristic;
    private double promotion_price;
    private String tags;
    private int category_id;
    private String gender;
    private int quantity_sold;

    public Item(String id) {
        this.id = id;
    }

    public Item() {
    }

    public Item(String id, String name, String characteristic, double promotion_price, String tags, int category_id, String gender, int quantity_sold) {
        this.id = id;
        this.name = name;
        this.characteristic = characteristic;
        this.promotion_price = promotion_price;
        this.tags = tags;
        this.category_id = category_id;
        this.gender = gender;
        this.quantity_sold = quantity_sold;
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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
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

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", characteristic='" + characteristic + '\'' +
                ", promotion_price=" + promotion_price +
                ", tags='" + tags + '\'' +
                ", category_id=" + category_id +
                ", gender='" + gender + '\'' +
                ", quantity_sold=" + quantity_sold +
                '}';
    }
}
