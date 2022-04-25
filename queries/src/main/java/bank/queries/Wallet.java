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

        makeConnection();

        if (connection != null) {
            String sql = "INSERT INTO \"Wallet\" (client_number) "
                    + "VALUES (?);";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, clientNumber);
            statement.executeUpdate();

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void deleteWallet(int walletNumber) throws SQLException {

        makeConnection();

        if (connection != null) {
            String sql = "DELETE FROM \"Wallet\" WHERE wallet_number = ?";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, walletNumber);
            statement.executeUpdate();

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void getAccounts(int walletNumber) throws SQLException {
        int account = 0;
        String type = "";

        makeConnection();

        if (connection != null) {
            String sql = "SELECT \"Account\".account_number, \"Account\".account_type "
                    + "FROM \"Wallet\" "
                    + "INNER JOIN \"Account\" ON \"Wallet\".wallet_number = \"Account\".wallet_number "
                    + "WHERE \"Wallet\".wallet_number = ? ";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, walletNumber);
            ResultSet result = statement.executeQuery();

            System.out.println("wallet number: " + walletNumber);
            while (result.next()) {
                account = result.getInt("account_number");
                type = result.getString("account_type");
                System.out.println("account: " + account + ", type: " + type);
            }

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void getBalance(int walletNumber) throws SQLException {

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

    public void getTransactions(int walletNumber) throws SQLException {

        int id = 0;
        String description = "";
        int amount = 0;
        String dateCreation = "";
        String dateExecution = "";
        int sender = 0;
        int recipient = 0;
        int loanId = 0;

        makeConnection();

        if (connection != null) {
            String sql = "SELECT \"Transactions\".transaction_id, \"Transactions\".description, \"Transactions\".amount, "
                    + "\"Transactions\".date_of_creation, \"Transactions\".date_of_execution, \"Transactions\".account_number_sender, "
                    + "\"Transactions\".account_number_recipient, \"Transactions\".loan_id "
                    + "FROM \"Wallet\" "
                    + "INNER JOIN \"Account\" ON \"Wallet\".wallet_number = \"Account\".wallet_number "
                    + "INNER JOIN \"Transactions\" ON \"Account\".account_number= \"Transactions\".account_number_recipient OR \"Account\".account_number= \"Transactions\".account_number_sender "
                    + "WHERE \"Wallet\".wallet_number = ? ";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, walletNumber);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                id = result.getInt("transaction_id");
                description = result.getString("description");
                amount = result.getInt("amount");
                dateCreation = result.getString("date_of_creation");
                dateExecution = result.getString("date_of_execution");
                sender = result.getInt("account_number_sender");
                recipient = result.getInt("account_number_recipient");
                loanId = result.getInt("loan_id");

                System.out.println("wallet number: " + walletNumber + ", transaction id: " + id + ", description: "
                        + description + ", amount: " + amount + ", date of creation: " + dateCreation
                        + ", date of Execution: " + dateExecution + ", account of sender: " + sender
                        + ", account of recipient: " + recipient + ", loan id (optional): " + loanId);
            }

            statement.close();
            connection.commit();
        }
        closeConnection();
    }
}
