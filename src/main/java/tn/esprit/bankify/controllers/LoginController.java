package tn.esprit.bankify.controllers;

import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import tn.esprit.bankify.entities.User;
import tn.esprit.bankify.services.ServiceEmail;
import tn.esprit.bankify.services.ServiceUser;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class LoginController {

    public Label captchaLabel;
    public TextField captchaInput;
    public ToggleButton toggleButton;
    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Label ShownPassword;

    @FXML
    private Button switchToSignUp;

    private Button resetPassword;
    
    private String captchaChallenge;


    private ServiceUser serviceUser;

    public LoginController() {
        serviceUser = new ServiceUser();
    }
    @FXML
    public void initialize() {
        captchaChallenge = generateCaptcha();
        captchaLabel.setText(captchaChallenge);
    }


    @FXML
    void toggleButton(ActionEvent event) {
        if (toggleButton.isSelected()) {
            txtPassword.setVisible(false);
            ShownPassword.setText(txtPassword.getText());
            ShownPassword.setVisible(true);
            toggleButton.setText("Hide");
        } else {
            ShownPassword.setVisible(false);
            txtPassword.setVisible(true);
            toggleButton.setText("Show");
        }
    }

    @FXML
    void passwordFieldKeyTyped(KeyEvent event) {
        if (toggleButton.isSelected()) {
            ShownPassword.setText(txtPassword.getText());
        }
    }
    private String generateCaptcha() {
        int length = 6; // Length of the CAPTCHA challenge
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            captcha.append(chars.charAt(random.nextInt(chars.length())));
        }
        return captcha.toString();
    }

    @FXML
    void login(ActionEvent event) {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        // Validate email and password
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Veuillez remplir tous les champs.");
            return;
        }
        // Authenticate user
        User user = serviceUser.authenticateUser(email, password);
        if (user != null) {
            String userInput = captchaInput.getText().trim();
            if (userInput.equals(captchaChallenge)) {
            if (user.isVerified()) {
                showAlert("Success", "Vous êtes connecté avec succès.");
                navigateToMainScreen(event, user.getRole());
            } else {
                handleVerification(user.getEmail(), event);
            }} else {
                showAlert("Login Failed", "Invalid CAPTCHA. Please try again.");
            }
        } else {
            showAlert("Error", "Adresse email ou mot de passe incorrect.");
        }
    }
    private Optional<String> showVerificationDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Verification Code");
        dialog.setHeaderText(null);
        dialog.setContentText("Entrez le code de vérification reçu par email:");
        return dialog.showAndWait();
    }
    private void handleVerification(String email, ActionEvent event) {
        // Generate verification code
        String verificationCode = generateVerificationCode();

        try {
            // Send verification email
            ServiceEmail.sendEmail(email, "Login Verification", "Your verification code is: " + verificationCode);
            showAlert("Verification Email Sent", "Un code de vérification a été envoyé à votre adresse email.");

            // Prompt user for verification code
            Optional<String> enteredCode = showVerificationDialog();
            if (enteredCode.isPresent() && enteredCode.get().equals(verificationCode)) {
                // Update user as verified in the database
                ServiceUser serviceUser = new ServiceUser();
                serviceUser.setVerified(email, true); // Assuming you have a method to update user's verified status

                showAlert("Success", "Vous êtes connecté avec succès.");
                navigateToMainScreen(event, "client");
            } else {
                showAlert("Error", "Le code de vérification est incorrect.");
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to send verification email: " + e.getMessage());
        }
    }

    private void navigateToMainScreen(ActionEvent event, String role) {
        String mainScreenPath = role.equalsIgnoreCase("admin") ? "/back.fxml" : "/front.fxml";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(mainScreenPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bankify");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6); // Extract a 6-character substring
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
