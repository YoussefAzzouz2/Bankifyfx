package controllers.categorieCreditController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import entities.CategorieCredit;
import services.ServiceCategorieCredit;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;
public class GetCategorieCredit {
    @FXML
    private VBox pnItems = null;
    @FXML
    private Button btnOverview;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;
    private final ServiceCategorieCredit service = new ServiceCategorieCredit();

    @FXML
    private TableView<CategorieCredit> categoriesTable;

    @FXML
    private TableColumn<CategorieCredit, Integer> idColumn;

    @FXML
    private TableColumn<CategorieCredit, String> nomColumn;

    @FXML
    private TableColumn<CategorieCredit, Double> minMontantColumn;

    @FXML
    private TableColumn<CategorieCredit, Double> maxMontantColumn;

    @FXML
    private TableColumn<CategorieCredit, Void> modifyColumn;

    @FXML
    private TableColumn<CategorieCredit, Void> deleteColumn;

    @FXML
    private TableColumn<CategorieCredit, Void> creditsColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        minMontantColumn.setCellValueFactory(new PropertyValueFactory<>("minMontant"));
        maxMontantColumn.setCellValueFactory(new PropertyValueFactory<>("maxMontant"));
        modifyColumn.setCellFactory(new Callback<TableColumn<CategorieCredit, Void>, TableCell<CategorieCredit, Void>>() {
            @Override
            public TableCell<CategorieCredit, Void> call(final TableColumn<CategorieCredit, Void> param) {
                return new TableCell<CategorieCredit, Void>() {
                    private final Button modifyButton = new Button("Modifier");

                    {
                        modifyButton.setOnAction(event -> {
                            CategorieCredit categorie = getTableView().getItems().get(getIndex());
                            handleModify(categorie);
                        });
                        modifyButton.setStyle("-fx-background-color: #800080; -fx-text-fill: white; -fx-font-weight: bold;");
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(modifyButton);
                            setAlignment(javafx.geometry.Pos.CENTER);
                        }
                    }
                };
            }
        });
        creditsColumn.setCellFactory(new Callback<TableColumn<CategorieCredit, Void>, TableCell<CategorieCredit, Void>>() {
            @Override
            public TableCell<CategorieCredit, Void> call(final TableColumn<CategorieCredit, Void> param) {
                return new TableCell<CategorieCredit, Void>() {
                    private final Button creditsButton = new Button("Liste des credits");
                    {
                        creditsButton.setOnAction(event -> {
                            CategorieCredit categorie = getTableView().getItems().get(getIndex());
                            handleCredits(categorie);
                        });
                        creditsButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(creditsButton);
                            setAlignment(javafx.geometry.Pos.CENTER);
                        }
                    }
                };
            }
        });
        deleteColumn.setCellFactory(column -> new TableCell<CategorieCredit, Void>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    CategorieCredit categorie = getTableView().getItems().get(getIndex());
                    handleDelete(categorie);
                });
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    setAlignment(javafx.geometry.Pos.CENTER);
                }
            }
        });
        loadData();
    }

    private void handleModify(CategorieCredit categorie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/categorieCreditTemplates/modifCategorieCredit.fxml"));
            Parent root = loader.load();
            ModifCategorieCredit controller = loader.getController();
            controller.initData(categorie);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier la categorie");
            stage.showAndWait();
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(CategorieCredit categorie) {
            int categorieId = categorie.getId();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de supprimer cette catégorie ?");
            alert.initStyle(StageStyle.UTILITY);
            ButtonType buttonTypeYes = new ButtonType("Oui");
            ButtonType buttonTypeNo = new ButtonType("Non");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeYes) {
                    try {
                        service.delete(categorieId);
                        loadData();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    private void handleCredits(CategorieCredit categorie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/categorieCreditTemplates/listeCredits.fxml"));
            Parent root = loader.load();
            ListeCredits controller = loader.getController();
            controller.initData(categorie);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des credits");
            stage.showAndWait();
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            List<CategorieCredit> categories = service.getAll();
            categoriesTable.getItems().setAll(categories);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAdd(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/categorieCreditTemplates/addCategorieCredit.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Ajouter une categorie");
        stage.showAndWait();
        loadData();
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnCustomers) {
            try {
                // Load aff.fxml
                FXMLLoader loader = new FXMLLoader();
                URL affFXMLUrl = getClass().getResource("/aff.fxml");
                loader.setLocation(affFXMLUrl);
                Pane affPane = loader.load();

                // Set the loaded pane as the background of pnlCustomer
                pnlCustomer.getChildren().setAll(affPane.getChildren());
                pnlCustomer.toFront();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
        if (actionEvent.getSource() == btnMenus) {
            pnlMenus.setStyle("-fx-background-color : #53639F");
            pnlMenus.toFront();
        }
        if (actionEvent.getSource() == btnOverview) {
            pnlOverview.setStyle("-fx-background-color : #02030A");
            pnlOverview.toFront();
        }
        if(actionEvent.getSource()==btnOrders)
        {
            pnlOrders.setStyle("-fx-background-color : #464F67");
            pnlOrders.toFront();
        }


    }
    @FXML
    private void goToBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/back.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void goToCategorie(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/categorieCreditTemplates/getCategorieCredit.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}