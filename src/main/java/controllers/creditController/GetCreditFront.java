package controllers.creditController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.ServiceCredit;
import entities.Credit;

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

    public void initData(Credit credit) {
        this.credit = credit;
        creditIdLabel.setText(String.valueOf(credit.getId()));
        montantTotalLabel.setText(String.valueOf(credit.getMontantTotale()));
        dureeTotaleLabel.setText(String.valueOf(credit.getDureeTotale()));
        interetLabel.setText(String.valueOf(credit.getInteret()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateCLabel.setText(dateFormat.format(credit.getDateC()));
        categorieLabel.setText(String.valueOf(credit.getCategorie().getNom()));
    }
}
