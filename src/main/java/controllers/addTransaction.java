package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import models.Transaction;
import services.TransactionService;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.Carte;
import models.Transaction;
import services.CarteService;
import services.TransactionService;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class addTransaction {

    private final TransactionService transactionService = new TransactionService();
    private final CarteService carteService = new CarteService();

    @FXML
    private TextField montantTF;

    @FXML
    private TextField dateTF;

    @FXML
    private TextField typeTF;

    @FXML
    private TextField statutTF;

    @FXML
    private ComboBox<String> carteComboBox;

    public void initialize() {
        // Load available cartes from the CarteService
        List<Carte> cartes = carteService.getAllCartes();

        // Iterate through each Carte and add the card number (num_c) to the ComboBox
        for (Carte carte : cartes) {
            String cardNumber = carte.getNum_c();
            carteComboBox.getItems().add(cardNumber);
        }
    }


    @FXML
    void addTransaction(ActionEvent event) throws SQLException {
        if (montantTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Montant is empty");
            return;
        }
        if (dateTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Date is empty");
            return;
        }
        if (carteComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Please select a Carte");
            return;
        }

        try {
            // Parse montant
            double montant = Double.parseDouble(montantTF.getText());

            // Parse date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateTF.getText());

            // Validate type
            String type = typeTF.getText();
            if (!List.of("paiement", "achat", "retrait").contains(type)) {
                showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Invalid type. Allowed values: paiement, achat, retrait");
                return;
            }

            // Validate statut
            String statut = statutTF.getText();
            if (!List.of("approuvee", "refusee", "en attente").contains(statut)) {
                showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Invalid statut. Allowed values: approuvee, refusee, en attente");
                return;
            }

            // Retrieve the selected card number
            String selectedCardNumber = carteComboBox.getValue();

            // Retrieve the corresponding Carte object based on the selected card number
            Carte selectedCarte = null;
            for (Carte carte : carteService.getAllCartes()) {
                if (carte.getNum_c().equals(selectedCardNumber)) {
                    selectedCarte = carte;
                    break;
                }
            }

            // Check if the selected Carte was found
            if (selectedCarte == null) {
                showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Invalid Carte selection.");
                return;
            }

            // Create a new transaction with the selected Carte
            Transaction transaction = new Transaction(montant, date, type, statut, selectedCarte);

            // Add the transaction
            transactionService.add(transaction);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction added successfully!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid number format for montant");
        } catch (ParseException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid date format for date");
        }
    }


    public void openAjouterTransactionWindow(ActionEvent event) {
        try {
            // Load the FXML file for the Ajouter Transaction window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addTransaction.fxml"));
            Parent root = loader.load();

            // Get the current event's source
            MenuItem menuItem = (MenuItem) event.getSource();

            // Get the current scene and stage from the menu item
            Scene scene = menuItem.getParentPopup().getOwnerWindow().getScene();
            Stage stage = (Stage) scene.getWindow();

            // Update the stage with the new scene for Ajouter Transaction
            stage.setTitle("Ajouter Transaction");
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void openAfficherTransactionsWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/getTransaction.fxml"));
            Parent root = loader.load();

            // Get the source of the event
            MenuItem menuItem = (MenuItem) event.getSource();

            // Get the scene from the source, and then get the stage from the scene
            Scene scene = menuItem.getParentPopup().getOwnerWindow().getScene();
            Stage stage = (Stage) scene.getWindow();

            // Set the new scene on the existing stage
            stage.setTitle("Afficher Transactions");
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception (e.g., log it)
        }
    }



    /**
     * Method to display alerts to the user.
     *
     * @param alertType The type of alert (e.g., INFORMATION, WARNING, ERROR).
     * @param title The title of the alert dialog.
     * @param content The content or message of the alert.
     */
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional, remove if you want to set a header
        alert.setContentText(content);
        alert.showAndWait();
    }
}
