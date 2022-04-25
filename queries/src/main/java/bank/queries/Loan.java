package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Loan {

    Connection connection = null;
    PreparedStatement statement = null;
    PreparedStatement statement2 = null;
    PreparedStatement statement3 = null;
    PreparedStatement statement4 = null;
    PreparedStatement statement5 = null;
    PreparedStatement statement6 = null;
    PreparedStatement statement7 = null;

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

    public void createLoan(int accountNumber, int contraAccount, int year, int month, int day,
            int amountOriginal, int paymentIntervalAmount, int paymentIntervalDays)
            throws SQLException {

        LocalDate creation = LocalDate.now();
        LocalDate maturity = LocalDate.of(year, month, day);
        LocalDate nextInstalment = creation.plusDays(paymentIntervalDays);
        int numberOfIntervals = amountOriginal / paymentIntervalAmount;
        int loanId = 0;
        int transactionId = 0;

        makeConnection();

        if (connection != null) {

            String sql = "BEGIN TRANSACTION;"
                    + "INSERT INTO \"Outstanding_Loan\" (account_number, contra_account, contract_date, maturity_date, original_amount, payment_interval_amount, payment_interval_days, date_next_instalment)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?); ";

            System.out.println("checkpoint 1");
            statement = connection.prepareStatement(sql);
            statement.setInt(1, accountNumber);
            statement.setInt(2, contraAccount);
            statement.setObject(3, creation);
            statement.setObject(4, maturity);
            statement.setInt(5, amountOriginal);
            statement.setInt(6, paymentIntervalAmount);
            statement.setInt(7, paymentIntervalDays);
            statement.setObject(8, nextInstalment);
            statement.executeUpdate();

            String sql2 = "SELECT MAX (loan_id) FROM \"Outstanding_Loan\"";

            System.out.println("checkpoint 2");
            statement2 = connection.prepareStatement(sql2);
            ResultSet result = statement2.executeQuery();
            while (result.next()) {
                loanId = result.getInt("max");
            }

            String sql3 = "INSERT INTO \"Transactions\" (description, amount, date_of_creation, date_of_execution, account_number_sender, account_number_recipient, loan_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?);";

            System.out.println("checkpoint 3");
            statement3 = connection.prepareStatement(sql3);
            statement3.setString(1, "starting loan");
            statement3.setInt(2, amountOriginal);
            statement3.setObject(3, creation);
            statement3.setObject(4, creation);
            statement3.setInt(5, contraAccount);
            statement3.setInt(6, accountNumber);
            statement3.setInt(7, loanId);
            statement3.executeUpdate();

            String sql4 = "SELECT MAX (transaction_id) FROM \"Transactions\"";
            statement4 = connection.prepareStatement(sql4);
            ResultSet result2 = statement4.executeQuery();
            while (result2.next()) {
                transactionId = result2.getInt("max");
            }

            String sql5 = "INSERT INTO \"Processed_Transactions\" (transaction_id)"
                    + "VALUES (?);"
                    + "UPDATE \"Account\" SET amount = amount - ? WHERE account_number = ?;"
                    + "UPDATE \"Account\" SET amount = amount + ? WHERE account_number = ?;"
                    + "INSERT INTO \"Transactions\" (description, amount, date_of_creation, date_of_execution, account_number_sender, account_number_recipient, loan_id)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?);";

            LocalDate newNextInstalment = nextInstalment;

            System.out.println("checkpoint 4");
            statement5 = connection.prepareStatement(sql5);
            statement5.setInt(1, transactionId);
            statement5.setInt(2, amountOriginal);
            statement5.setInt(3, contraAccount);
            statement5.setInt(4, amountOriginal);
            statement5.setInt(5, accountNumber);
            statement5.setString(6, "loan instalment");
            statement5.setInt(7, paymentIntervalAmount);
            statement5.setObject(8, creation);
            statement5.setObject(9, newNextInstalment);
            statement5.setInt(10, accountNumber);
            statement5.setInt(11, contraAccount);
            statement5.setInt(12, loanId);
            statement5.executeUpdate();

            numberOfIntervals--;

            System.out.println("checkpoint 5");

            String sql6 = "INSERT INTO \"Transactions\" (description, amount, date_of_creation, date_of_execution, account_number_sender, account_number_recipient, loan_id)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?);";

            while (numberOfIntervals > 0) {
                newNextInstalment = newNextInstalment.plusDays(paymentIntervalDays);

                statement6 = connection.prepareStatement(sql6);
                statement6.setString(1, "loan instalment");
                statement6.setInt(2, paymentIntervalAmount);
                statement6.setObject(3, creation);
                statement6.setObject(4, newNextInstalment);
                statement6.setInt(5, accountNumber);
                statement6.setInt(6, contraAccount);
                statement6.setInt(7, loanId);
                statement6.executeUpdate();

                numberOfIntervals--;
            }

            String sql7 = "COMMIT;";

            statement7 = connection.prepareStatement(sql7);
            statement7.executeUpdate();

            statement.close();
            statement2.close();
            statement3.close();
            statement4.close();
            statement5.close();
            statement6.close();
            statement7.close();
            connection.commit();
        }
        closeConnection();
    }

    public void transactions(int loanId) throws SQLException {

        int id = 0;
        String description = "";
        int amount = 0;
        String dateCreation = "";
        String dateExecution = "";
        int sender = 0;
        int recipient = 0;

        makeConnection();

        if (connection != null) {
            String sql = "SELECT \"Transactions\".transaction_id, \"Transactions\".description, \"Transactions\".amount, "
                    + "\"Transactions\".date_of_creation, \"Transactions\".date_of_execution, \"Transactions\".account_number_sender, "
                    + "\"Transactions\".account_number_recipient, \"Transactions\".loan_id "
                    + "FROM \"Outstanding_Loan\" "
                    + "INNER JOIN \"Transactions\" ON \"Outstanding_Loan\".loan_id= \"Transactions\".loan_id "
                    + "WHERE \"Outstanding_Loan\".loan_id = ? ";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, loanId);
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

                System.out.println("loan id: " + loanId + ", transaction id: " + id + ", description: "
                        + description + ", amount: " + amount + ", date of creation: " + dateCreation
                        + ", date of Execution: " + dateExecution + ", account of sender: " + sender
                        + ", account of recipient: " + recipient);
            }

            statement.close();
            connection.commit();
        }
        closeConnection();
    }
}
