package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import models.Agence;

import java.io.IOException;
import java.sql.*;

public class AgenceController {

    @FXML
    private TableView<Agence> agenceTable;


    @FXML
    private TextField nomAgenceTF;

    @FXML
    private TextField emailAgenceTF;

    @FXML
    private TextField telAgenceTF;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/bankify";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    void initialize() {
        showAgence(null);
    }

    @FXML
    void AddAgence(ActionEvent event) {
        String nomAgence = nomAgenceTF.getText();
        String emailAgence = emailAgenceTF.getText();
        String telAgence = telAgenceTF.getText();

        // Input validation
        if (nomAgence.length() < 3 || emailAgence.length() < 3 || telAgence.length() < 3) {
            showAlert("All fields must have at least 3 characters.");
            return;
        }

        if (!telAgence.matches("\\d+")) { // Check if telAgence contains only numbers
            showAlert("Tel Agence should contain only numbers.");
            return;
        }

        if (!isValidEmail(emailAgence)) { // Check if emailAgence is a valid email
            showAlert("Invalid email format.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO agence (nom_agence, email_agence, tel_agence) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nomAgence);
            stmt.setString(2, emailAgence);
            stmt.setString(3, telAgence);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                Agence newAgence = new Agence(id, nomAgence, emailAgence, telAgence);
                agenceTable.getItems().add(newAgence);
            }

            clearFields();

        } catch (SQLException e) {
            showAlert("Error adding record: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        // Simple email validation using regex
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(regex);
    }



    @FXML
    void deleteAgence(ActionEvent event) {
        Agence selectedAgence = agenceTable.getSelectionModel().getSelectedItem();
        if (selectedAgence != null) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM agence WHERE id = ?")) {

                stmt.setInt(1, selectedAgence.getId());  // Assuming getId() returns the id of Agence
                stmt.executeUpdate();

                showAgence(null);

            } catch (SQLException e) {
                showAlert("Error deleting record: " + e.getMessage());
            }
        } else {
            showAlert("Please select an agence.");
        }
    }



    @FXML
    void showAgence(ActionEvent event) {
        ObservableList<Agence> agenceList = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM agence")) {

            while (rs.next()) {
                Agence agence = new Agence(
                        rs.getInt("id"),
                        rs.getString("nom_agence"),
                        rs.getString("email_agence"),
                        rs.getString("tel_agence")
                );
                agenceList.add(agence);
            }

            agenceTable.setItems(agenceList);



        } catch (SQLException e) {
            showAlert("Error fetching records: " + e.getMessage());
        }
    }




    @FXML
    void updateAgence(ActionEvent event) {
        Agence selectedAgence = agenceTable.getSelectionModel().getSelectedItem();
        if (selectedAgence != null) {
            String nomAgence = nomAgenceTF.getText();
            String emailAgence = emailAgenceTF.getText();
            String telAgence = telAgenceTF.getText();

            // Input validation
            if (nomAgence.length() < 3 || emailAgence.length() < 3 || telAgence.length() < 3) {
                showAlert("All fields must have at least 3 characters.");
                return;
            }

            if (!telAgence.matches("\\d+")) { // Check if telAgence contains only numbers
                showAlert("Tel Agence should contain only numbers.");
                return;
            }

            if (!isValidEmail(emailAgence)) { // Check if emailAgence is a valid email
                showAlert("Invalid email format.");
                return;
            }

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("UPDATE agence SET nom_agence = ?, email_agence = ?, tel_agence = ? WHERE id = ?")) {

                stmt.setString(1, nomAgence);
                stmt.setString(2, emailAgence);
                stmt.setString(3, telAgence);
                stmt.setInt(4, selectedAgence.getId());  // Assuming getId() returns the id of Agence

                stmt.executeUpdate();

                showAgence(null);
                clearFields();

            } catch (SQLException e) {
                showAlert("Error updating record: " + e.getMessage());
            }
        } else {
            showAlert("Please select an agence.");
        }
    }



    private void clearFields() {
        nomAgenceTF.clear();
        emailAgenceTF.clear();
        telAgenceTF.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void goToCategorieButtonClicked(ActionEvent event) {
        try {
            // Load CategorieAssuranceGUI.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CategorieAssuranceGUI.fxml"));
            Parent root = loader.load();

            // Get the stage from the action event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showAlert("Error loading CategorieAssuranceGUI: " + e.getMessage());
        }
    }

    }

