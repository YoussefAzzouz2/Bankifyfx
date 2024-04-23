module com.example.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    //requires mysql.connector.java;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens controllers to javafx.fxml, javafx.base;
    opens models to javafx.base;




    exports com.example.app;
}
