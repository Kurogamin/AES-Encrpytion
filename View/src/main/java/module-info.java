module com.example.view {
    requires javafx.controls;
    requires javafx.fxml;
    requires Model;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.view to javafx.fxml;
    exports com.example.view;
}