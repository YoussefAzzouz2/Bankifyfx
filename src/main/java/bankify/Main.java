package bankify;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {


       //FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankify/Cheques/Back/Cheques.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankify/Front/Front.fxml"));
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankify/Cheques/Front/FrontCheques.fxml"));



        Parent root = loader.load(); // Load the FXML file and get the root node
        Scene scene = new Scene(root);

        primaryStage.setTitle("Bankify");
        primaryStage.setScene(scene); // Set the loaded root node as the scene root
        primaryStage.show();


    }

    public static void main(String[] args) throws SQLException {
        launch(args);

    }

}
