package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transactions {

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

    public void transferDirectly(int transactionId, String description, int amount, int year, int month, int day,
            int year2, int month2, int day2, int sender, int receiver) throws SQLException {

        LocalDate localDate = LocalDate.of(year, month, day);
        LocalDate localDate2 = LocalDate.of(year2, month2, day2);

        makeConnection();

        if (connection != null) {
            String sql = "BEGIN TRANSACTION;"
                    + "INSERT INTO \"Transactions\" (transaction_id, description, amount, date_of_creation, date_of_execution, account_number_sender, account_number_recipient)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?);"
                    + "UPDATE \"Account\" SET amount = amount-? WHERE account_number = ?;"
                    + "UPDATE \"Account\" SET amount = amount+? WHERE account_number = ?;"
                    + "COMMIT;";

            statement = connection.prepareStatement(sql);

            statement.setInt(1, transactionId);
            statement.setString(2, description);
            statement.setObject(3, amount);
            statement.setObject(4, localDate);
            statement.setObject(5, localDate2);
            statement.setInt(6, sender);
            statement.setInt(7, receiver);
            statement.setObject(8, amount);
            statement.setInt(9, sender);
            statement.setObject(10, amount);
            statement.setInt(11, receiver);

            statement.executeUpdate();
            System.out.println("Created transaction");

            System.out.println("End of query");

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void transferDelayed(int transactionId, String description, int amount, int year, int month, int day,
            int year2, int month2, int day2, int sender, int receiver) throws SQLException {

        LocalDate localDate = LocalDate.of(year, month, day);
        LocalDate localDate2 = LocalDate.of(year2, month2, day2);

        makeConnection();

        if (connection != null) {
            String sql = "INSERT INTO \"Stored_Transactions\" (transaction_id, description, amount, date_of_creation, date_of_execution, account_number_sender, account_number_recipient)"
                    + "VALUES ( ?, ?, ?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(sql);

            statement.setInt(1, transactionId);
            statement.setString(2, description);
            statement.setObject(3, amount);
            statement.setObject(4, localDate);
            statement.setObject(5, localDate2);
            statement.setInt(6, sender);
            statement.setInt(7, receiver);

            statement.executeUpdate();
            System.out.println("Created transaction");

            System.out.println("End of query");

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void ExecuteStoredTransaction(int transactionId, int transactionID2) throws SQLException {

        String description = null;
        int amount = 0;
        String dateOfCreation = null;
        String dateOfExecution = null;
        int sender = 0;
        int receiver = 0;
        int loanId = 0;

        makeConnection();

        if (connection != null) {
            String sql = "SELECT * FROM \"Stored_Transactions\" WHERE transaction_id = ?";

            statement = connection.prepareStatement(sql);

            statement.setInt(1, transactionId);

            ResultSet result = statement.executeQuery();
            System.out.println("Collected data");

            while (result.next()) {
                description = result.getString("description");
                amount = result.getInt("amount");
                dateOfCreation = result.getString("date_of_creation");
                dateOfExecution = result.getString("date_of_execution");
                sender = result.getInt("account_number_sender");
                receiver = result.getInt("account_number_recipient");
                loanId = result.getInt("loan_id");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
            LocalDate localDateCreation = LocalDate.parse(dateOfCreation, formatter);
            LocalDate localDateMaturity = LocalDate.parse(dateOfExecution, formatter);

            String sql2 = "BEGIN TRANSACTION;"
                    + "INSERT INTO \"Transactions\" (transaction_id, description, amount, date_of_creation, date_of_execution, account_number_sender, account_number_recipient, loant_id)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);"
                    + "UPDATE \"Account\" SET amount = amount-? WHERE account_number = ?;"
                    + "UPDATE \"Account\" SET amount = amount+? WHERE account_number = ?;"
                    + "DELETE FROM \"Stored_Transactions\" WHERE transaction_id = ?;"
                    + "COMMIT;";

            statement2 = connection.prepareStatement(sql2);

            statement2.setInt(1, transactionID2);
            statement2.setString(2, description);
            statement2.setObject(3, amount);
            statement2.setObject(4, localDateCreation);
            statement2.setObject(5, localDateMaturity);
            statement2.setInt(6, sender);
            statement2.setInt(7, receiver);
            statement2.setInt(8, loanId);
            statement2.setObject(9, amount);
            statement2.setInt(10, sender);
            statement2.setObject(11, amount);
            statement2.setInt(12, receiver);
            statement2.setInt(13, transactionId);

            statement2.executeUpdate();
            System.out.println("Created transaction");

            System.out.println("End of query");

            statement.close();
            connection.commit();
        }
        closeConnection();
    }
}
