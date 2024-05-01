package controllers.remboursementController;
import controllers.categorieCreditController.ModifCategorieCredit;
import controllers.creditController.GetCreditFront;
import entities.CategorieCredit;
import entities.Credit;
import entities.Remboursement;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import services.ServiceCategorieCredit;
import services.ServiceCredit;
import services.ServiceRemboursement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
public class AddRemboursement {
    private final ServiceRemboursement service = new ServiceRemboursement();
    private Credit credit;
    @FXML
    private TextField montantTF;

    public void initData(Credit credit) {
        this.credit = credit;
    }

    @FXML
    void addRemboursement(ActionEvent event) throws SQLException {
        if(montantTF.getText().isEmpty())
        {
            System.out.println("Pas de montant saisie");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez saisir un montant");
            alert.showAndWait();
        } else if (!montantTF.getText().matches("[0-9.]+")) {
            System.out.println("Montant impossible");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le montant doit être en chiffre");
            alert.showAndWait();
        } else if ((credit.getInteret()==10 && Double.parseDouble(montantTF.getText())<500)||(credit.getInteret()==15 && (Double.parseDouble(montantTF.getText())<300 || Double.parseDouble(montantTF.getText())>499))||(credit.getInteret()==20 && (Double.parseDouble(montantTF.getText())<100 || Double.parseDouble(montantTF.getText())>299))) {
            System.out.println("Montant impossible");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le montant saisie n'est pas compatible avec l'interet");
            alert.showAndWait();
        } else {
            try{
                double montantRestant= credit.getMontantTotale()-Double.parseDouble(montantTF.getText());
                Date dateR = new Date();
                LocalDate localDate1 = dateR.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate localDate2 = new java.util.Date(credit.getDateC().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                Period period = Period.between(localDate1, localDate2);
                int diffInMonths = period.getMonths();
                Remboursement remboursement = new Remboursement(credit.getDureeTotale()-diffInMonths,Double.parseDouble(montantTF.getText()),montantRestant,new Date(),credit);
                service.add(remboursement);
                service.sendSms(remboursement);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/creditTemplates/getCreditFront.fxml"));
                Parent root = loader.load();
                GetCreditFront controller = loader.getController();
                controller.initData(credit.getCompte().getId());
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
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