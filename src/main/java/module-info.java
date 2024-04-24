module bankify {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    requires pdfbox.app;
    requires com.google.gson;


    opens bankify to javafx.fxml;
    exports bankify;

    exports bankify.controller;
    opens bankify.controller to javafx.fxml;

    exports bankify.controller.Cheques;
    opens bankify.controller.Cheques to javafx.fxml;


}