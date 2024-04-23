package tn.esprit.bankify;

import tn.esprit.bankify.services.ServiceUser;
import tn.esprit.bankify.utils.MyDatabase;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        /*ServiceUser serviceUser = new ServiceUser();

        // Creating User objects with appropriate fields
        User p1 = new User(1, "Mohamed", "bensalah", "mohamed@gmail.com", "12345", new Date(), "Male");
        User p2 = new User(2, "Saif", "kallela", "saif@gmail.com", "12345", new Date(), "Male");

        try {
            // Adding users
            serviceUser.ajouter(p1);
            serviceUser.ajouter(p2);

            // Modifying a user (you might want to modify a different user)
            serviceUser.modifier(p1);

            // Displaying all users
            System.out.println(serviceUser.afficher());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }*/

        MyDatabase.getInstance();
        ServiceUser su = new ServiceUser();
        System.out.println(su.afficherUtilisateurs());
    }
}
