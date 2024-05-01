package tn.esprit.bankify.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.bankify.entities.User;
import tn.esprit.bankify.services.ServiceUser;

import java.io.IOException;
import java.util.List;

public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button switchToSignUp;

    private Button resetPassword;


    private ServiceUser serviceUser;

    public LoginController() {
        serviceUser = new ServiceUser();
    }

    @FXML
    void login() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        // Check if email and password fields are empty
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Veuillez remplir tous les champs.");
            return;
        }

        // Call the service to retrieve all users from the database
        List<User> userList = serviceUser.getAllUsers();

        // Check if the provided email and password match any user in the database
        boolean loggedIn = false;
        for (User user : userList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                loggedIn = true;
                break;
            }
        }

        if (loggedIn) {
            showAlert("Success", "Vous êtes connecté avec succès.");

            // Load aff.fxml and display it
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/back.fxml"));
                Parent root = loader.load();

                // Create a new stage
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                // Set the title of the stage
                stage.setTitle("Bankify");

                // Show the stage
                stage.show();

                // Close the current login stage
                Stage loginStage = (Stage) btnLogin.getScene().getWindow();
                loginStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            showAlert("Error", "Adresse email ou mot de passe incorrect.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void navigateToRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) switchToSignUp.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bankify");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetPassword(ActionEvent actionEvent) throws IOException {
        // Get a reference to the stage of the current window
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        // Load resetPassword.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resetPassword.fxml"));
        Parent root = loader.load();

        // Create a new stage for the resetPassword scene
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }
}
