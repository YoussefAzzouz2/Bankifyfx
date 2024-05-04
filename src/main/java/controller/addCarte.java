package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import models.Carte;
import services.CarteService;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import models.CompteClient;
import services.CompteClientService;
import java.util.List;
import java.text.ParseException;


public class addCarte {

    private final CarteService carteService = new CarteService();
    @FXML
    private VBox pnItems ;
    @FXML
    private TextField num_cTF;

    @FXML
    private TextField dateExpTF;

    @FXML
    private TextField typeTF;

    @FXML
    private TextField statutTF;


    @FXML
    private ComboBox<Integer> compteClientComboBox;

    // Add an instance of CompteClientService to retrieve CompteClient objects
    private final CompteClientService compteClientService = new CompteClientService();

    public void initialize() {
        // Load available CompteClient objects from the CompteClientService
        List<CompteClient> compteClients = compteClientService.getAllCompteClients();



        // Iterate through each CompteClient and add the desired attribute (ID) to the ComboBox
        for (CompteClient compteClient : compteClients) {
            // Retrieve the ID of the CompteClient
            int clientId = compteClient.getId();

            // Add the ID to the ComboBox
            compteClientComboBox.getItems().add(clientId);
        }
    }

    @FXML
    private void fillCheques(ActionEvent event) {
        // Check if the content is already loaded

            // Load Cheques.fxml into panel_scroll VBox when the button is clicked
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cheques/Back/Cheques.fxml"));
                Node chequesNode = loader.load();
                pnItems.getChildren().setAll(chequesNode);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception if the FXML file cannot be loaded
            }

    }
    @FXML
    void loadMacarte(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addCarte.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }






    @FXML
    void addCarte(ActionEvent event) throws SQLException {
        // Check for empty fields
        if (num_cTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Card number is empty");
            return;
        }
        if (dateExpTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Expiration date is empty");
            return;
        }
        if (typeTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Type is empty");
            return;
        }
        if (compteClientComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Please select a CompteClient");
            return;
        }

        try {
            // Parse card number
            String cardNumber = num_cTF.getText();

            // Parse expiration date
            java.util.Date dateExp = java.sql.Date.valueOf(dateExpTF.getText());

            // Validate card type
            String type = typeTF.getText();
            if (!List.of("Visa", "Mastercard").contains(type)) {
                showAlert(Alert.AlertType.WARNING, "Input Validation Error", "Invalid card type. Allowed values: Visa, Mastercard");
                return;
            }

            // Retrieve the selected CompteClient ID from the ComboBox
            int selectedClientId = compteClientComboBox.getValue();

            // Retrieve the corresponding CompteClient object
            CompteClient selectedCompteClient = compteClientService.getById(selectedClientId);

            // Create a new Carte object with the provided information
            Carte newCarte = new Carte(cardNumber, dateExp, type, "active", selectedCompteClient);

            // Add the new Carte using the CarteService
            carteService.add(newCarte);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Carte added successfully!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid number format for input");
        } catch (IllegalArgumentException e) {
            System.out.println("Format de date invalide");
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format de date invalide !");
        }
    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void openAjouterTransactionWindow(ActionEvent event) {
        openWindow(event, "/addCarte.fxml", "Ajouter Transaction");
    }

    /**
     * Opens the Afficher Transactions window.
     *
     * @param event The event that triggered this method.
     */
    public void openAfficherTransactionsWindow(ActionEvent event) {
        openWindow(event, "/getCarte.fxml", "Afficher Transactions");
    }

    /**
     * General method to open a new window based on the provided FXML file and title.
     *
     * @param event    The event that triggered this method.
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param title    The title of the new window.
     */
    private void openWindow(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Get the event source (Button) and retrieve the stage
            Button button = (Button) event.getSource();
            Scene scene = button.getScene();
            Stage stage = (Stage) scene.getWindow();

            // Set the new scene and title on the stage
            stage.setTitle(title);
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            // Provide a user-friendly error message or log the error
            System.err.println("Error opening window: " + e.getMessage());
            // Optionally, you can display an alert to the user
            // showAlert(Alert.AlertType.ERROR, "Erreur", "Failed to open window!");
        }
    }



}