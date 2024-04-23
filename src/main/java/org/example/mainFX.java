package org.example;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/showVirement.fxml"));

        // Create a scene with the loaded FXML file
        Scene scene = new Scene(root, 600, 400);

        // Set the title of the stage
        primaryStage.setTitle("Ajout de Compte");

        // Set the scene to the stage and show it
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

