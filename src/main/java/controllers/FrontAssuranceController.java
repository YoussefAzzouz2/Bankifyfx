package controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import models.Assurance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FrontAssuranceController {

    @FXML
    private ImageView frontimg;

    @FXML
    private TableView<Assurance> assuranceTable;

    @FXML
    private TableColumn<Assurance, Integer> idAssuranceCol;

    @FXML
    private TableColumn<Assurance, String> typeAssuranceCol;

    @FXML
    private TableColumn<Assurance, Double> montantPrimeCol;

    @FXML
    private TableColumn<Assurance, String> nomAssureCol;

    @FXML
    private TableColumn<Assurance, String> nomBeneficiaireCol;

    @FXML
    private TableColumn<Assurance, String> infoAssuranceCol;

    private static final String URL = "jdbc:mysql://127.0.0.1/bankify";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @FXML
    void initialize() {
        populateTableView();
    }

    private void populateTableView() {
        ObservableList<Assurance> assuranceList = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM assurance")) {

            while (rs.next()) {
                Assurance assurance = new Assurance(
                        rs.getInt("id_assurance"),
                        rs.getString("type_assurance"),
                        rs.getDouble("montant_prime"),
                        rs.getString("nom_assure"),
                        rs.getString("nom_beneficiaire"),
                        rs.getString("info_assurance")
                );
                assuranceList.add(assurance);
            }

            idAssuranceCol.setCellValueFactory(new PropertyValueFactory<>("idAssurance"));
            typeAssuranceCol.setCellValueFactory(new PropertyValueFactory<>("typeAssurance"));
            montantPrimeCol.setCellValueFactory(new PropertyValueFactory<>("montantPrime"));
            nomAssureCol.setCellValueFactory(new PropertyValueFactory<>("nomAssure"));
            nomBeneficiaireCol.setCellValueFactory(new PropertyValueFactory<>("nomBeneficiaire"));
            infoAssuranceCol.setCellValueFactory(new PropertyValueFactory<>("infoAssurance"));

            assuranceTable.setItems(assuranceList);

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception properly in your application
        }
    }
}
