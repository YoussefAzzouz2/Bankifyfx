module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    requires com.google.gson;
    requires java.persistence;
    requires org.apache.pdfbox;
    requires twilio;
    requires com.google.zxing;


    opens models to javafx.base;


    exports controllers;
    opens controllers to javafx.fxml;

    exports controllers.Cheques;
    opens controllers.Cheques to javafx.fxml;



    opens controllers.Compte to javafx.fxml;
    opens controllers.creditController to javafx.fxml;
    opens controllers.categorieCreditController to javafx.fxml;
    opens controllers.remboursementController to javafx.fxml;


    exports org.example;



}