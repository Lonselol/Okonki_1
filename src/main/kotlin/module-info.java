module com.example.dz {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires kotlin.stdlib;

    opens com.example.dz to javafx.fxml;
    exports com.example.dz;
}