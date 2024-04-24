package controllers.creditController;
import controllers.categorieCreditController.ListeCredits;
import entities.Credit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import entities.CategorieCredit;
import services.ServiceCategorieCredit;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;
import services.ServiceCredit;
import javafx.beans.property.SimpleStringProperty;

public class GetCreditPayed {
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
    private TableColumn<Credit, Void> supprimerColumn;

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
        supprimerColumn.setCellFactory(new Callback<TableColumn<Credit, Void>, TableCell<Credit, Void>>() {
            @Override
            public TableCell<Credit, Void> call(final TableColumn<Credit, Void> param) {
                return new TableCell<Credit, Void>() {
                    private final Button supprimerButton = new Button("Supprimer");
                    {
                        supprimerButton.setOnAction(event -> {
                            Credit credit = getTableView().getItems().get(getIndex());
                            handleSupprimer(credit);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(supprimerButton);
                            setAlignment(javafx.geometry.Pos.CENTER);
                        }
                    }
                };
            }
        });
        loadData();
    }
    private void handleSupprimer(Credit credit) {
        int creditId = credit.getId();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de supprimer ce crédit ?");
        alert.initStyle(StageStyle.UTILITY);
        ButtonType buttonTypeYes = new ButtonType("Oui");
        ButtonType buttonTypeNo = new ButtonType("Non");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                try {
                    service.delete(creditId);
                    loadData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void loadData() {
        try {
            List<Credit> credits = service.getCreditsPayed();
            creditsTable.getItems().setAll(credits);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
