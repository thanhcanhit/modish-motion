package fit.iuh.modish_motion.entities;

public class Variant {

    private String id;
    private String name;
    private String imageUrl;
    private double price;
    private int color_id;
    private int product_id;;
    private int availble_quantity;

    public Variant(String id, String name, String imageUrl, double price, int color_id, int product_id, int availble_quantity) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.color_id = color_id;
        this.product_id = product_id;
        this.availble_quantity = availble_quantity;
    }

    public Variant() {
    }

    public Variant(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Variant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", price=" + price +
                ", color_id=" + color_id +
                ", product_id=" + product_id +
                ", availble_quantity=" + availble_quantity +
                '}';
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

    public int getColor_id() {
        return color_id;
    }

    public void setColor_id(int color_id) {
        this.color_id = color_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getAvailble_quantity() {
        return availble_quantity;
    }

    public void setAvailble_quantity(int availble_quantity) {
        this.availble_quantity = availble_quantity;
    }
}
