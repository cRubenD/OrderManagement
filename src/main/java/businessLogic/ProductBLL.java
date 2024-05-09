package businessLogic;

import dataAccess.ProductDAO;
import javafx.scene.control.TextField;
import model.Product;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This class provides business logic for managing products in the application.
 * It acts as an intermediary between the user interface and the data access layer,
 * handling CRUD operations and others business-related tasks.
 * @author Ruben
 */
public class ProductBLL {

    /**
     * The data access object used to interact with the underlying database.
     */
    ProductDAO productDAO;

    public ProductBLL(){

    }

    /**
     * Construct a new ProductBLL instance and creates a Product object from a list
     * of textFields, containing the name, price and stock of the new product.
     * @param productField List of TextField objects containing the product info
     */
    public ProductBLL(@NotNull List<TextField> productField){
        String name = (productField.get(0).getText());
        float price = Float.parseFloat((productField.get(1).getText()));
        int stock = Integer.parseInt((productField.get(2).getText()));
        Product product = new Product(name, price, stock);

        insertProduct(product);
    }

    /**
     * Finds all products in the database.
     * @return A list of all Product objects
     */

    public List<Product> findAll(){
        productDAO = new ProductDAO();
        return productDAO.findAll();
    }

    /**
     * Inserts a new product into the database.
     * @param product The Product object to be inserted
     */
    private void insertProduct(Product product){
        productDAO = new ProductDAO();
        productDAO.insert(product);
    }

    /**
     * Deletes a product from the database.
     * @param product The Product to be deleted
     */
    public void deleteProduct(Product product){
        productDAO = new ProductDAO();
        productDAO.delete(product.getId());
    }

    /**
     * Counts the total number of product in the database.
     * @return The total count of products
     */
    public int count(){
        productDAO = new ProductDAO();
        return productDAO.count();
    }

    /**
     * Updates a product's details in the database.
     * @param product The Product object containing updated information
     */
    public void updateProduct(Product product){
        productDAO = new ProductDAO();
        productDAO.update(product, product.getId());
    }

    /**
     * Finds a product by its name in the database.
     * @param name The name of the Product to find
     * @return The Product object with the specified name
     */
    public Product findByName(String name){
        productDAO = new ProductDAO();
        return productDAO.findByName(name);
    }
}
