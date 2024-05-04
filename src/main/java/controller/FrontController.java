package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FrontController   implements Initializable {

    @FXML
    private VBox panel_scroll;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillCheques(null); // Call fillCheques when the page is loaded
    }


    @FXML
    private void fillCheques(ActionEvent event) {
        // Check if the content is already loaded

            // Load Cheques.fxml into panel_scroll VBox when the button is clicked
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cheques/Front/FrontCheques.fxml"));
                Node chequesNode = loader.load();
                panel_scroll.getChildren().setAll(chequesNode);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception if the FXML file cannot be loaded
            }

    }


}
