package presentationLayer;

import businessLogic.ProductBLL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the product-related CRUD operations in the application.
 * Additionally, it manages the navigation between different views.
 * @author Ruben
 */

public class ProductController {
    @FXML
    public ImageView closeBtn;
    @FXML
    public ImageView homeImg;
    @FXML
    public ImageView clientImg;
    @FXML
    public ImageView orderImg;
    @FXML
    public TextField productName;
    @FXML
    public TextField price;
    @FXML
    public TextField stock;
    @FXML
    public Button addProductBtn;
    @FXML
    public Button updateProduct;
    @FXML
    public Button deleteProduct;
    @FXML
    public TableView productTable;
    @FXML
    public TableColumn productId;
    @FXML
    public TableColumn productNameC;
    @FXML
    public TableColumn productPrice;
    @FXML
    public TableColumn productStock;

    private Stage mainStage;

    private final ObservableList<Product> productData = FXCollections.observableArrayList();

    public ProductController(){
        // Default constructor needed for FXMLoader
    }

    /**
     * This method sets up the table columnsm updates the product table,
     * and configures listeners for adding, deleting and updating products.
     * @param stage The current window of the application
     */

    public void init(Stage stage) {

        this.mainStage = stage;

        setupTableColumns();
        updateProductTable();

        // close button to close the application
        closeBtn.setOnMouseClicked(mouseEvent -> {
            mainStage.close();
        });

        homeImg.setOnMouseClicked(mouseEvent -> {
            WindowController windowController = new WindowController();
            windowController.homeView(mainStage);
        });

        clientImg.setOnMouseClicked(mouseEvent -> {
            ClientController clientController = new ClientController();
            clientController.clientView(mainStage);
        });

        orderImg.setOnMouseClicked(mouseEvent -> {
            OrderController orderController = new OrderController();
            orderController.orderView(mainStage);
        });

        assert addProductBtn != null : "addProductBtn este null";
        addProductBtn.setOnMouseClicked(mouseEvent -> {
            ProductBLL productBLL = new ProductBLL(getProduct());
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Product successfully added.", ButtonType.OK);
            successAlert.show();
            updateProductTable();
        });

        deleteProduct.setOnMouseClicked(mouseEvent -> {
            // retrieve the selected product from the table
            Product selectedProduct = (Product) productTable.getSelectionModel().getSelectedItem();

            if (selectedProduct == null) {

                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a product to delete.", ButtonType.OK);
                alert.show();
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?", ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait();

            if (confirmAlert.getResult() == ButtonType.YES) {

                ProductBLL productBLL = new ProductBLL();
                productBLL.deleteProduct(selectedProduct);
                updateProductTable();
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Product deleted successfully.", ButtonType.OK);
                successAlert.show();
            }
        });

        updateProduct.setOnMouseClicked(mouseEvent -> {
            Product selectedProduct = (Product) productTable.getSelectionModel().getSelectedItem();

            if (selectedProduct == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a client to update.", ButtonType.OK);
                alert.show();
                return;
            }

            boolean updated = false;

            if (!productName.getText().isEmpty()) {
                selectedProduct.setName(productName.getText());
                updated = true;
            }

            if (!price.getText().isEmpty()) {
                selectedProduct.setPrice(Float.parseFloat(price.getText()));
                updated = true;
            }

            if (!stock.getText().isEmpty()) {
                selectedProduct.setStock(Integer.parseInt(stock.getText()));
                updated = true;
            }

            if (updated) {
                ProductBLL productBLL = new ProductBLL();
                productBLL.updateProduct(selectedProduct);

                updateProductTable();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Product updated successfully.", ButtonType.OK);
                successAlert.show();
            } else {
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "No changes made.", ButtonType.OK);
                infoAlert.show();
            }
        });

    }

    /**
     * Configure the table columns with the appropiate property value factories
     */
    private void setupTableColumns(){
        productId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        productNameC.setCellValueFactory(new PropertyValueFactory<>("Name"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        productStock.setCellValueFactory(new PropertyValueFactory<>("Stock"));
    }

    /**\
     * Navigates to the product-view window
     */
    public void productView(Stage mainStage){
        try {
            FXMLLoader loader = new FXMLLoader(ProductController.class.getResource("product-view.fxml"));
            Parent root = loader.load();
            ProductController productController = loader.getController();
            productController.init(mainStage);

            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the table with the current list of products from the database
     */
    private void updateProductTable(){
        ProductBLL productBLL = new ProductBLL();
        List<Product> products = productBLL.findAll();

        productData.setAll(products);
        productTable.setItems(productData);
    }

    /**
     * Retrives the product data from the text fields.
     * @return A list of text fields containing the name, price and stock information
     */
    private List<TextField> getProduct(){
        List<TextField> list = new ArrayList<>();
        list.add(productName);
        list.add(price);
        list.add(stock);
        return list;
    }
}

