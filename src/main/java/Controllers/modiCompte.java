package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Models.CompteClient;
import Services.CompteClientService;
import java.sql.SQLException;
import java.util.regex.Pattern;

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

            // Validating non-empty fields
            if (nom.isEmpty() || prenom.isEmpty() || rib.isEmpty() || mail.isEmpty() || tel.isEmpty() || soldeField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
                return;
            }

            // Validating nom and prenom fields (only letters)
            if (!nom.matches("[a-zA-Z]+") || !prenom.matches("[a-zA-Z]+")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom et le prénom doivent contenir seulement des lettres !");
                return;
            }

            // Validating rib (only digits and length = 16)
            if (!rib.matches("\\d{16}")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le RIB doit contenir exactement 16 chiffres !");
                return;
            }

            // Validating email format
            if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", mail)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Format d'adresse e-mail invalide !");
                return;
            }

            // Validating tel (only digits and length = 8)
            if (!tel.matches("\\d{8}")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres !");
                return;
            }

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
