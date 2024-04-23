package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class AjoutVirement {

    @FXML
    private TextField compteSourceTextField;

    @FXML
    private TextField compteDestinationTextField;

    @FXML
    private TextField montantTextField;

    @FXML
    private TextField dateTextField;

    @FXML
    private TextField heureTextField;

    @FXML
    private void initialize() {
        // Initialize method if needed
    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException {
        // This method will be called when the confirm button is clicked
        // You can retrieve the values from the text fields and perform actions here
        String compteSource = compteSourceTextField.getText();
        String compteDestination = compteDestinationTextField.getText();
        float montant = 0.0f;
        Date date = null;
        Time heure = null;

        // Validation: Check if the montant text field is not empty and is a valid float number
        try {
            montant = Float.parseFloat(montantTextField.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid Montant", "Please enter a valid numeric value for Montant.");
            return;
        }

        // Validation: Check if the date and heure fields are not empty
        String dateString = dateTextField.getText();
        String heureString = heureTextField.getText();
        if (dateString.isEmpty() || heureString.isEmpty()) {
            showAlert("Incomplete Information", "Please enter both Date and Heure.");
            return;
        }

        // Parse date and heure strings to Date and Time objects
        try {
            date = Date.valueOf(dateString);
            heure = Time.valueOf(heureString);
        } catch (IllegalArgumentException e) {
            showAlert("Invalid Date or Heure", "Please enter valid Date and Heure in the format yyyy-mm-dd for Date and hh:mm:ss for Heure.");
            return;
        }

        // Perform actions with the retrieved data, such as processing the virement

        // Show a confirmation dialog
        showAlert("Success", "Virement effectué avec succès!");
    }

    // Method to show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
