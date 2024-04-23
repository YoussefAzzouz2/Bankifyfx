package services;

import entities.CategorieCredit;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategorieCredit implements IService<CategorieCredit>{
    Connection connection;
    public ServiceCategorieCredit(){
        connection= MyDatabase.getInstance().getConnection();

    }
    @Override
    public void add(CategorieCredit categorie) throws SQLException {
        String req="insert into categorie_credit (nom,min_montant,max_montant)"+
                "values('"+categorie.getNom()+"','"+categorie.getMinMontant()+"',"+categorie.getMaxMontant()+")";

        Statement statement=connection.createStatement();
        statement.executeUpdate(req);
        System.out.println("categorie ajoute");
    }

    @Override
    public void edit(CategorieCredit categorie) throws SQLException {
        String req="update categorie_credit set nom=?,min_montant=?,max_montant=?  where id=?";
        PreparedStatement preparedStatement= connection.prepareStatement(req);
        preparedStatement.setString(1, categorie.getNom());
        preparedStatement.setDouble(2,categorie.getMinMontant());
        preparedStatement.setDouble(3,categorie.getMaxMontant());
        preparedStatement.setInt(4,categorie.getId());
        preparedStatement.executeUpdate();
        System.out.println("categorie modifie");
    }

    @Override
    public void delete(int id) throws SQLException {
        String req="delete from categorie_credit where id="+id+";";
        Statement statement=connection.createStatement();
        statement.executeUpdate(req);
        System.out.println("categorie supprime");
    }

    @Override
    public List<CategorieCredit> getAll() throws SQLException {
        List<CategorieCredit> categories= new ArrayList<>();
        String req="select * from categorie_credit";
        Statement statement= connection.createStatement();
        ResultSet rs= statement.executeQuery(req);

        while (rs.next()){
            CategorieCredit categorie= new CategorieCredit();
            categorie.setNom(rs.getString("nom"));
            categorie.setMinMontant(rs.getDouble("min_montant"));
            categorie.setMaxMontant(rs.getDouble("max_montant"));
            categorie.setId(rs.getInt("id"));

            categories.add(categorie);
        }
        return categories;
    }

    @Override
    public CategorieCredit getById(int id) throws SQLException {
        String req = "select * from categorie_credit WHERE id ="+id+";";
        Statement statement= connection.createStatement();
        ResultSet rs= statement.executeQuery(req);

        if (rs.next()) {
            String nom = rs.getString("nom");
            double minMontant = rs.getDouble("min_montant");
            double maxMontant = rs.getDouble("max_montant");

            return new CategorieCredit(id, nom, minMontant, maxMontant);
        } else {
            return null;
        }
    }

    public boolean nomExiste(String nom) throws SQLException {
        String req = "SELECT * FROM categorie_credit WHERE nom = ?";
        PreparedStatement statement = connection.prepareStatement(req);
        statement.setString(1, nom);
        ResultSet rs = statement.executeQuery();
        return rs.next();
    }
}
