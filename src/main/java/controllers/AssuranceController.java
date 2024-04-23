package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class AssuranceController {

    @FXML
    private TableView<Assurance> assuranceTable;
    @FXML
    private TextField infoTF;

    @FXML
    private TextField montantTF;

    @FXML
    private TextField nomassureTF;

    @FXML
    private TextField nombenefTF;


    @FXML
    private TextField typeTF;
    @FXML
    private TableColumn<Assurance, String> typeAssuranceColumn;

    @FXML
    private TableColumn<Assurance, String> nomAssureColumn;

    @FXML
    private TableColumn<Assurance, String> nomBeneficiaireColumn;

    @FXML
    private TableColumn<Assurance, String> montantPrimeColumn;

    @FXML
    private TableColumn<Assurance, String> infoAssuranceColumn;


    // Database connection details
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/bankify";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    void addButton(ActionEvent event) {
        String type = typeTF.getText();
        String nomAssure = nomassureTF.getText();
        String nomBeneficiaire = nombenefTF.getText();
        String montantPrime = montantTF.getText();
        String infoAssurance = infoTF.getText();

        // Validate input
        if (type.length() < 3 || nomAssure.length() < 3 || nomBeneficiaire.length() < 3) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Fields must have at least 3 characters.");
            alert.showAndWait();
            return;
        }

        try {
            double montant = Double.parseDouble(montantPrime);
            if (montant <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Montant prime must be a valid number greater than 0.");
            alert.showAndWait();
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO assurance (type_assurance, nom_assure, nom_beneficiaire, montant_prime, info_assurance) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, type);
            stmt.setString(2, nomAssure);
            stmt.setString(3, nomBeneficiaire);
            stmt.setDouble(4, Double.parseDouble(montantPrime));
            stmt.setString(5, infoAssurance);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                // Refresh TableView by updating the existing data
                assuranceTable.setItems(loadDataIntoTableView());
            }
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error adding record: " + e.getMessage());
            alert.showAndWait();
        }
    }


    private ObservableList<Assurance> loadDataIntoTableView() {
        ObservableList<Assurance> data = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM assurance";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Assurance assurance = new Assurance(
                        rs.getString("type_assurance"),
                        rs.getString("nom_assure"),
                        rs.getString("nom_beneficiaire"),
                        rs.getString("montant_prime"),
                        rs.getString("info_assurance")
                );
                data.add(assurance);
            }

            // Assuming you have a TableView named assuranceTable
            assuranceTable.setItems(data);

        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error fetching records: " + e.getMessage());
            alert.showAndWait();
        }
        return data;
    }


    @FXML
    void showButton(ActionEvent event) {
        ObservableList<Assurance> data = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM assurance";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Assurance assurance = new Assurance(
                        rs.getString("type_assurance"),
                        rs.getString("nom_assure"),
                        rs.getString("nom_beneficiaire"),
                        rs.getString("montant_prime"),
                        rs.getString("info_assurance")
                );
                data.add(assurance);
            }

            typeAssuranceColumn.setCellValueFactory(new PropertyValueFactory<>("typeAssurance"));
            nomAssureColumn.setCellValueFactory(new PropertyValueFactory<>("nomAssure"));
            nomBeneficiaireColumn.setCellValueFactory(new PropertyValueFactory<>("nomBeneficiaire"));
            montantPrimeColumn.setCellValueFactory(new PropertyValueFactory<>("montantPrime"));
            infoAssuranceColumn.setCellValueFactory(new PropertyValueFactory<>("infoAssurance"));

            assuranceTable.setItems(data);

        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error fetching records: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void goToFrontAssuranceButtonClicked(ActionEvent actionEvent) {
    }

    public static class Assurance {
        private final String typeAssurance;
        private final String nomAssure;
        private final String nomBeneficiaire;
        private final String montantPrime;
        private final String infoAssurance;

        public Assurance(String typeAssurance, String nomAssure, String nomBeneficiaire, String montantPrime, String infoAssurance) {
            this.typeAssurance = typeAssurance;
            this.nomAssure = nomAssure;
            this.nomBeneficiaire = nomBeneficiaire;
            this.montantPrime = montantPrime;
            this.infoAssurance = infoAssurance;
        }

        public String getTypeAssurance() {
            return typeAssurance;
        }

        public String getNomAssure() {
            return nomAssure;
        }

        public String getNomBeneficiaire() {
            return nomBeneficiaire;
        }

        public String getMontantPrime() {
            return montantPrime;
        }

        public String getInfoAssurance() {
            return infoAssurance;
        }
    }

    @FXML
    void updateButton(ActionEvent event) {
        Assurance selectedAssurance = assuranceTable.getSelectionModel().getSelectedItem();
        if (selectedAssurance == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Selection Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a row to update.");
            alert.showAndWait();
            return;
        }

        String type = typeTF.getText();
        String nomAssure = nomassureTF.getText();
        String nomBeneficiaire = nombenefTF.getText();
        String montantPrime = montantTF.getText();
        String infoAssurance = infoTF.getText();

        // Validate input
        if (type.length() < 3 || nomAssure.length() < 3 || nomBeneficiaire.length() < 3) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Fields must have at least 3 characters.");
            alert.showAndWait();
            return;
        }

        try {
            double montant = Double.parseDouble(montantPrime);
            if (montant <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Montant prime must be a valid number greater than 0.");
            alert.showAndWait();
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE assurance SET type_assurance=?, nom_assure=?, nom_beneficiaire=?, montant_prime=?, info_assurance=? WHERE type_assurance=?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, type);
            stmt.setString(2, nomAssure);
            stmt.setString(3, nomBeneficiaire);
            stmt.setDouble(4, Double.parseDouble(montantPrime));
            stmt.setString(5, infoAssurance);
            stmt.setString(6, selectedAssurance.getTypeAssurance()); // Assuming typeAssurance is unique, adjust accordingly if not

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                // Refresh TableView by updating the existing data
                assuranceTable.setItems(loadDataIntoTableView());
            }
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error updating record: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void deleteButton(ActionEvent event) {
        // Get the selected item from the TableView
        Assurance selectedAssurance = assuranceTable.getSelectionModel().getSelectedItem();

        if (selectedAssurance == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a record to delete.");
            alert.showAndWait();
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM assurance WHERE type_assurance = ? AND nom_assure = ? AND nom_beneficiaire = ? AND montant_prime = ? AND info_assurance = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, selectedAssurance.getTypeAssurance());
            stmt.setString(2, selectedAssurance.getNomAssure());
            stmt.setString(3, selectedAssurance.getNomBeneficiaire());
            stmt.setString(4, selectedAssurance.getMontantPrime());
            stmt.setString(5, selectedAssurance.getInfoAssurance());

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                // Remove the selected item from the TableView
                assuranceTable.getItems().remove(selectedAssurance);

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Record deleted successfully.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error deleting record.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error deleting record: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void goToAgenceButtonClicked(ActionEvent event) {
        try {
            // Load the Agence GUI FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AgenceGUI.fxml"));
            Parent root = loader.load();

            // Create a new stage for the Agence GUI
            Stage stage = new Stage();
            stage.setTitle("Agence Form");
            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();

            // Close the current window
            ((Node) (event.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error loading AgenceGUI: " + e.getMessage());
            alert.showAndWait();
        }
    }


}

