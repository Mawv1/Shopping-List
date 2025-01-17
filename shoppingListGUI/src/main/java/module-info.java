module com.example.shoppinglistgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;

    opens com.example.shoppinglistgui to javafx.fxml;
    exports com.example.shoppinglistgui;
}