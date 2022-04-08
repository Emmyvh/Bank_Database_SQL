package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Account {

    public void createAccount(int accountNumber, int amount, String accountType, int year, int month, int day,
            int clientNumber) {
        Connection connection = null;
        PreparedStatement statement = null;
        LocalDate localDate = LocalDate.of(year, month, day);

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "INSERT INTO \"Account\" (account_number, amount, account_type, opening_date, client_number) "
                        + "VALUES (? ,? ,?, ?, ?);";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, accountNumber);
                statement.setInt(2, amount);
                statement.setString(3, accountType);
                statement.setObject(4, localDate);
                statement.setInt(5, clientNumber);

                statement.executeUpdate();
                System.out.println("Executed query successfully");

                statement.close();
                connection.commit();
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Error in database connection");
        }
        System.exit(0);
    }

    public void updateAccount(int accountNumber, int amount, String accountType, int year, int month, int day,
            int clientNumber) {
        Connection connection = null;
        PreparedStatement statement = null;
        LocalDate localDate = LocalDate.of(year, month, day);

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

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
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Error in database connection");
        }
        System.exit(0);
    }

    public void deleteAccount(int accountNumber) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "DELETE FROM \"Account\" WHERE account_number= ? ";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, accountNumber);

                statement.executeUpdate();
                System.out.println("Executed query successfully");

                statement.close();
                connection.commit();
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Error in database connection");
        }
        System.exit(0);
    }

    public void selectAccount(int number) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

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
                    int clientNumber = result.getInt("client_number");
                    int transactionID = result.getInt("transaction_id");

                    System.out.println(accountNumber + " " + amount + " " + accountType + " " + openingDate + " "
                            + clientNumber + " " + transactionID);
                }
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
