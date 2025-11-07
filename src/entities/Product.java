package entities;

public class Product {
    private Integer code;
    private String name;
    private Integer quantity;
    private Double price;

    public Product(int code, String name, int quantity, double price) {
        this.code = code;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Code: ").append(this.code);
        sb.append(", Name: ").append(this.name);
        sb.append(", Quantity: ").append(this.quantity);
        sb.append(", Price: R$ ").append(String.format("%.2f", this.price));
        return sb.toString();
    }
}
