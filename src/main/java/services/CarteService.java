package services;

import models.Carte;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarteService implements IService<Carte> {

    private Connection connection;

    public CarteService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    public boolean isCardNumberUnique(String num_c) throws SQLException {
        String sql = "SELECT COUNT(*) FROM carte WHERE num_c = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, num_c);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count == 0; // True if count is 0, meaning the card number is unique
        }
        return false; // Default to false if there was an error
    }


    @Override
    public void add(Carte carte) throws SQLException {
        String sql = "INSERT INTO carte (num_c, date_exp, type_c, statut_c) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, carte.getNum_c());
        preparedStatement.setDate(2, new java.sql.Date(carte.getDate_exp().getTime()));
        preparedStatement.setString(3, carte.getType_c());
        preparedStatement.setString(4, carte.getStatut_c());
        preparedStatement.executeUpdate();
    }

    @Override
    public void update(Carte carte) throws SQLException {
        String sql = "UPDATE carte SET num_c = ?, date_exp = ?, type_c = ?, statut_c = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, carte.getNum_c());
        preparedStatement.setDate(2, new java.sql.Date(carte.getDate_exp().getTime()));
        preparedStatement.setString(3, carte.getType_c());
        preparedStatement.setString(4, carte.getStatut_c());
        preparedStatement.setLong(5, carte.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM carte WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Carte> getAll() throws SQLException {
        String sql = "SELECT * FROM carte";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Carte> cartes = new ArrayList<>();
        while (rs.next()) {
            Carte carte = new Carte();
            carte.setId(rs.getLong("id"));
            carte.setNum_c(rs.getString("num_c"));
            carte.setDate_exp(rs.getDate("date_exp"));
            carte.setType_c(rs.getString("type_c"));
            carte.setStatut_c(rs.getString("statut_c"));

            cartes.add(carte);
        }
        return cartes;
    }

    @Override
    public Carte getById(int id) throws SQLException {
        String sql = "SELECT * FROM carte WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Carte carte = new Carte();
            carte.setId(resultSet.getLong("id"));
            carte.setNum_c(resultSet.getString("num_c"));
            carte.setDate_exp(resultSet.getDate("date_exp"));
            carte.setType_c(resultSet.getString("type_c"));
            carte.setStatut_c(resultSet.getString("statut_c"));

            return carte;
        } else {
            return null;
        }
    }

    public List<Carte> getAllCartes() {
        List<Carte> cartes = new ArrayList<>();
        String query = "SELECT * FROM carte";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Carte carte = new Carte();
                carte.setId(rs.getLong("id"));
                carte.setNum_c(rs.getString("num_c"));
                carte.setDate_exp(rs.getDate("date_exp"));
                carte.setType_c(rs.getString("type_c"));
                carte.setStatut_c(rs.getString("statut_c"));

                cartes.add(carte);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartes;
    }
}