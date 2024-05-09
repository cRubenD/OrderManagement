module presentationLayer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires mysql.connector.java;
    requires java.sql;
    requires java.desktop;
    requires org.jetbrains.annotations;

    opens model to javafx.base;
    opens presentationLayer to javafx.fxml;
    exports presentationLayer;
}