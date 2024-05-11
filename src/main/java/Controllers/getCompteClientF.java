package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import Models.CompteClient;
import Services.CompteClientService;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class getCompteClientF {

    private final CompteClientService compteClientService = new CompteClientService();

    @FXML
    private TableView<CompteClient> compteClientTable;



    @FXML
    private TableColumn<CompteClient, String> nomColumn;

    @FXML
    private TableColumn<CompteClient, String> prenomColumn;

    @FXML
    private TableColumn<CompteClient, String> ribColumn;

    @FXML
    private TableColumn<CompteClient, String> mailColumn;

    @FXML
    private TableColumn<CompteClient, String> telColumn;

    @FXML
    private TableColumn<CompteClient, Float> soldeColumn;

    @FXML
    private TableColumn<CompteClient, String> typeCompteColumn;

    @FXML
    private TableColumn<CompteClient, String> packCompteColumn;


    @FXML
    private TableColumn<CompteClient, Void> modifyColumn;

    @FXML
    private TableColumn<CompteClient, Void> deleteColumn;

    @FXML
    public void initialize() {
        // Configure table columns
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ribColumn.setCellValueFactory(new PropertyValueFactory<>("rib"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("mail"));
        telColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));
        soldeColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
        typeCompteColumn.setCellValueFactory(new PropertyValueFactory<>("type_compte"));
        packCompteColumn.setCellValueFactory(new PropertyValueFactory<>("pack_compte"));


        // Configure "Modifier" column
        modifyColumn.setCellFactory(new Callback<TableColumn<CompteClient, Void>, TableCell<CompteClient, Void>>() {
            @Override
            public TableCell<CompteClient, Void> call(final TableColumn<CompteClient, Void> param) {
                return new TableCell<CompteClient, Void>() {
                    private final Button modifyButton = new Button("Modifier");

                    {
                        modifyButton.setOnAction(event -> {
                            CompteClient compteClient = getTableView().getItems().get(getIndex());
                            handleModify(compteClient);
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

        // Configure "Supprimer" column
        deleteColumn.setCellFactory(column -> new TableCell<CompteClient, Void>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    CompteClient compteClient = getTableView().getItems().get(getIndex());
                    handleDelete(compteClient);
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

        // Load data into the table
        loadData();
    }

    private void handleModify(CompteClient compteClient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modiCompte.fxml"));
            Parent root = loader.load();
            modiCompte controller = loader.getController();
            controller.initData(compteClient);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier le compte client");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions here...
        }
    }

    private void handleDelete(CompteClient compteClient) {
        try {
            int compteClientId = compteClient.getId();
            compteClientService.delete(compteClientId);
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions here...
        }
    }

    private void loadData() {
        try {
            List<CompteClient> compteClients = compteClientService.getAll();
            compteClientTable.getItems().setAll(compteClients);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions here...
        }
    }

    public void ajouterCompte(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutCompte.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Ajout Compte");
            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();

            // Close the current window
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void NewCompte(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouCompteF.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Ajouter un compte");
            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();

            // Close the current window
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}