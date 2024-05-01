package controllers.creditController;
import controllers.categorieCreditController.GetCategorieCredit;
import controllers.categorieCreditController.ListeCredits;
import controllers.categorieCreditController.ModifCategorieCredit;
import entities.CategorieCredit;
import entities.CompteClient;
import entities.Credit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import services.ServiceCategorieCredit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.ServiceCredit;

import java.util.List;

public class AddCredit {
    private final ServiceCredit service = new ServiceCredit();
    private final ServiceCategorieCredit serviceCategorie = new ServiceCategorieCredit();
    private Credit credit = new Credit();
    @FXML
    private TextField montantTF;

    @FXML
    private RadioButton option1RadioButton;

    @FXML
    private RadioButton option2RadioButton;

    @FXML
    private RadioButton option3RadioButton;

    @FXML
    private ToggleGroup toggleGroup;

    @FXML
    private ComboBox<String> categorieComboBox;

    public void initialize() {
        try {
            toggleGroup = new ToggleGroup();
            option1RadioButton.setToggleGroup(toggleGroup);
            option2RadioButton.setToggleGroup(toggleGroup);
            option3RadioButton.setToggleGroup(toggleGroup);
            toggleGroup.selectedToggleProperty().getValue();
            option1RadioButton.setSelected(true);
            credit.setInteret(10);
            credit.setDureeTotale(36);
            List<CategorieCredit> categories = serviceCategorie.getAll();
            ArrayList<String> noms = new ArrayList<>();
            for (CategorieCredit categorie : categories)
                noms.add(categorie.getNom());
            ObservableList<String> list = FXCollections.observableArrayList(noms);
            categorieComboBox.setItems(list);
            if (!categories.isEmpty())
                categorieComboBox.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addCredit(ActionEvent event) throws SQLException{
        credit.setDateC(new Date());
        credit.setAccepted(false);
        credit.setPayed(false);
        CompteClient compte = new CompteClient(1,"Ghazouani","Samer","aaaaaaaaaaaa","aaaaaaaaaaaaaaaaa","+21628352443",10000.0);
        credit.setCompte(compte);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton selectedRadioButton = (RadioButton) newValue;
            if (selectedRadioButton == option1RadioButton) {
                credit.setInteret(10);
                credit.setDureeTotale(36);
            } else if (selectedRadioButton == option2RadioButton) {
                credit.setInteret(15);
                credit.setDureeTotale(48);
            } else if (selectedRadioButton == option3RadioButton) {
                credit.setInteret(20);
                credit.setDureeTotale(60);
            }
        });
        if (categorieComboBox.getSelectionModel().getSelectedItem() != null)
            credit.setCategorie(serviceCategorie.getByNom(categorieComboBox.getSelectionModel().getSelectedItem()));

        if (montantTF.getText().isEmpty()) {
            System.out.println(service.clientExiste(credit.getCompte().getId()));
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
        } else if (Double.parseDouble(montantTF.getText())<credit.getCategorie().getMinMontant() || Double.parseDouble(montantTF.getText())>credit.getCategorie().getMaxMontant()) {
            System.out.println("Montant impossible");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le montant doit être entre "+credit.getCategorie().getMinMontant()+" et "+credit.getCategorie().getMaxMontant()+" pour la categorie "+credit.getCategorie().getNom());
            alert.showAndWait();
        } else if (!service.clientExiste(credit.getCompte().getId())==false) {
            System.out.println("Client existe");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Vous avez déjà un crédit en cours");
            alert.showAndWait();
        } else {
            credit.setMontantTotale(Double.parseDouble(montantTF.getText()));
            service.add(credit);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
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