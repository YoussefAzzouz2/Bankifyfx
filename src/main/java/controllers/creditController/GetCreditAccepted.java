package controllers.creditController;
import controllers.categorieCreditController.ListeCredits;
import controllers.remboursementController.GetRemboursementBack;
import controllers.remboursementController.GetRemboursementFront;
import entities.Credit;
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
import services.ServiceCredit;
import javafx.beans.property.SimpleStringProperty;

public class GetCreditAccepted {
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
    private final ServiceCredit service = new ServiceCredit();

    @FXML
    private TableView<Credit> creditsTable;

    @FXML
    private TableColumn<Credit, Integer> idColumn;

    @FXML
    private TableColumn<Credit, Double> montantTotaleColumn;

    @FXML
    private TableColumn<Credit, Integer> dureeTotaleColumn;

    @FXML
    private TableColumn<Credit, Integer> interetColumn;

    @FXML
    private TableColumn<Credit, java.sql.Date> dateCColumn;

    @FXML
    private TableColumn<Credit, String> categorieColumn;

    @FXML
    private TableColumn<Credit, Void> remboursementsColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        montantTotaleColumn.setCellValueFactory(new PropertyValueFactory<>("montantTotale"));
        dureeTotaleColumn.setCellValueFactory(new PropertyValueFactory<>("dureeTotale"));
        interetColumn.setCellValueFactory(new PropertyValueFactory<>("interet"));
        dateCColumn.setCellValueFactory(new PropertyValueFactory<>("dateC"));
        categorieColumn.setCellValueFactory(cellData -> {
            Credit credit = cellData.getValue();
            if (credit != null && credit.getCategorie() != null) {
                return new SimpleStringProperty(credit.getCategorie().getNom());
            } else {
                return new SimpleStringProperty("");
            }
        });
        remboursementsColumn.setCellFactory(new Callback<TableColumn<Credit, Void>, TableCell<Credit, Void>>() {
            @Override
            public TableCell<Credit, Void> call(final TableColumn<Credit, Void> param) {
                return new TableCell<Credit, Void>() {
                    private final Button remboursementsButton = new Button("Suivre ses remboursement");

                    {
                        remboursementsButton.setOnAction(event -> {
                            Credit credit = getTableView().getItems().get(getIndex());
                            handleRemboursements(credit);
                        });
                        remboursementsButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(remboursementsButton);
                            setAlignment(javafx.geometry.Pos.CENTER);
                        }
                    }
                };
            }
        });
        loadData();
    }
    private void handleRemboursements(Credit credit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/remboursementTemplates/getRemboursementBack.fxml"));
            Parent root = loader.load();
            GetRemboursementBack controller = loader.getController();
            controller.initData(credit);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des remboursements");
            stage.showAndWait();
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadData() {
        try {
            List<Credit> credits = service.getCreditsAccepted();
            creditsTable.getItems().setAll(credits);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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