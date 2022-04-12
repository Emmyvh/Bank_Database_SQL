package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Transactions {

    public static void transferDirectly(int transactionId, String description, int amount, int year, int month, int day,
            int year2, int month2, int day2, int sender, int receiver) {
        Connection connection = null;
        PreparedStatement statement = null;
        LocalDate localDate = LocalDate.of(year, month, day);
        LocalDate localDate2 = LocalDate.of(year2, month2, day2);

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "BEGIN TRANSACTION;"
                        + "INSERT INTO \"Transactions\" (transaction_id, description, amount, date_of_creation, date_of_execution, client_number_sender, client_number_recipient)"
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
            connection.close();
        }

        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Error in database connection");
        }
        System.exit(0);
    }

    public void transferDelayed(int transactionId, String description, int amount, int year, int month, int day,
            int year2, int month2, int day2, int sender, int receiver) {
        Connection connection = null;
        PreparedStatement statement = null;
        LocalDate localDate = LocalDate.of(year, month, day);
        LocalDate localDate2 = LocalDate.of(year2, month2, day2);

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "INSERT INTO \"Stored_Transactions\" (transaction_id, description, amount, date_of_creation, date_of_execution, client_number_sender, client_number_recipient)"
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
            connection.close();
        }

        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Error in database connection");
        }
        System.exit(0);
    }

    public static void ExecuteStoredTransaction(int transactionId, int transactionID2) {
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        String description = null;
        int amount = 0;
        String dateOfCreation = null;
        String dateOfExecution = null;
        int sender = 0;
        int receiver = 0;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

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
                    sender = result.getInt("client_number_sender");
                    receiver = result.getInt("client_number_recipient");
                }

                int year = Integer.parseInt(dateOfCreation.split("-")[0]);
                int month = Integer.parseInt(dateOfCreation.split("-")[1]);
                int day = Integer.parseInt(dateOfCreation.split("-")[2]);

                int year2 = Integer.parseInt(dateOfExecution.split("-")[0]);
                int month2 = Integer.parseInt(dateOfExecution.split("-")[1]);
                int day2 = Integer.parseInt(dateOfExecution.split("-")[2]);

                LocalDate localDate = LocalDate.of(year, month, day);
                LocalDate localDate2 = LocalDate.of(year2, month2, day2);

                String sql2 = "BEGIN TRANSACTION;"
                        + "INSERT INTO \"Transactions\" (transaction_id, description, amount, date_of_creation, date_of_execution, client_number_sender, client_number_recipient)"
                        + "VALUES (?, ?, ?, ?, ?, ?, ?);"
                        + "UPDATE \"Account\" SET amount = amount-? WHERE account_number = ?;"
                        + "UPDATE \"Account\" SET amount = amount+? WHERE account_number = ?;"
                        + "DELETE FROM \"Stored_Transactions\" WHERE transaction_id = ?;"
                        + "COMMIT;";

                statement2 = connection.prepareStatement(sql2);

                statement2.setInt(1, transactionID2);
                statement2.setString(2, description);
                statement2.setObject(3, amount);
                statement2.setObject(4, localDate);
                statement2.setObject(5, localDate2);
                statement2.setInt(6, sender);
                statement2.setInt(7, receiver);
                statement2.setObject(8, amount);
                statement2.setInt(9, sender);
                statement2.setObject(10, amount);
                statement2.setInt(11, receiver);
                statement2.setInt(12, transactionId);

                statement2.executeUpdate();
                System.out.println("Created transaction");

                System.out.println("End of query");

                statement.close();
                connection.commit();
            }
            connection.close();
        }

        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Error in database connection");
        }
        System.exit(0);
    }

    /*
     * public static void ExecuteStoredTransaction(int storedId, int newId) {
     * Connection connection = null;
     * PreparedStatement statement = null;
     * PreparedStatement statement2 = null;
     * String dateOfExecution = null;
     * LocalDate localDate = null;
     * 
     * try {
     * Class.forName("org.postgresql.Driver");
     * connection = DriverManager
     * .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres",
     * "123");
     * 
     * connection.setAutoCommit(false);
     * System.out.println("Opened database successfully");
     * 
     * if (connection != null) {
     * String sql =
     * "SELECT * FROM \"Stored_Transactions\" WHERE transaction_id = ?";
     * 
     * statement = connection.prepareStatement(sql);
     * 
     * statement.setInt(1, storedId);
     * 
     * ResultSet result = statement.executeQuery();
     * 
     * while (result.next()) {
     * dateOfExecution = result.getString("date_of_execution");
     * }
     * 
     * int year = Integer.parseInt(dateOfExecution.split("-")[0]);
     * int month = Integer.parseInt(dateOfExecution.split("-")[1]);
     * int day = Integer.parseInt(dateOfExecution.split("-")[2]);
     * 
     * localDate = LocalDate.of(year, month, day);
     * statement.close();
     * connection.commit();
     * 
     * String sql2 = "BEGIN TRANSACTION;"
     * + "INSERT INTO \"Transactions\" (transaction_id)"
     * + "VALUES (?);"
     * +
     * "UPDATE \"Transactions\" SET description = \"Stored_Transactions\".description, amount = \"Stored_Transactions\".amount, date_of_creation = \"Stored_Transactions\".date_of_creation, date_of_execution = \"Stored_Transactions\".date_of_execution, client_number_sender = \"Stored_Transactions\".client_number_sender, client_number_recipient = \"Stored_Transactions\".client_number_recipient"
     * +
     * "FROM \"Stored_Transactions\" WHERE \"Transactions\".transaction_id = ? AND \"Stored_Transactions\".transaction_id = ?;"
     * +
     * "UPDATE \"Account\" SET amount = \"Account\".amount - \"Transactions\".amount FROM \"Transactions\" WHERE \"Account\".account_number = \"Transactions\".client_number_sender;"
     * +
     * "UPDATE \"Account\" SET amount = \"Account\".amount + \"Transactions\".amount FROM \"Transactions\" WHERE \"Account\".account_number = \"Transactions\".client_number_recipient;"
     * + "DELETE FROM \"Stored_Transactions\" WHERE transaction_id = ?;"
     * + "COMMIT;";
     * 
     * statement2 = connection.prepareStatement(sql2);
     * 
     * statement2.setInt(1, newId);
     * statement2.setInt(2, newId);
     * statement2.setInt(3, storedId);
     * statement2.setInt(4, storedId);
     * 
     * statement2.executeUpdate();
     * 
     * System.out.println("Created transaction");
     * 
     * System.out.println("End of query");
     * 
     * statement2.close();
     * connection.commit();
     * }
     * connection.close();
     * }
     * 
     * catch (Exception e) {
     * e.printStackTrace();
     * System.err.println(e.getClass().getName() + ": " + e.getMessage());
     * System.out.println("Error in database connection");
     * }
     * System.exit(0);
     * }
     */
}
