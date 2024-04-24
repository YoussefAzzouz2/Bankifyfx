package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import models.Carte;
import services.CarteService;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class getCarte {

    private final CarteService carteService = new CarteService();

    @FXML
    private TableView<Carte> carteTable;

    @FXML
    private TableColumn<Carte, Long> idColumn;

    @FXML
    private TableColumn<Carte, String> numColumn;

    @FXML
    private TableColumn<Carte, java.sql.Date> dateExpColumn;

    @FXML
    private TableColumn<Carte, String> typeColumn;

    @FXML
    private TableColumn<Carte, String> statutColumn;

    @FXML
    private TableColumn<Carte, Void> modifyColumn;

    @FXML
    private TableColumn<Carte, Void> deleteColumn;

    @FXML
    public void initialize() {
        // Configuration des colonnes de la table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        numColumn.setCellValueFactory(new PropertyValueFactory<>("num_c"));
        dateExpColumn.setCellValueFactory(new PropertyValueFactory<>("date_exp"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type_c"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut_c"));

        // Configurer la colonne "Modifier"
        modifyColumn.setCellFactory(new Callback<TableColumn<Carte, Void>, TableCell<Carte, Void>>() {
            @Override
            public TableCell<Carte, Void> call(final TableColumn<Carte, Void> param) {
                return new TableCell<Carte, Void>() {
                    private final Button modifyButton = new Button("Modifier");

                    {
                        modifyButton.setOnAction(event -> {
                            // Récupérer la carte de la ligne associée
                            Carte carte = getTableView().getItems().get(getIndex());
                            handleModify(carte);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(modifyButton);
                        }
                    }
                };
            }
        });

        // Configurer la colonne "Supprimer"
        deleteColumn.setCellFactory(column -> new TableCell<Carte, Void>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Carte carte = getTableView().getItems().get(getIndex());
                    handleDelete(carte);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        // Charger les données dans la table
        loadData();
    }

    private void handleModify(Carte carte) {
        try {
            // Charger le fichier FXML de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifCarte.fxml"));

            // Créer la scène de modification
            Parent root = loader.load();

            // Obtenir le contrôleur de modification et passer la carte à modifier
            modifCarte controller = loader.getController();
            controller.initData(carte);

            // Créer un nouveau stage pour la scène de modification
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier la carte");

            // Afficher la scène de modification
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérez les exceptions ici...
        }
    }

    private void handleDelete(Carte carte) {
        try {
            // Récupérer l'ID de la carte à supprimer
            long carteId = carte.getId();

            // Appeler la méthode `delete` de `CarteService` avec l'ID de la carte
            carteService.delete(carteId);

            // Recharger les données dans la table après suppression
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez les exceptions ici...
        }
    }

    private void loadData() {
        try {
            List<Carte> cartes = carteService.getAll();
            carteTable.getItems().setAll(cartes);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez les exceptions ici...
        }
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
