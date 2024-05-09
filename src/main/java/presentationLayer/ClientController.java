package presentationLayer;

import businessLogic.ClientBLL;
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

import model.Client;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The ClientController class provides CRUD functionalities for clients and manages
 * the user interface with the Client-view.
 * @author Ruben
 */
public class ClientController {
    @FXML
    public ImageView closeBtn;
    @FXML
    public TextField clientName;
    @FXML
    public ImageView homeImg;
    @FXML
    public ImageView productImg;
    @FXML
    public ImageView orderImg;
    @FXML
    public TextField phone;
    @FXML
    public TextField email;
    @FXML
    public Button addClientBtn;
    @FXML
    public Button updateClientBtn;
    @FXML
    public Button deleteClientBtn;
    @FXML
    public TableView clientTable;
    @FXML
    public TableColumn clientId;
    @FXML
    public TableColumn clientNameC;
    @FXML
    public TableColumn clientPhone;
    @FXML
    public TableColumn clientEmail;
    private Stage mainStage;

    private final ObservableList<Client> clientData = FXCollections.observableArrayList();

    public ClientController(){
        // default constructor for the FXML loading
    }

    /**
     * Initializes the ClientController and sets up the user interface components.
     * @param stage The stage where the client view is displayed
     */
    public void init(Stage stage) {

        this.mainStage = stage;

        setupTableColumns();
        updateClientTable();

        // close button to close the application
        closeBtn.setOnMouseClicked(mouseEvent -> {
            mainStage.close();
        });

        homeImg.setOnMouseClicked(mouseEvent -> {
            WindowController windowController = new WindowController();
            windowController.homeView(mainStage);
        });

        productImg.setOnMouseClicked(mouseEvent -> {
            ProductController productController = new ProductController();
            productController.productView(mainStage);
        });

        orderImg.setOnMouseClicked(mouseEvent -> {
            OrderController orderController = new OrderController();
            orderController.orderView(mainStage);
        });

        assert addClientBtn != null : "addClientBtn este null";
        addClientBtn.setOnMouseClicked(mouseEvent -> {
            ClientBLL clientBLL = new ClientBLL(getClient());
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Client successfully added.", ButtonType.OK);
            successAlert.show();
            updateClientTable();
        });

        deleteClientBtn.setOnMouseClicked(mouseEvent -> {
            Client selectedClient = (Client) clientTable.getSelectionModel().getSelectedItem();

            if (selectedClient == null) {

                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a client to delete.", ButtonType.OK);
                alert.show();
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this client?", ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait();

            if (confirmAlert.getResult() == ButtonType.YES) {

                ClientBLL clientBLL = new ClientBLL();
                clientBLL.deleteClient(selectedClient);
                updateClientTable();
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Client deleted successfully.", ButtonType.OK);
                successAlert.show();
            }
        });

        updateClientBtn.setOnMouseClicked(mouseEvent -> {
            Client selectedClient = (Client) clientTable.getSelectionModel().getSelectedItem();

            if (selectedClient == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a client to update.", ButtonType.OK);
                alert.show();
                return;
            }

            boolean updated = false;

            if (!clientName.getText().isEmpty()) {
                selectedClient.setName(clientName.getText());
                updated = true;
            }

            if (!phone.getText().isEmpty()) {
                selectedClient.setPhone(phone.getText());
                updated = true;
            }

            if (!email.getText().isEmpty()) {
                selectedClient.setEmail(email.getText());
                updated = true;
            }

            if (updated) {
                ClientBLL clientBLL = new ClientBLL();
                clientBLL.updateClient(selectedClient);

                updateClientTable();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Client updated successfully.", ButtonType.OK);
                successAlert.show();
            } else {
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "No changes made.", ButtonType.OK);
                infoAlert.show();
            }
        });
    }

    /**
     * Configure the TableView columns with the appropriate property value factories
     */
    private void setupTableColumns(){
        clientId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        clientNameC.setCellValueFactory(new PropertyValueFactory<>("Name"));
        clientPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        clientEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
    }

    public void clientView(Stage mainStage){
        try {
            FXMLLoader loader = new FXMLLoader(ClientController.class.getResource("client-view.fxml"));
            Parent root = loader.load();
            ClientController clientController = loader.getController();
            clientController.init(mainStage);

            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the TableView with the current list of clients from the database
     */
    private void updateClientTable(){
        ClientBLL clientBLL = new ClientBLL();
        List<Client> clients = clientBLL.findAll();

        clientData.setAll(clients);
        clientTable.setItems(clientData);
    }

    /**
     * Retrieves a list of textFields representing the data from a client: name, phone and email.
     * @return List of textFields that will be later converted
     */
    private @NotNull List<TextField> getClient(){
        List<TextField> list = new ArrayList<>();
        list.add(clientName);
        list.add(phone);
        list.add(email);
        return list;
    }
}
