package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import Models.CompteClient;
import Services.CompteClientService;

import java.sql.SQLException;

public class AjoutCompte {

    private final CompteClientService compteClientService = new CompteClientService();

    @FXML
    private TextField nomTF;

    @FXML
    private TextField prenomTF;

    @FXML
    private TextField ribTF;

    @FXML
    private TextField mailTF;

    @FXML
    private TextField telTF;

    @FXML
    private TextField soldeTF;

    @FXML
    private Button confirmButton;

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException {
        try {
            String nom = nomTF.getText();
            String prenom = prenomTF.getText();
            String rib = ribTF.getText();
            String mail = mailTF.getText();
            String tel = telTF.getText();

            // Parsing the float value for solde
            float solde = Float.parseFloat(soldeTF.getText());

            // Adding the new compteClient
            CompteClient compteClient = new CompteClient(nom, prenom, rib, mail, tel, solde);
            compteClientService.add(compteClient);
            System.out.println("Compte client ajouté avec succès !");

            // Show a success message to the user
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte client ajouté avec succès !");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format de nombre invalide !");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
