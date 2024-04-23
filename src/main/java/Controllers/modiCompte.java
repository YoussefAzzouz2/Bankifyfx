package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Services.CompteClientService;
import Models.CompteClient;

import java.sql.SQLException;

public class modiCompte {

    private final CompteClientService compteClientService = new CompteClientService();
    private CompteClient compteClient;

    @FXML
    private Label compteClientLabel;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField ribField;

    @FXML
    private TextField mailField;

    @FXML
    private TextField telField;

    @FXML
    private TextField soldeField;

    @FXML
    private Button modifierButton;

    /**
     * Initialise les champs avec les valeurs du compte client à modifier.
     */
    public void initData(CompteClient compteClient) {
        this.compteClient = compteClient;
        // Afficher les détails du compte client dans les champs de texte
        nomField.setText(compteClient.getNom());
        prenomField.setText(compteClient.getPrenom());
        ribField.setText(compteClient.getRib());
        mailField.setText(compteClient.getMail());
        telField.setText(compteClient.getTel());
        soldeField.setText(String.valueOf(compteClient.getSolde()));
        compteClientLabel.setText("Modification du compte client ID: " + compteClient.getId());
    }

    /**
     * Cette méthode est appelée lorsque l'utilisateur clique sur le bouton "Modifier".
     */
    @FXML
    void modifierCompteClient(ActionEvent event) {
        try {
            // Obtenir les valeurs des champs de texte
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String rib = ribField.getText();
            String mail = mailField.getText();
            String tel = telField.getText();
            float solde = Float.parseFloat(soldeField.getText());

            // Mettre à jour les valeurs du compte client avec les valeurs modifiées
            compteClient.setNom(nom);
            compteClient.setPrenom(prenom);
            compteClient.setRib(rib);
            compteClient.setMail(mail);
            compteClient.setTel(tel);
            compteClient.setSolde(solde);

            // Appeler le service pour modifier le compte client
            compteClientService.update(compteClient);

            // Afficher un message de succès à l'utilisateur
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte client modifié avec succès !");

            // Fermer la fenêtre actuelle après la modification réussie
            closeWindow();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format de solde invalide !");
        } catch (SQLException e) {
            e.printStackTrace();
            // Afficher un message d'erreur à l'utilisateur
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification du compte client !");
        }
    }

    /**
     * Affiche une boîte de dialogue d'alerte à l'utilisateur.
     *
     * @param alertType le type d'alerte (INFORMATION, WARNING, ERROR)
     * @param title le titre de l'alerte
     * @param message le message de l'alerte
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Ferme la fenêtre actuelle.
     */
    private void closeWindow() {
        Stage stage = (Stage) modifierButton.getScene().getWindow();
        stage.close();
    }
}
