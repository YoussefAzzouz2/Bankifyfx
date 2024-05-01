package tn.esprit.bankify.controllers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.bankify.controllers.AjouterController;
import tn.esprit.bankify.entities.User;
import tn.esprit.bankify.services.ServiceUser;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AffController implements Initializable, ModifierController.OnUserModifiedListener, AjouterController.OnUserAddedListener {
    ServiceUser sp = new ServiceUser();

    @FXML
    private Button btnSupprimer;

    @FXML
    private TableColumn<User, String> dnclm;

    @FXML
    private TableColumn<User, String> emclm;

    @FXML
    private TableColumn<User, String> grclm;

    @FXML
    private TableColumn<User, String> nmclm;

    @FXML
    private TableColumn<User, String> pmclm;

    @FXML
    private TableView<User> table;

    @FXML
    void supprimer(ActionEvent event) {
        User u = table.getSelectionModel().getSelectedItem();
        if (u != null) {
            sp.Supprimer(u);
            loadUsers();
        } else {
            // Handle the case where no user is selected
            System.out.println("No user selected to delete");
        }
    }


    @Override
    public void onUserModified(User modifiedUser) {
        loadUsers();
    }

    @Override
    public void onUserAdded(User newUser) {
        loadUsers();
    }

    private void loadUsers() {
        ObservableList<User> listef = sp.afficherUtilisateurs();
        nmclm.setCellValueFactory(new PropertyValueFactory<>("nom"));
        pmclm.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emclm.setCellValueFactory(new PropertyValueFactory<>("email"));
        grclm.setCellValueFactory(new PropertyValueFactory<>("genre"));
        dnclm.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        table.setItems(listef);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        loadUsers();
        // Add double-click event handler to table rows
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Check for double-click
                User selectedUser = table.getSelectionModel().getSelectedItem();
                openModifier(selectedUser);
            }
        });
    }

    private void openModifier(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifier.fxml"));
            Parent root = loader.load();

            ModifierController modifierController = loader.getController();
            modifierController.setUser(user); // Pass the selected user to ModifierController
            modifierController.setOnUserModifiedListener(this); // Set AffController as the listener

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Bankify");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ouvrirAjouter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouter.fxml"));
            Parent root = loader.load();

            AjouterController ajouterController = loader.getController();
            ajouterController.setOnUserAddedListener(this); // Set AffController as the listener

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Bankify");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
