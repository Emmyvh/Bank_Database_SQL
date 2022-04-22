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

    public void direct(String description, int amount, int sender, int receiver) throws SQLException {

        LocalDate localDate = LocalDate.now();
        int transactionId = 0;

        makeConnection();

        if (connection != null) {
            String sql = "BEGIN TRANSACTION;"
                    + "INSERT INTO \"Transactions\" (description, amount, date_of_creation, date_of_execution, account_number_sender, account_number_recipient)"
                    + "VALUES (?, ?, ?, ?, ?, ?);"
                    + "UPDATE \"Account\" SET amount = amount-? WHERE account_number = ?;"
                    + "UPDATE \"Account\" SET amount = amount+? WHERE account_number = ?;";

            statement = connection.prepareStatement(sql);
            statement.setString(1, description);
            statement.setInt(2, amount);
            statement.setObject(3, localDate);
            statement.setObject(4, localDate);
            statement.setInt(5, sender);
            statement.setInt(6, receiver);
            statement.setObject(7, amount);
            statement.setInt(8, sender);
            statement.setObject(9, amount);
            statement.setInt(10, receiver);
            statement.executeUpdate();

            String sql2 = "SELECT MAX (transaction_id) FROM \"Transactions\"";

            statement2 = connection.prepareStatement(sql2);
            ResultSet result = statement2.executeQuery();
            while (result.next()) {
                transactionId = result.getInt("max");
            }

            String sql3 = "INSERT INTO \"Processed_Transactions\" (transaction_id)"
                    + "VALUES (?);"
                    + "COMMIT;";

            statement3 = connection.prepareStatement(sql3);
            statement3.setInt(1, transactionId);
            statement3.executeUpdate();

            statement.close();
            statement2.close();
            statement3.close();
            connection.commit();

            closeConnection();
        }
    }

    public void delayed(String description, int amount, int year, int month, int day,
            int sender, int receiver) throws SQLException {

        LocalDate localDate = LocalDate.now();
        LocalDate localDate2 = LocalDate.of(year, month, day);

        makeConnection();

        if (connection != null) {
            String sql = "INSERT INTO \"Transactions\" (description, amount, date_of_creation, date_of_execution, account_number_sender, account_number_recipient)"
                    + "VALUES ( ?, ?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(sql);
            statement.setString(1, description);
            statement.setObject(2, amount);
            statement.setObject(3, localDate);
            statement.setObject(4, localDate2);
            statement.setInt(5, sender);
            statement.setInt(6, receiver);
            statement.executeUpdate();

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void ExecuteStored(int transactionId) throws SQLException {

        int amount = 0;
        String dateOfExecution = null;
        int sender = 0;
        int receiver = 0;

        makeConnection();

        if (connection != null) {
            String sql = "BEGIN TRANSACTION;";

            statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            String sql2 = "SELECT * FROM \"Transactions\" WHERE transaction_id = ?";

            statement2 = connection.prepareStatement(sql2);
            statement2.setInt(1, transactionId);
            ResultSet result = statement2.executeQuery();

            while (result.next()) {
                amount = result.getInt("amount");
                dateOfExecution = result.getString("date_of_execution");
                sender = result.getInt("account_number_sender");
                receiver = result.getInt("account_number_recipient");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
            LocalDate localDateExecution = LocalDate.parse(dateOfExecution, formatter);
            LocalDate today = LocalDate.now();

            if (localDateExecution.equals(today)) {
                String sql3 = "UPDATE \"Account\" SET amount = amount-? WHERE account_number = ?;"
                        + "UPDATE \"Account\" SET amount = amount+? WHERE account_number = ?;"
                        + "INSERT INTO \"Processed_Transactions\" (transaction_id) "
                        + "VALUES (?);"
                        + "COMMIT;";

                statement3 = connection.prepareStatement(sql3);
                statement3.setObject(1, amount);
                statement3.setInt(2, sender);
                statement3.setObject(3, amount);
                statement3.setInt(4, receiver);
                statement3.setInt(5, transactionId);
                statement3.executeUpdate();

                statement.close();
                statement2.close();
                statement3.close();
                connection.commit();
            } else {
                System.out.println("This transaction is not scheduled for today.");
            }
            closeConnection();
        }
    }
}
