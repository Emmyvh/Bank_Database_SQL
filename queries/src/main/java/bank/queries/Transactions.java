package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
}
