package org.example;

import  Models.Virement;
import Services.VirementService;
import Models.CompteClient;
import Services.CompteClientService;
import java.sql.SQLException;
import java.util.List;
import java.sql.Time;
import java.sql.Date;

public class Main {

    public static void main(String[] args) {
        VirementService virementService = new VirementService();

        // Créer un nouveau virement
        try {
            Virement nouveauVirement = new Virement("compte1", "compte2", 500.0f, Date.valueOf("2024-04-23"), Time.valueOf("12:30:00"));
            virementService.add(nouveauVirement);
            System.out.println("Nouveau virement créé avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du virement : " + e.getMessage());
        }

        // Récupérer tous les virements
        try {
            List<Virement> tousLesVirements = virementService.getAll();
            System.out.println("Liste de tous les virements :");
            for (Virement virement : tousLesVirements) {
                System.out.println(virement);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des virements : " + e.getMessage());
        }

        // Mettre à jour un virement
        try {
            Virement virementAUpdate = virementService.getById(1); // Supposons que l'ID 1 existe
            if (virementAUpdate != null) {
                virementAUpdate.setMontant(700.0f);
                virementService.update(virementAUpdate);
                System.out.println("Virement mis à jour avec succès.");
            } else {
                System.err.println("Le virement avec l'ID spécifié n'existe pas.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du virement : " + e.getMessage());
        }

        // Supprimer un virement
        try {
            virementService.delete(1); // Supposons que l'ID 1 existe
            System.out.println("Virement supprimé avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du virement : " + e.getMessage());
        }
    }
}