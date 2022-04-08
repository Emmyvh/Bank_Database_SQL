package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Account account = new Account();
        Client client = new Client();
        Wallet wallet = new Wallet();
        Transactions transactions = new Transactions();

        ExecuteStoredTransaction(1);

    }

    public static void ExecuteStoredTransaction(int transactionId) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "SELECT FROM \"Stored_Transactions\" WHERE transaction_id = ?";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, transactionId);

                ResultSet resultset = statement.executeQuery();
                System.out.println("Collected data");

                while (resultset.next()) {
                    String description = resultset.getString("description");
                    int amount = resultset.getInt("amount");
                    LocalDate dateOfCreation = (LocalDate) resultset.getObject("date_of_creation");
                    LocalDate dateOfExecution = (LocalDate) resultset.getObject("date_of_execution");
                    int sender = resultset.getInt("client_number_sender");
                    int receiver = resultset.getInt("client_number_recipient");

                    System.out.println(description + " " + amount + " " + dateOfCreation + " " + dateOfExecution + " "
                            + sender + " " + receiver);
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
