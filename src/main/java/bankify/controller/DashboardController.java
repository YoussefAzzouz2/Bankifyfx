/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankify.controller;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Lenovo
 */
public class DashboardController implements Initializable {

    @FXML
    private Button affichercheques;

    @FXML
    private VBox panel_scroll;

    @FXML
    private Pane top_pane;
    


    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }




    @FXML
    private void fillCheques(ActionEvent event) {
        // Check if the content is already loaded
        if (panel_scroll.getChildren().isEmpty()) {
            // Load Cheques.fxml into panel_scroll VBox when the button is clicked
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankify/Cheques/Back/Cheques.fxml"));
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






