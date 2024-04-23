package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.Carte;
import services.CarteService;

import java.sql.SQLException;

public class addCarte {

    private final CarteService carteService = new CarteService();

    @FXML
    private TextField num_cTF;

    @FXML
    private TextField dateExpTF;

    @FXML
    private TextField typeTF;

    @FXML
    private TextField statutTF;


    @FXML
    void addCarte(ActionEvent event) throws SQLException {
        if (num_cTF.getText().isEmpty()) {
            System.out.println("Card number is empty");
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de carte est vide !");
            return;
        }

        try {
            String num_c = num_cTF.getText();
            // Convert date string to java.util.Date (assumes date format is yyyy-MM-dd)
            java.util.Date dateExp = java.sql.Date.valueOf(dateExpTF.getText());

            String type = typeTF.getText();
            // Vérifier que le type de carte est "Visa" ou "Mastercard"
            if (!type.equalsIgnoreCase("Visa") && !type.equalsIgnoreCase("Mastercard")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le type de carte doit être Visa ou Mastercard !");
                return;
            }

            String statut = statutTF.getText();
            // Vérifier que le statut est "active", "inactive" ou "bloquee"
            if (!statut.equalsIgnoreCase("active") && !statut.equalsIgnoreCase("inactive") && !statut.equalsIgnoreCase("bloquee")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le statut doit être active, inactive ou bloquee !");
                return;
            }

            // Vérifiez si le numéro de carte est unique
            boolean isUnique = carteService.isCardNumberUnique(num_c);
            if (!isUnique) {
                System.out.println("Card number is not unique");
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de carte existe déjà !");
                return;
            }

            // Ajouter la nouvelle carte
            Carte carte = new Carte(num_c, dateExp, type, statut);
            carteService.add(carte);
            System.out.println("Carte ajoutée avec succès !");

            // Afficher un message de succès à l'utilisateur
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Carte ajoutée avec succès !");
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

}
