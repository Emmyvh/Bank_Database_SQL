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

    public void createAccount(int accountNumber, int amount, String accountType, int year, int month, int day,
            int walletNumber) throws SQLException {

        LocalDate localDate = LocalDate.of(year, month, day);

        makeConnection();

        if (connection != null) {
            String sql = "";
            if (accountType == "invest") {
                sql = "BEGIN TRANSACTION;"
                        + "INSERT INTO \"Account\" (account_number, amount, account_type, opening_date) "
                        + "VALUES (? ,? ,? ,?);"
                        + "UPDATE \"Wallet\" SET account_number_invest = ? WHERE wallet_number = ?;"
                        + "COMMIT;";
            } else if (accountType == "savings") {
                sql = "BEGIN TRANSACTION;"
                        + "INSERT INTO \"Account\" (account_number, amount, account_type, opening_date) "
                        + "VALUES (? ,? ,? ,?);"
                        + "UPDATE \"Wallet\" SET account_number_savings = ? WHERE wallet_number = ?;"
                        + "COMMIT;";
            } else if (accountType == "current") {
                sql = "BEGIN TRANSACTION;"
                        + "INSERT INTO \"Account\" (account_number, amount, account_type, opening_date) "
                        + "VALUES (? ,? ,? ,?);"
                        + "UPDATE \"Wallet\" SET account_number_current = ? WHERE wallet_number = ?;"
                        + "COMMIT;";
            } else {
                System.out.println(accountType + " is not a valid account type");
            }

            statement = connection.prepareStatement(sql);

            statement.setInt(1, accountNumber);
            statement.setInt(2, amount);
            statement.setString(3, accountType);
            statement.setObject(4, localDate);
            statement.setInt(5, accountNumber);
            statement.setInt(6, walletNumber);

            statement.executeUpdate();
            System.out.println("Executed query successfully");

            statement.close();
            connection.commit();
        }

        closeConnection();
    }

    public void updateAccount(int accountNumber, int amount, String accountType, int year, int month, int day,
            int clientNumber) throws SQLException {

        LocalDate localDate = LocalDate.of(year, month, day);

        makeConnection();

        if (connection != null) {
            String sql = "UPDATE \"Account\" SET(account_number=?, amount=?, account_type=?, opening_date=?, client_number=?) WHERE account_number= ?";

            statement = connection.prepareStatement(sql);

            statement.setInt(1, accountNumber);
            statement.setInt(2, amount);
            statement.setString(3, accountType);
            statement.setObject(4, localDate);
            statement.setInt(5, clientNumber);
            statement.setInt(6, accountNumber);

            statement.executeUpdate();
            System.out.println("Executed query successfully");

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void deleteAccount(int accountNumber) throws SQLException {
        String accountType = "";

        makeConnection();

        if (connection != null) {

            String sql = "SELECT * FROM \"Account\" WHERE account_number= ?;";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, accountNumber);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                accountType = result.getString("account_type");
            }
            System.out.println("collected data");

            String sql2 = "";
            if (accountType.equals("invest")) {
                sql2 = "BEGIN TRANSACTION;"
                        + "UPDATE \"Wallet\" SET account_number_invest = Null WHERE account_number_invest = ?;"
                        + "DELETE FROM \"Account\" WHERE account_number = ?;"
                        + "COMMIT;";
            } else if (accountType.equals("savings")) {
                sql2 = "BEGIN TRANSACTION;"
                        + "UPDATE \"Wallet\" SET account_number_savings = Null WHERE account_number_savings = ?;"
                        + "DELETE FROM \"Account\" WHERE account_number = ?;"
                        + "COMMIT;";
            } else if (accountType.equals("current")) {
                sql2 = "BEGIN TRANSACTION;"
                        + "UPDATE \"Wallet\" SET account_number_current = Null WHERE account_number_current = ?;"
                        + "DELETE FROM \"Account\" WHERE account_number = ?;"
                        + "COMMIT;";
            } else {
                System.out.println("error in retrieved string");
            }

            statement2 = connection.prepareStatement(sql2);
            statement2.setInt(1, accountNumber);
            statement2.setInt(2, accountNumber);
            statement2.executeQuery();
            System.out.println("Executed query successfully");

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void selectAccount(int number) throws SQLException {

        makeConnection();

        if (connection != null) {
            String sql = "SELECT * FROM \"Account\" WHERE account_number= ? ";

            statement = connection.prepareStatement(sql);

            statement.setInt(1, number);

            ResultSet result = statement.executeQuery();
            System.out.println("Executed query successfully");

            while (result.next()) {
                int accountNumber = result.getInt("account_number");
                String amount = result.getString("amount");
                String accountType = result.getString("account_type");
                String openingDate = result.getString("opening_date");

                System.out.println(
                        accountNumber + " " + amount + " " + accountType + " " + openingDate);
            }
            statement.close();
            connection.commit();
        }
        closeConnection();
    }
}
