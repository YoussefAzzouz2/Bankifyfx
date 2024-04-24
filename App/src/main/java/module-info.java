module com.example.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    //requires mysql.connector.java;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires org.apache.pdfbox;
    requires java.desktop;

    opens controllers to javafx.fxml, javafx.base;
    opens models to javafx.base;




    exports com.example.app;
}
