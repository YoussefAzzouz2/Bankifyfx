package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.Transaction;
import services.TransactionService;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class addTransaction {

    private final TransactionService transactionService = new TransactionService();

    @FXML
    private TextField montantTF;

    @FXML
    private TextField dateTF;

    @FXML
    private TextField typeTF;

    @FXML
    private TextField statutTF;

    @FXML
    void addTransaction(ActionEvent event) throws SQLException {
        if (montantTF.getText().isEmpty()) {
            System.out.println("Montant is empty");
        } else if (dateTF.getText().isEmpty()) {
            System.out.println("Date is empty");
        } else {
            try {
                double montant = Double.parseDouble(montantTF.getText());
                // Parse date string to java.util.Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(dateTF.getText());

                String type = typeTF.getText();
                String statut = statutTF.getText();

                Transaction transaction = new Transaction(montant, date, type, statut);
                transactionService.add(transaction);
                System.out.println("Transaction added successfully!");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format for montant");
            } catch (ParseException e) {
                System.out.println("Invalid date format for date");
            }
        }
    }
}
