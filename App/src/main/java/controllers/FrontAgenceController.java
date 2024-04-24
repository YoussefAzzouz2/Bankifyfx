package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.stage.Stage;
import models.Agence;

public class FrontAgenceController {

    private static final String URL = "jdbc:mysql://127.0.0.1/bankify";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @FXML
    private TableView<Agence> agenceTable;

    @FXML
    private ImageView frontimg;

    // ObservableList to hold Agence objects
    private ObservableList<Agence> agences = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize the TableView columns
        TableColumn<Agence, Integer> idCol = new TableColumn<>("ID");
        TableColumn<Agence, String> nomAgenceCol = new TableColumn<>("Nom Agence");
        TableColumn<Agence, String> emailAgenceCol = new TableColumn<>("Email Agence");
        TableColumn<Agence, String> telAgenceCol = new TableColumn<>("Tel Agence");

        // Define how to get values from Agence properties
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomAgenceCol.setCellValueFactory(new PropertyValueFactory<>("nomAgence"));
        emailAgenceCol.setCellValueFactory(new PropertyValueFactory<>("emailAgence"));
        telAgenceCol.setCellValueFactory(new PropertyValueFactory<>("telAgence"));

        // Add columns to TableView
        agenceTable.getColumns().addAll(idCol, nomAgenceCol, emailAgenceCol, telAgenceCol);

        // Populate TableView with data
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String query = "SELECT * FROM Agence";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Agence agence = new Agence(
                        rs.getInt("id"),
                        rs.getString("nom_agence"),
                        rs.getString("email_agence"),
                        rs.getString("tel_agence")
                );
                agences.add(agence);
            }

            // Close the connections
            rs.close();
            stmt.close();
            conn.close();

            // Set items to TableView
            agenceTable.setItems(agences);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions properly in your application
        }
    }

    @FXML
    public void goToFrontAssuranceButtonClicked(ActionEvent actionEvent) {
        try {
            // Load the Assurance GUI FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontAssuranceGUI.fxml"));
            Parent root = loader.load();

            // Create a new stage for the Assurance GUI
            Stage stage = new Stage();
            stage.setTitle("Assurance Form");
            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();

            // Close the current window if needed
            // You can uncomment the following lines if you want to close the current window
             ((Node) actionEvent.getSource()).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle any IOException properly in your application
        }
    }

    public void goToFrontCategorieButtonClicked(ActionEvent actionEvent) {
        try {
            // Load the FrontAgence GUI FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontCategorieGUI.fxml"));
            Parent root = loader.load();

            // Create a new stage for the FrontAgence GUI
            Stage stage = new Stage();
            stage.setTitle("Front Categorie");
            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();

            // Close the current window
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception properly in your application
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error loading FrontCategorieGUI: " + e.getMessage());
            alert.showAndWait();
        }
    }}

