package org.example;

import models.Carte;
import services.CarteService;

import models.Transaction;
import services.TransactionService;

import java.sql.SQLException;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        CarteService carteService = new CarteService();
        TransactionService transactionService = new TransactionService();







        try {
            // Get the existing carte by id
            Carte existingCarte = carteService.getById(15);
            if (existingCarte != null) {
                // Update the necessary attributes
                existingCarte.setNum_c("888888888");
                existingCarte.setType_c("Debit");
                existingCarte.setStatut_c("Inactive");
                // Update the carte in the database
                carteService.update(existingCarte);
            } else {
                System.err.println("Carte with id 1 not found");
            }
        } catch (SQLException e) {
            System.err.println("Error updating carte: " + e.getMessage());
        }

        try {
            System.out.println(carteService.getAll());
        } catch (SQLException e) {
            System.err.println("Error getting all cartes: " + e.getMessage());
        }

        try {
            System.out.println(carteService.getById(1));
        } catch (SQLException e) {
            System.err.println("Error getting carte by id: " + e.getMessage());
        }



        try {
            // Get the existing transaction by id
            Transaction existingTransaction = transactionService.getById(1);
            if (existingTransaction != null) {
                // Update the necessary attributes
                existingTransaction.setMontant(2000);
                existingTransaction.setType_t("Debit");
                existingTransaction.setStatut_t("Completed");
                // Update the transaction in the database
                transactionService.update(existingTransaction);
                System.out.println("Transaction updated successfully!");
            } else {
                System.err.println("Transaction with id 1 not found");
            }
        } catch (SQLException e) {
            System.err.println("Error updating transaction: " + e.getMessage());
        }

        try {
            System.out.println(transactionService.getAll());
        } catch (SQLException e) {
            System.err.println("Error getting all transactions: " + e.getMessage());
        }

        try {
            System.out.println(transactionService.getById(1));
        } catch (SQLException e) {
            System.err.println("Error getting transaction by id: " + e.getMessage());
        }
    }
}