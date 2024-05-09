package presentationLayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * The main class for the Orders Management JavaFX application.
 * It sets up the initial stage, scene and configuration for the application window.
 * @author Ruben
 */
public class OrdersManagement extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(OrdersManagement.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 785, 510);
        stage.setTitle("Orders Management");
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        ((WindowController)fxmlLoader.getController()).init(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}