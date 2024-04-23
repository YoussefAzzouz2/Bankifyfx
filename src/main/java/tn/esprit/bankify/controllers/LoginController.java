package tn.esprit.bankify.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/aff.fxml"));
                Parent root = loader.load();

                // Create a new stage
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
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
}
