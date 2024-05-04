package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class FrontController {

    @FXML
    private VBox panel_scroll;

    @FXML
    private void fillCheques(ActionEvent event) {
        // Check if the content is already loaded
        if (panel_scroll.getChildren().isEmpty()) {
            // Load Cheques.fxml into panel_scroll VBox when the button is clicked
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cheques/Front/FrontCheques.fxml"));
                Node chequesNode = loader.load();
                panel_scroll.getChildren().setAll(chequesNode);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception if the FXML file cannot be loaded
            }
        } else {
            // Content is already loaded, so clear the children to make it disappear
            panel_scroll.getChildren().clear();
        }
    }


}
