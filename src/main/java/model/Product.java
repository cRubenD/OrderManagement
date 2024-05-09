package model;

/**
 * Represents a product in the inventory system. A product has an ID, a name, a price and a stock count.
 * @author Ruben
 */
public class Product {
    private int id;
    private String name;
    private float price;
    private int stock = 0;

    public Product(){

    }

    public Product(String name, float price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}
