package services;

import entities.Credit;
import entities.Remboursement;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRemboursement implements IService<Remboursement> {
    Connection connection;

    public ServiceRemboursement() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Remboursement remboursement) throws SQLException {
        String req = "INSERT INTO remboursement (duree_restante, montant_r, montant_restant, date_r, credit_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, remboursement.getDureeRestante());
        preparedStatement.setDouble(2, remboursement.getMontantR());
        preparedStatement.setDouble(3, remboursement.getMontantRestant());
        preparedStatement.setDate(4, new java.sql.Date(remboursement.getDateR().getTime()));
        preparedStatement.setInt(5, remboursement.getCredit().getId());

        preparedStatement.executeUpdate();
        System.out.println("Remboursement ajouté");
    }

    @Override
    public void edit(Remboursement remboursement) throws SQLException {
        String req = "UPDATE remboursement SET duree_restante=?, montant_r=?, montant_restant=?, date_r=?, credit_id=? WHERE id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, remboursement.getDureeRestante());
        preparedStatement.setDouble(2, remboursement.getMontantR());
        preparedStatement.setDouble(3, remboursement.getMontantRestant());
        preparedStatement.setDate(4, new java.sql.Date(remboursement.getDateR().getTime()));
        preparedStatement.setInt(5, remboursement.getCredit().getId());
        preparedStatement.setInt(6, remboursement.getId());

        preparedStatement.executeUpdate();
        System.out.println("Remboursement modifié");
    }

    @Override
    public void delete(int id) throws SQLException {
        String req = "DELETE FROM remboursement WHERE id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);

        preparedStatement.executeUpdate();
        System.out.println("Remboursement supprimé");
    }

    @Override
    public List<Remboursement> getAll() throws SQLException {
        List<Remboursement> remboursements = new ArrayList<>();
        String req = "SELECT * FROM remboursement";

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(req);

        while (rs.next()) {
            int id = rs.getInt("id");
            int dureeRestante = rs.getInt("duree_restante");
            double montantR = rs.getDouble("montant_r");
            double montantRestant = rs.getDouble("montant_restant");
            Date dateR = rs.getDate("date_r");

            int creditId = rs.getInt("credit_id");
            Credit credit = new ServiceCredit().getById(creditId);

            Remboursement remboursement = new Remboursement(id, dureeRestante, montantR, montantRestant, dateR, credit);
            remboursements.add(remboursement);
        }

        return remboursements;
    }

    @Override
    public Remboursement getById(int id) throws SQLException {
        String req = "SELECT * FROM remboursement WHERE id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);

        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            int dureeRestante = rs.getInt("duree_restante");
            double montantR = rs.getDouble("montant_r");
            double montantRestant = rs.getDouble("montant_restant");
            Date dateR = rs.getDate("date_r");

            int creditId = rs.getInt("credit_id");
            Credit credit = new ServiceCredit().getById(creditId);

            return new Remboursement(id, dureeRestante, montantR, montantRestant, dateR, credit);
        } else {
            return null;
        }
    }

    public List<Remboursement> getByCredit(int creditId) throws SQLException {
        List<Remboursement> remboursements = new ArrayList<>();
        String req = "SELECT * FROM remboursement WHERE credit_id="+creditId+";";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(req);
        while (rs.next()) {
            int id = rs.getInt("id");
            int dureeRestante = rs.getInt("duree_restante");
            double montantR = rs.getDouble("montant_r");
            double montantRestant = rs.getDouble("montant_restant");
            Date dateR = rs.getDate("date_r");
            Credit credit = new ServiceCredit().getById(creditId);
            Remboursement remboursement = new Remboursement(id, dureeRestante, montantR, montantRestant, dateR, credit);
            remboursements.add(remboursement);
        }
        return remboursements;
    }
}
