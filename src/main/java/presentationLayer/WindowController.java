package presentationLayer;

import businessLogic.ClientBLL;
import businessLogic.OrderBLL;
import businessLogic.ProductBLL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class represents a controller for the main window of the application in JavaFX.
 * It handles navigation between the various views.
 * It also updates summary information such as total clients, products and orders.
 * @author Ruben
 */

public class WindowController {

    @FXML
    public ImageView closeBtn;
    @FXML
    public ImageView homeImg;
    @FXML
    public ImageView clientImg;
    @FXML
    public ImageView productImg;
    @FXML
    public ImageView orderImg;
    @FXML
    public TextField totalClients;
    @FXML
    public TextField totalProducts;
    @FXML
    public TextField totalOrders;

    private Stage mainStage;

    /**
     * Default constructor for the WindowController, essential for FXML loading.
     */
    public WindowController(){
    }

    /**
     * Initialize the controller with the provided Stage.
     * This method also sets up different listeners and makes textFields non-editable.
     * @param stage The primary stage for the application
     */

    public void init(Stage stage){

        this.mainStage = stage;

        totalClients.setEditable(false);
        updateClients();
        totalProducts.setEditable(false);
        updateProducts();
        totalOrders.setEditable(false);
        updateOrders();

        setupEventListeneres();
    }

    /**
     * Sets up event listeners for various UI components, such as navigation icons and close button.
     */
    private void setupEventListeneres(){
        closeBtn.setOnMouseClicked(mouseEvent -> {
            mainStage.close();
        });

        homeImg.setOnMouseClicked(mouseEvent -> {
            homeView(mainStage);
        });

        clientImg.setOnMouseClicked(mouseEvent -> {
            ClientController clientController = new ClientController();
            clientController.clientView(mainStage);
        });

        productImg.setOnMouseClicked(mouseEvent -> {
            ProductController productController = new ProductController();
            productController.productView(mainStage);
        });

        orderImg.setOnMouseClicked(mouseEvent -> {
            OrderController orderController = new OrderController();
            orderController.orderView(mainStage);
        });
    }

    /**
     * These methods retrieve the total client/product/order count from the business logic layer and
     * sets the corresponding text field.
     */
    private void updateClients(){
        ClientBLL clientBLL = new ClientBLL();
        int total = clientBLL.count();
        totalClients.setText(String.valueOf(total));
    }

    private void updateProducts(){
        ProductBLL productBLL = new ProductBLL();
        int total = productBLL.count();
        totalProducts.setText(String.valueOf(total));
    }

    private void updateOrders(){
        OrderBLL orderBLL = new OrderBLL();
        int total = orderBLL.count();
        totalOrders.setText(String.valueOf(total));
    }

    /**
     * Loads the "start-view" file -> navigate to the home window.
     * @param mainStage The primary JavaFx stage for the application
     */
    public void homeView(Stage mainStage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("start-view.fxml"));
            Parent root = loader.load();
            WindowController clientcontroller = loader.getController();
            clientcontroller.init(mainStage);

            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}