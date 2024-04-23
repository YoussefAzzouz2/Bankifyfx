package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import models.Transaction;
import services.TransactionService;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class getTransaction {

    private final TransactionService transactionService = new TransactionService();

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, Long> idColumn;

    @FXML
    private TableColumn<Transaction, Double> montantColumn;

    @FXML
    private TableColumn<Transaction, java.sql.Date> dateColumn;

    @FXML
    private TableColumn<Transaction, String> typeColumn;

    @FXML
    private TableColumn<Transaction, String> statutColumn;

    @FXML
    private TableColumn<Transaction, Void> modifyColumn;

    @FXML
    private TableColumn<Transaction, Void> deleteColumn;

    @FXML
    public void initialize() {
        // Configuration des colonnes de la table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_t"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type_t"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut_t"));

        // Configurer la colonne "Modify"
        modifyColumn.setCellFactory(new Callback<TableColumn<Transaction, Void>, TableCell<Transaction, Void>>() {
            @Override
            public TableCell<Transaction, Void> call(final TableColumn<Transaction, Void> param) {
                return new TableCell<Transaction, Void>() {
                    private final Button modifyButton = new Button("Modifier");

                    {
                        modifyButton.setOnAction(event -> {
                            // Récupérer la transaction de la ligne associée
                            Transaction transaction = getTableView().getItems().get(getIndex());
                            handleModify(transaction);
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
        // Configurer la colonne "Delete"
        deleteColumn.setCellFactory(column -> new TableCell<Transaction, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Transaction transaction = getTableView().getItems().get(getIndex());
                    handleDelete(transaction);
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

    private void handleModify(Transaction transaction) {
        try {
            // Charger le fichier FXML de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifTransaction.fxml"));

            // Créer la scène de modification
            Parent root = loader.load();

            // Obtenir le contrôleur de modification et passer la transaction à modifier
            modifTransaction controller = loader.getController();
            controller.initData(transaction);

            // Créer un nouveau stage pour la scène de modification
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier la transaction");

            // Afficher la scène de modification
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérez les exceptions ici...
        }
    }

    private void handleDelete(Transaction transaction) {
        try {
            // Récupérer l'ID de la transaction à supprimer
            long transactionId = transaction.getId();

            // Appeler la méthode delete de TransactionService avec l'ID de la transaction
            transactionService.delete(transactionId);

            // Recharger les données dans la table après suppression
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez les exceptions ici...
        }
    }


    private void loadData() {
        try {
            List<Transaction> transactions = transactionService.getAll();
            transactionTable.getItems().setAll(transactions);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez les exceptions ici...
        }
    }
}
