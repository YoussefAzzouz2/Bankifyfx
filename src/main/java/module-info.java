module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    requires com.google.gson;
    requires java.persistence;
    requires org.apache.pdfbox;



    opens models to javafx.base;


    exports controller;
    opens controller to javafx.fxml;

    exports controller.Cheques;
    opens controller.Cheques to javafx.fxml;


    exports org.example;



}