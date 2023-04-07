module com.example.retea_socializare {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.retea_socializare to javafx.fxml;
    exports com.example.retea_socializare;
    exports domain;
}