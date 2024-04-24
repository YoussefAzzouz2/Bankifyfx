package tn.esprit.bankify;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class bankify extends Application {

    public static Stage stage = null;

    @Override
    public void start(Stage primaryStage) throws IOException {

        try {

            FXMLLoader loader= new FXMLLoader(getClass().getResource("/back.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("Bankify");
            primaryStage.setScene(scene);
            bankify.stage = primaryStage;
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        launch(args);
    }
}