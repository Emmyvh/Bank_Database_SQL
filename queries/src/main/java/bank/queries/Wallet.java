package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Wallet {

    Connection connection = null;
    PreparedStatement statement = null;

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

    public void createWallet(int walletNumber, int clientNumber) throws SQLException {

        makeConnection();

        connection.setAutoCommit(false);
        System.out.println("Opened database successfully");

        if (connection != null) {
            String sql = "BEGIN TRANSACTION;"
                    + "INSERT INTO \"Wallet\" (wallet_number) "
                    + "VALUES (?);"
                    + "UPDATE \"Client\" SET wallet_number = ? WHERE client_number = ?;"
                    + "COMMIT;";

            statement = connection.prepareStatement(sql);

            statement.setInt(1, walletNumber);
            statement.setInt(2, walletNumber);
            statement.setInt(3, clientNumber);

            statement.executeUpdate();
            System.out.println("Executed query successfully");

            statement.close();
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
            System.out.println("Executed query successfully");

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void selectWallet(int number) throws SQLException {

        makeConnection();

        if (connection != null) {
            String sql = "SELECT * FROM \"Wallet\" WHERE wallet_number= ? ";

            statement = connection.prepareStatement(sql);

            statement.setInt(1, number);

            ResultSet result = statement.executeQuery();
            System.out.println("Executed query successfully");

            while (result.next()) {
                int walletNumber = result.getInt("wallet_number");
                String accountNumberCurrent = result.getString("account_number_current");
                String accountNumberSavings = result.getString("account_number_savings");
                String accountNumberInvest = result.getString("account_number_invest");

                System.out.println(walletNumber + " " + accountNumberCurrent + " " + accountNumberSavings + " "
                        + accountNumberInvest);
            }
            statement.close();
            connection.commit();
        }
        closeConnection();
    }
}
