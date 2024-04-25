package controllers.remboursementController;
import controllers.categorieCreditController.ModifCategorieCredit;
import controllers.creditController.GetCreditFront;
import entities.CategorieCredit;
import entities.Credit;
import entities.Remboursement;
import javafx.scene.control.Alert;
import services.ServiceCategorieCredit;
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
}
