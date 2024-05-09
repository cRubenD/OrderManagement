package presentationLayer;

import businessLogic.BillBLL;
import businessLogic.ClientBLL;
import businessLogic.OrderBLL;
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

import model.Bill;
import model.Client;
import model.Order;
import model.Product;

import java.io.IOException;
import java.util.List;

/**
 * The controller class responsible for managing the order-related operations
 * @author Ruben
 */
public class OrderController {
    @FXML
    public ImageView closeBtn;
    @FXML
    public ImageView homeImg;
    @FXML
    public ImageView clientImg;
    @FXML
    public ImageView productImg;
    @FXML
    public TextField quantity;
    @FXML
    public Button makeOrderBtn;
    @FXML
    public ComboBox clientBox;
    @FXML
    public ComboBox productBox;
    @FXML
    public TableView logTable;
    @FXML
    public TableColumn orderId;
    @FXML
    public TableColumn clientName;
    @FXML
    public TableColumn productName;
    @FXML
    public TableColumn quantityC;
    @FXML
    public TableColumn totalPrice;

    private Stage mainStage;

    private final ObservableList<Bill> billData = FXCollections.observableArrayList();


    public OrderController(){
        // Default constructor needed for FXMLoader
    }

    /**
     * Initializes the Order view, setting up the ComboBox items like clients and products,
     * event handlers and table columns.
     * @param stage Current stage used in this controller
     */
    public void init(Stage stage) {

        this.mainStage = stage;

        ClientBLL clientBLL = new ClientBLL();
        List<Client> clientList = clientBLL.findAll();
        clientBox.setItems(FXCollections.observableArrayList(
                clientList.stream().map(Client::getName).toList()
        ));

        // Populate ComboBox with product names
        ProductBLL productBLL = new ProductBLL();
        List<Product> productList = productBLL.findAll();
        productBox.setItems(FXCollections.observableArrayList(
                productList.stream().map(Product::getName).toList()
        ));

        setupTableColumns();
        updateOrderTable();

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

        productImg.setOnMouseClicked(mouseEvent -> {
            ProductController productController = new ProductController();
            productController.productView(mainStage);
        });

        makeOrderBtn.setOnMouseClicked(mouseEvent -> makeOrder());
    }

    /**
     * Configure the table columns with the appropiate property value factories
     */
    private void setupTableColumns(){
        orderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        clientName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityC.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }

    public void orderView(Stage mainStage){
        try {
            FXMLLoader loader = new FXMLLoader(OrderController.class.getResource("order-view.fxml"));
            Parent root = loader.load();
            OrderController orderController = loader.getController();
            orderController.init(mainStage);

            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new order based on the selected client, product and the specified quantity.
     * Validates the stock level and updates the product's stock after creating a new order.
     * Finally, it creates a new bill with all the information of the order.
     */
    private void makeOrder(){
        String clientName = (String) clientBox.getValue();
        String productName = (String) productBox.getValue();
        int orderQuantity;

        try{
            orderQuantity = Integer.parseInt(quantity.getText());
            if(orderQuantity <= 0){
                throw new NumberFormatException("Quantity must be positive");
            }
        } catch (NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid quantity " + ex.getMessage());
            alert.show();
            return;
        }

        ClientBLL clientBLL = new ClientBLL();
        Client client = clientBLL.findByName(clientName);

        ProductBLL productBLL = new ProductBLL();
        Product product = productBLL.findByName(productName);

        if(product.getStock() < orderQuantity){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Under-Stock for " + productName);
            alert.show();
            return;
        }

        Order order = new Order(client.getId(), product.getId(), orderQuantity);
        OrderBLL orderBLL = new OrderBLL();
        orderBLL.insertOrder(order);
        product.setStock(product.getStock() - orderQuantity);
        float totalPrice = product.getPrice() * orderQuantity;
        productBLL.updateProduct(product);

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Order created successfully.");
        successAlert.show();

        System.out.println(order.getId());
        Bill bill = new Bill(order.getId(), clientName, productName, orderQuantity, totalPrice);
        BillBLL billBLL = new BillBLL();
        billBLL.insertBill(bill);

        updateOrderTable();
    }

    /**
     * Updates the table from the order-view with the latest data from the database
     */
    private void updateOrderTable(){
        BillBLL billBLL = new BillBLL();
        List<Bill> bills = billBLL.findAll();

        billData.setAll(bills);
        logTable.setItems(billData);
    }
}
