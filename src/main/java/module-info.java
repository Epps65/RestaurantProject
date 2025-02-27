module com.example.restaruantnew {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.restaruantnew to javafx.fxml;
    exports com.example.restaruantnew;
}