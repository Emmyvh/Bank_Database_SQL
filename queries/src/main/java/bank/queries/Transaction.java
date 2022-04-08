package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class Transaction {

    public void transferMoney(int transactionId, String description, int amount, int year, int month, int day,
            int year2, int month2, int day2, int sender, int receiver) {
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        LocalDate localDate = LocalDate.of(year, month, day);
        LocalDate localDate2 = LocalDate.of(year2, month2, day2);

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "INSERT INTO \"Transaction\" (transaction_id, description, amount, date_of_creation, date_of_execution, client_number_sender, client_number_recipient)"
                        + "VALUES ( ?, ?, ?, ?, ?, ?, ?)";
                String sql2 = "UPDATE \"Account\" SET amount = amount-? WHERE account_number = ?";
                String sql3 = "UPDATE \"Account\" SET amount = amount+? WHERE account_number = ?";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, transactionId);
                statement.setString(2, description);
                statement.setObject(3, amount);
                statement.setObject(4, localDate);
                statement.setObject(5, localDate2);
                statement.setInt(6, sender);
                statement.setInt(7, receiver);

                int affectedRows = statement.executeUpdate();
                System.out.println("Created transaction");

                if (affectedRows > 0) {
                    statement2 = connection.prepareStatement(sql2);
                    statement2.setInt(1, amount);
                    statement2.setInt(2, sender);
                    statement2.executeUpdate();
                    System.out.println("Got money from sender");
                } else {
                    connection.rollback();
                    System.out.println("Rolled back on 2th query");
                }

                if (affectedRows > 0) {
                    statement3 = connection.prepareStatement(sql3);
                    statement3.setInt(1, amount);
                    statement3.setInt(2, receiver);
                    statement3.executeUpdate();
                    System.out.println("Given money to receiver");
                } else {
                    connection.rollback();
                    System.out.println("Rolled back on 3rd query");
                }

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
