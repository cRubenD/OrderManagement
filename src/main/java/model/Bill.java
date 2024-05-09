package model;

// records are great for creating immutable data structures. It automatically generates constructors
// getters, and other methods

/**
 * A bill is tied to an order and contains information about the client and the product.
 * @param orderId The ID of the order
 * @param clientName The name of the client that placed the order
 * @param productName The product that the client bought
 * @param quantity The amount of products ordered
 * @param totalPrice The total price of the products
 */
public record Bill(
        int orderId,
        String clientName,
        String productName,
        int quantity,
        float totalPrice
) {
    public int getOrderId() {
        return orderId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }
}
