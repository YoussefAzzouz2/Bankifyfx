package controllers.creditController;
import controllers.categorieCreditController.ListeCredits;
import controllers.categorieCreditController.ModifCategorieCredit;
import controllers.remboursementController.AddRemboursement;
import controllers.remboursementController.GetRemboursementFront;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import services.ServiceCredit;
import entities.Credit;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class GetCreditFront {
    private final ServiceCredit service = new ServiceCredit();
    private Credit credit;
    @FXML
    private Label creditIdLabel;
    @FXML
    private Label montantTotalLabel;
    @FXML
    private Label dureeTotaleLabel;
    @FXML
    private Label interetLabel;
    @FXML
    private Label dateCLabel;
    @FXML
    private Label categorieLabel;

    public void initData(int compteId) {
        try {
            this.credit = service.getByClient(compteId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        creditIdLabel.setText(String.valueOf(credit.getId()));
        montantTotalLabel.setText(String.valueOf(credit.getMontantTotale()));
        dureeTotaleLabel.setText(String.valueOf(credit.getDureeTotale()));
        interetLabel.setText(String.valueOf(credit.getInteret()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateCLabel.setText(dateFormat.format(credit.getDateC()));
        categorieLabel.setText(String.valueOf(credit.getCategorie().getNom()));
    }

    @FXML
    private void goToAddRemboursement(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/remboursementTemplates/addRemboursement.fxml"));
        Parent root = loader.load();
        AddRemboursement controller = loader.getController();
        controller.initData(credit);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToListeRemboursement(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/remboursementTemplates/getRemboursementFront.fxml"));
            Parent root = loader.load();
            GetRemboursementFront controller = loader.getController();
            controller.initData(credit);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des remboursements");
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void demanderCredit(ActionEvent event) throws IOException {
        try {
            if (!new ServiceCredit().clientExiste(1)==false) {
                System.out.println("Client existe");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Vous avez déjà un crédit en cours");
                alert.showAndWait();
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/creditTemplates/addCredit.fxml"));
                    Parent root = loader.load();
                    MenuItem menuItem = (MenuItem) event.getSource();
                    Scene scene = menuItem.getParentPopup().getOwnerWindow().getScene();
                    Stage stage = (Stage) scene.getWindow();
                    stage.setScene(new Scene(root));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void consulterCredit(ActionEvent event) throws IOException {
        try {
            if (new ServiceCredit().clientExiste(1)==false) {
                System.out.println("Client n'existe pas");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Vous n'avez pas un crédit");
                alert.showAndWait();
            } else if (new ServiceCredit().getByClient(1).isAccepted()==false) {
                System.out.println("Demande en cours");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Votre demande est en attente");
                alert.showAndWait();
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/creditTemplates/getCreditFront.fxml"));
                    Parent root = loader.load();
                    MenuItem menuItem = (MenuItem) event.getSource();
                    GetCreditFront controller = loader.getController();
                    controller.initData(1);
                    Scene scene = menuItem.getParentPopup().getOwnerWindow().getScene();
                    Stage stage = (Stage) scene.getWindow();
                    stage.setScene(new Scene(root));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}