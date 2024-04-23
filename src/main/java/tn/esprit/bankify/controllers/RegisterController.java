package tn.esprit.bankify.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.bankify.entities.User;
import tn.esprit.bankify.services.ServiceUser;

import java.io.IOException;
import java.time.LocalDate;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private ChoiceBox<String> choiceGenre;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private Button btnRegister;

    @FXML
    private Button switchToLogin;

    private final ServiceUser serviceUser = new ServiceUser();

    @FXML
    private void initialize() {
        // Populate the genre choice box
        choiceGenre.getItems().addAll("Male", "Female");

        // Example event handler for the register button
        btnRegister.setOnAction(event -> register());
    }
    @FXML
    private void navigateToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) switchToLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register() {
        // Retrieve user inputs
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String genre = choiceGenre.getValue();
        LocalDate dateNaissance = dateNaissancePicker.getValue();

        // Create a new User object
        User user = new User(nom, prenom, email, password, java.sql.Date.valueOf(dateNaissance), genre);

        // Add the user to the database using the service
        serviceUser.Ajouter(user);

        // Show alert
        showAlert(AlertType.INFORMATION, "Registration Successful", "User registered successfully!");

        // Navigate to login.fxml
        navigateToLogin();
    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRegister.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        txtEmail.clear();
        txtPassword.clear();
        txtNom.clear();
        txtPrenom.clear();
        choiceGenre.getSelectionModel().clearSelection();
        dateNaissancePicker.getEditor().clear();
    }
}
