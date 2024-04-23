package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import Models.Virement;
import Services.VirementService;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class getVirement {

    private final VirementService virementService = new VirementService();

    @FXML
    private TableView<Virement> virementTable;

    @FXML
    private TableColumn<Virement, Integer> idColumn;

    @FXML
    private TableColumn<Virement, String> compteSourceColumn;

    @FXML
    private TableColumn<Virement, String> compteDestinationColumn;

    @FXML
    private TableColumn<Virement, Float> montantColumn;

    @FXML
    private TableColumn<Virement, String> dateColumn;

    @FXML
    private TableColumn<Virement, String> heureColumn;

    @FXML
    private TableColumn<Virement, Void> modifyColumn;

    @FXML
    private TableColumn<Virement, Void> deleteColumn;

    @FXML
    public void initialize() {
        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        compteSourceColumn.setCellValueFactory(new PropertyValueFactory<>("compte_source"));
        compteDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("compte_destination"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        heureColumn.setCellValueFactory(new PropertyValueFactory<>("heure"));

        // Configure "Modifier" column
        /*modifyColumn.setCellFactory(new Callback<TableColumn<Virement, Void>, TableCell<Virement, Void>>() {
            @Override
            public TableCell<Virement, Void> call(final TableColumn<Virement, Void> param) {
                return new TableCell<Virement, Void>() {
                    private final Button modifyButton = new Button("Modifier");

                    {
                        modifyButton.setOnAction(event -> {
                            Virement virement = getTableView().getItems().get(getIndex());
                            handleModify(virement);
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
*/
        // Configure "Supprimer" column
  /*      deleteColumn.setCellFactory(column -> new TableCell<Virement, Void>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Virement virement = getTableView().getItems().get(getIndex());
                    handleDelete(virement);
                });
            }*/

          /*  @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
*/
        // Load data into the table
        loadData();
    }

    private void handleModify(Virement virement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modiVirement.fxml"));
            Parent root = loader.load();
            //modiVirement controller = loader.getController();
           // controller.initData(virement);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier le virement");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions here...
        }
    }

    private void handleDelete(Virement virement) {
        try {
            int virementId = virement.getId();
            virementService.delete(virementId);
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions here...
        }
    }

    private void loadData() {
        try {
            List<Virement> virements = virementService.getAll();
            virementTable.getItems().setAll(virements);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions here...
        }
    }
}
