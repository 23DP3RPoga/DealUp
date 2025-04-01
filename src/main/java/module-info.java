module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.opencsv;

    opens com.example to javafx.fxml;
    exports com.example;
}
