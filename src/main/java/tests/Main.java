package tests;

import entities.*;
import services.*;
import java.sql.SQLException;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        ServiceCredit services = new ServiceCredit();
        try {
            CompteClient client = new CompteClient(1,"Ghazouani","Samer","aaaaaaaaaaaa","aaaaaaaaaaaaaaaaa","+21628352443",10000.0);
            CategorieCredit categorie = new CategorieCredit(1,"prêt immobilier",10000.0,100000.0);
            Credit credit = new Credit(10,36,50000.0,new Date(),false,false,client,categorie);
            Remboursement remboursement = new Remboursement(31,40,100.0,700,new Date(),credit);
            services.add(credit);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
