package tn.esprit.bankify.services;

import tn.esprit.bankify.entities.User;
import tn.esprit.bankify.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServiceUser implements IService<User> {
    Connection connection;
    Statement ste;

    public ServiceUser() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void Ajouter(User t) {
        try {
            String req = "INSERT INTO user (nom, prenom, email, password, dateNaissance, genre) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, t.getNom());
            preparedStatement.setString(2, t.getPrenom());
            preparedStatement.setString(3, t.getEmail());
            preparedStatement.setString(4, t.getPassword());
            preparedStatement.setDate(5, new java.sql.Date(t.getDateNaissance().getTime()));
            preparedStatement.setString(6, t.getGenre());
            preparedStatement.executeUpdate();
            System.out.println("User ajouté");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void Modifier(User u) {
        String req = "UPDATE user SET nom=?, prenom=?, email=?, dateNaissance=?, genre=? WHERE id=?";
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, u.getNom());
            preparedStatement.setString(2, u.getPrenom());
            preparedStatement.setString(3, u.getEmail());
            preparedStatement.setDate(4, new java.sql.Date(u.getDateNaissance().getTime()));
            preparedStatement.setString(5, u.getGenre());
            preparedStatement.setInt(6, u.getId());
            preparedStatement.executeUpdate();
            System.out.println("User modifié");
        } catch (SQLException ex) {
            System.out.println(ex);

        }
    }

    @Override
    public void Supprimer(User u) {
        String sql = "delete from user where id=?";
        try {
            PreparedStatement ste = connection.prepareStatement(sql);
            ste.setInt(1, u.getId());
            ste.executeUpdate();
            System.out.println("Utilisateur supprimé");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void SupprimerById(int t) {
        String sql = "DELETE FROM user WHERE id=?";
        try {

            PreparedStatement ste = connection.prepareStatement(sql);
            ste.setInt(1, t);

            ste.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public ObservableList<User> afficherUtilisateurs() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            String req = "SELECT * FROM user";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(req);
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setDateNaissance(rs.getDate("dateNaissance"));
                user.setGenre(rs.getString("genre"));
                users.add(user);
            }
        } catch (SQLException ex) {

            System.out.println("liste::" + users);
        }
        return users;
    }
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try {
            String req = "SELECT * FROM user";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(req);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Date dateNaissance= rs.getDate("dateNaissance");
                String genre = rs.getString("genre");

                // Create a User object and add it to the list
                User user = new User(id, nom, prenom, email, password, dateNaissance, genre);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
}


