package Utils;

import Models.CompteClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {

    public static Map<String, Integer> calculateTypeCompteStatistics(List<CompteClient> compteClients) {
        Map<String, Integer> typeCompteStatistics = new HashMap<>();

        // Initialize counters for each type_compte
        int compteATermeCount = 0;
        int compteCourantCount = 0;
        int compteEntrepriseCount = 0;
        int compteEpargneCount = 0;
        int compteEpargneRetraiteCount = 0;
        int comptePaiementMobileCount = 0;

        // Iterate through the compteClients and count occurrences of each type_compte
        for (CompteClient compteClient : compteClients) {
            String typeCompte = compteClient.getType_compte(); // Assuming type_compte is a String attribute
            switch (typeCompte) {
                case "Compte à terme":
                    compteATermeCount++;
                    break;
                case "Compte courant":
                    compteCourantCount++;
                    break;
                case "Compte d'entreprise":
                    compteEntrepriseCount++;
                    break;
                case "Compte d'épargne":
                    compteEpargneCount++;
                    break;
                case "Compte d'épargne retraite":
                    compteEpargneRetraiteCount++;
                    break;
                case "Compte de paiement mobile":
                    comptePaiementMobileCount++;
                    break;
                default:
                    // Handle unknown type_compte values if necessary
                    break;
            }
        }

        // Populate the map with type_compte statistics
        typeCompteStatistics.put("Compte à terme", compteATermeCount);
        typeCompteStatistics.put("Compte courant", compteCourantCount);
        typeCompteStatistics.put("Compte d'entreprise", compteEntrepriseCount);
        typeCompteStatistics.put("Compte d'épargne", compteEpargneCount);
        typeCompteStatistics.put("Compte d'épargne retraite", compteEpargneRetraiteCount);
        typeCompteStatistics.put("Compte de paiement mobile", comptePaiementMobileCount);

        return typeCompteStatistics;
    }

    public static Map<String, Integer> calculatePackCompteStatistics(List<CompteClient> compteClients) {
        Map<String, Integer> packCompteStatistics = new HashMap<>();

        // Initialize counters for each pack_compte
        int packConfortCount = 0;
        int packEssentielCount = 0;
        int packJeunesCount = 0;
        int packPremiumCount = 0;
        int packSeniorsCount = 0;

        // Iterate through the compteClients and count occurrences of each pack_compte
        for (CompteClient compteClient : compteClients) {
            String packCompte = compteClient.getPack_compte(); // Assuming pack_compte is a String attribute
            switch (packCompte) {
                case "Pack Confort":
                    packConfortCount++;
                    break;
                case "Pack Essentiel":
                    packEssentielCount++;
                    break;
                case "Pack Jeunes":
                    packJeunesCount++;
                    break;
                case "Pack Premium":
                    packPremiumCount++;
                    break;
                case "Pack Séniors":
                    packSeniorsCount++;
                    break;
                default:
                    // Handle unknown pack_compte values if necessary
                    break;
            }
        }

        // Populate the map with pack_compte statistics
        packCompteStatistics.put("Pack Confort", packConfortCount);
        packCompteStatistics.put("Pack Essentiel", packEssentielCount);
        packCompteStatistics.put("Pack Jeunes", packJeunesCount);
        packCompteStatistics.put("Pack Premium", packPremiumCount);
        packCompteStatistics.put("Pack Séniors", packSeniorsCount);

        return packCompteStatistics;
    }

}
