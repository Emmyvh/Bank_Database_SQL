package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Account {

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

    public void createAccount(int amount, String accountType, int year, int month, int day,
            int walletNumber) throws SQLException {

        LocalDate localDate = LocalDate.of(year, month, day);

        makeConnection();

        if (connection != null) {
            String sql = "INSERT INTO \"Account\" (amount, account_type, opening_date, wallet_number) "
                    + "VALUES (? ,? ,?, ?);";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, amount);
            statement.setString(2, accountType);
            statement.setObject(3, localDate);
            statement.setInt(4, walletNumber);
            statement.executeUpdate();

            statement.close();
            connection.commit();
        }

        closeConnection();
    }

    public void updateAccount(int accountNumber, int amount, String accountType, int year, int month, int day,
            int walletNumber) throws SQLException {

        LocalDate localDate = LocalDate.of(year, month, day);

        makeConnection();

        if (connection != null) {
            String sql = "UPDATE \"Account\" SET(amount=?, account_type=?, opening_date=?, wallet_number=?) WHERE account_number= ?";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, amount);
            statement.setString(2, accountType);
            statement.setObject(3, localDate);
            statement.setInt(4, walletNumber);
            statement.setInt(5, accountNumber);
            statement.executeUpdate();

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void deleteAccount(int accountNumber) throws SQLException {

        makeConnection();

        if (connection != null) {

            String sql = "DELETE FROM \"Account\" WHERE account_number = ?;";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, accountNumber);
            statement.executeUpdate();

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void selectAccount(int number) throws SQLException {

        int accountNumber = 0;
        int amount = 0;
        String accountType = "";
        String openingDate = "";
        int walletNumber = 0;

        makeConnection();

        if (connection != null) {
            String sql = "SELECT * FROM \"Account\" WHERE account_number= ? ";

            statement = connection.prepareStatement(sql);

            statement.setInt(1, number);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                accountNumber = result.getInt("account_number");
                amount = result.getInt("amount");
                accountType = result.getString("account_type");
                openingDate = result.getString("opening_date");
                walletNumber = result.getInt("wallet_number");
            }

            System.out.println(
                    accountNumber + " " + amount + " " + accountType + " " + openingDate + " " + walletNumber);

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void getOwner(int accountNumber) throws SQLException {

        int owner = 0;

        makeConnection();

        if (connection != null) {
            String sql = "SELECT \"Account\".wallet_number, \"Wallet\".wallet_number, \"Client\".client_number, \"Client\".wallet_number "
                    + "FROM \"Account\" "
                    + "INNER JOIN \"Wallet\" ON \"Account\".wallet_number = \"Wallet\".wallet_number "
                    + "INNER JOIN \"Client\" ON \"Wallet\".wallet_number= \"Account\".wallet_number "
                    + "WHERE \"Account\".account_number = ? ";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, accountNumber);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                owner = result.getInt("client_number");
            }

            System.out.println("account owner: " + owner);

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void transactionTotalBalance(int accountNumber) throws SQLException {

        int amountSend = 0;
        int amountSendTotal = 0;
        int amountReceived = 0;
        int amountReceivedTotal = 0;
        int total = 0;

        makeConnection();

        if (connection != null) {
            String sql = "SELECT \"Account\".account_number, \"Transactions\".account_number_sender, \"Transactions\".amount "
                    + "FROM \"Account\" "
                    + "INNER JOIN \"Transactions\" ON \"Account\".account_number= \"Transactions\".account_number_sender "
                    + "WHERE \"Account\".account_number = ? ";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, accountNumber);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                amountSend = result.getInt("amount");

                total = total - amountSend;
                amountSendTotal = amountSendTotal + amountSend;
            }

            String sql2 = "SELECT \"Account\".account_number, \"Transactions\".account_number_recipient, \"Transactions\".amount "
                    + "FROM \"Account\" "
                    + "INNER JOIN \"Transactions\" ON \"Account\".account_number= \"Transactions\".account_number_recipient "
                    + "WHERE \"Account\".account_number = ? ";

            statement2 = connection.prepareStatement(sql2);
            statement2.setInt(1, accountNumber);
            ResultSet result2 = statement2.executeQuery();

            while (result2.next()) {
                amountReceived = result2.getInt("amount");

                total = total + amountReceived;
                amountReceivedTotal = amountReceivedTotal + amountReceived;
            }

            System.out.println("account number: " + accountNumber + ", send: -" + amountSendTotal + ", received: "
                    + amountReceivedTotal + ", transaction total: " + total);

            statement.close();
            statement2.close();
            connection.commit();
        }
        closeConnection();
    }

    public void transactions(int accountNumber) throws SQLException {

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
                    + "FROM \"Account\" "
                    + "INNER JOIN \"Transactions\" ON \"Account\".account_number= \"Transactions\".account_number_recipient OR \"Account\".account_number= \"Transactions\".account_number_sender "
                    + "WHERE \"Account\".account_number = ? ";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, accountNumber);
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

                System.out.println("account number: " + accountNumber + ", transaction id: " + id + ", description: "
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
