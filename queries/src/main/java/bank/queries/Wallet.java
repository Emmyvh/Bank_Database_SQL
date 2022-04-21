package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Wallet {

    Connection connection = null;
    PreparedStatement statement = null;
    PreparedStatement statement2 = null;
    PreparedStatement statement3 = null;

    public void makeConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");
        }

        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Error in database connection");
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            System.out.println("Closed database");
        }
    }

    public void createWallet(int clientNumber) throws SQLException {
        int walletNumber = 0;

        makeConnection();

        if (connection != null) {
            String sql = "BEGIN TRANSACTION;"
                    + "INSERT INTO \"Wallet\" ";

            statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            String sql2 = "SELECT MAX (wallet_number) FROM \"Wallet\"";

            statement2 = connection.prepareStatement(sql2);
            ResultSet result = statement2.executeQuery();

            while (result.next()) {
                walletNumber = result.getInt("max");
            }

            String sql3 = "UPDATE \"Client\" SET wallet_number = ? WHERE client_number = ?;"
                    + "COMMIT;";

            statement3 = connection.prepareStatement(sql3);
            statement3.setInt(1, walletNumber);
            statement3.setInt(2, clientNumber);
            statement.executeUpdate();

            statement.close();
            statement2.close();
            statement3.close();
            connection.commit();
        }
        closeConnection();
    }

    public void deleteWallet(int walletNumber) throws SQLException {

        makeConnection();

        if (connection != null) {
            String sql = "BEGIN TRANSACTION;"
                    + "UPDATE \"Client\" SET wallet_number = Null WHERE wallet_number = ?;"
                    + "DELETE FROM \"Wallet\" WHERE wallet_number= ?; "
                    + "COMMIT;";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, walletNumber);
            statement.setInt(2, walletNumber);
            statement.executeUpdate();

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void balance(int walletNumber) throws SQLException {

        int amount = 0;
        int total = 0;

        makeConnection();

        if (connection != null) {
            String sql = "SELECT \"Wallet\".wallet_number, \"Account\".wallet_number, \"Account\".amount "
                    + "FROM \"Wallet\" "
                    + "INNER JOIN \"Account\" ON \"Wallet\".wallet_number = \"Account\".wallet_number "
                    + "WHERE \"Wallet\".wallet_number = ? ";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, walletNumber);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                amount = result.getInt("amount");

                total = total + amount;
            }

            System.out.println("wallet number: " + walletNumber + ", account value: " + total);

            statement.close();
            connection.commit();
        }
        closeConnection();
    }
}
