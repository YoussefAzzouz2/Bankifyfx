package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import models.CategorieAssurance;

import java.io.IOException;
import java.sql.*;

public class CategorieAssuranceController {

    @FXML
    private TextField agenceresponsableTF;

    @FXML
    private TableView<CategorieAssurance> categorieTable;

    @FXML
    private TextField decripTF;

    @FXML
    private Button goassurance;

    @FXML
    private TextField nomcategorieTF;

    @FXML
    private TextField typecouvertureTF;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/bankify";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    void addcategorie(ActionEvent event) {
        String nomCategorie = nomcategorieTF.getText();
        String description = decripTF.getText();
        String typeCouverture = typecouvertureTF.getText();
        String agenceResponsable = agenceresponsableTF.getText();

        // Input validation
        if (nomCategorie.length() < 3 || description.length() < 3 || typeCouverture.length() < 3 || agenceResponsable.length() < 3) {
            showAlert("All fields must have at least 3 characters.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO categorie_assurance (nom_categorie, description, TypeCouverture, agenceResponsable) VALUES (?, ?, ?, ?)")) {

            stmt.setString(1, nomCategorie);
            stmt.setString(2, description);
            stmt.setString(3, typeCouverture);
            stmt.setString(4, agenceResponsable);

            stmt.executeUpdate();

            showAlert("Categorie added successfully!");

            showcategorie(null); // Refresh the TableView

        } catch (SQLException e) {
            showAlert("Error adding categorie: " + e.getMessage());
        }
    }


    @FXML
    void deletecategorie(ActionEvent event) {
        CategorieAssurance selectedCategorie = categorieTable.getSelectionModel().getSelectedItem();
        if (selectedCategorie != null) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM categorie_assurance WHERE id_categorie = ?")) {

                stmt.setInt(1, selectedCategorie.getIdCategorie());
                stmt.executeUpdate();

                showAlert("Categorie deleted successfully!");
                showcategorie(null); // Refresh the TableView

            } catch (SQLException e) {
                showAlert("Error deleting categorie: " + e.getMessage());
            }
        } else {
            showAlert("Please select a categorie.");
        }
    }

    @FXML
    void showcategorie(ActionEvent event) {
        ObservableList<CategorieAssurance> categorieList = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM categorie_assurance")) {

            while (rs.next()) {
                CategorieAssurance categorie = new CategorieAssurance(
                        rs.getInt("id_categorie"),
                        rs.getString("nom_categorie"),
                        rs.getString("description"),
                        rs.getString("TypeCouverture"),
                        rs.getString("agenceResponsable")
                );
                categorieList.add(categorie);
            }

            categorieTable.setItems(categorieList);

        } catch (SQLException e) {
            showAlert("Error fetching categories: " + e.getMessage());
        }
    }


    @FXML
    void updatecategorie(ActionEvent event) {
        CategorieAssurance selectedCategorie = categorieTable.getSelectionModel().getSelectedItem();
        if (selectedCategorie != null) {
            String nomCategorie = nomcategorieTF.getText();
            String description = decripTF.getText();
            String typeCouverture = typecouvertureTF.getText();
            String agenceResponsable = agenceresponsableTF.getText();

            // Input validation
            if (nomCategorie.length() < 3 || description.length() < 3 || typeCouverture.length() < 3 || agenceResponsable.length() < 3) {
                showAlert("All fields must have at least 3 characters.");
                return;
            }

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("UPDATE categorie_assurance SET nom_categorie = ?, description = ?, TypeCouverture = ?, agenceResponsable = ? WHERE id_categorie = ?")) {

                stmt.setString(1, nomCategorie);
                stmt.setString(2, description);
                stmt.setString(3, typeCouverture);
                stmt.setString(4, agenceResponsable);
                stmt.setInt(5, selectedCategorie.getIdCategorie());

                stmt.executeUpdate();

                showAlert("Categorie updated successfully!");
                showcategorie(null); // Refresh the TableView

            } catch (SQLException e) {
                showAlert("Error updating categorie: " + e.getMessage());
            }
        } else {
            showAlert("Please select a categorie.");
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    public void goToAssuranceButtonClicked(ActionEvent actionEvent) {
        try {
            // Load AssuranceGUI.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AssuranceGUI.fxml"));
            Parent root = loader.load();

            // Get the stage from the action event
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showAlert("Error loading AssuranceGUI: " + e.getMessage());
        }
    }

}
