package model;

/**
 * Represents an order in the application.
 * An order has a unique identifier, a client ID, a product ID and a quantity.
 * @author Ruben
 */
public class Order {
    private int id;
    private final int client_id;
    private final int product_id;
    private int quantity;

    public Order(int client_id, int product_id, int quantity) {
        this.client_id = client_id;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getClient_id() {
        return client_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getQuantity() {
        return quantity;
    }
}
