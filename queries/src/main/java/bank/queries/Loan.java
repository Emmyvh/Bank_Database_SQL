package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Loan {

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

    public void createLoan(int loanId, int accountNumber, int contraAcount, int year, int month, int day,
            int year2,
            int month2, int day2, int amountOriginal, int paymentIntervalAmount, int paymentIntervalDays)
            throws SQLException {

        LocalDate creation = LocalDate.of(year, month, day);
        LocalDate maturity = LocalDate.of(year2, month2, day2);
        LocalDate nextInstalment = LocalDate.of(year, month, (day + paymentIntervalDays));
        int numberOfIntervals = amountOriginal / paymentIntervalAmount;

        makeConnection();

        if (connection != null) {

            String sql = "BEGIN TRANSACTION;"
                    + "INSERT INTO \"Outstanding_Loan\" (account_number, contra_account, contract_date, maturity_date, original_amount, payment_interval_amount, remaining_amount, payment_interval_days, date_next_instalment)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"
                    + "COMMIT;";

            String sql2 = "BEGIN TRANSACTION;"
                    + "INSERT INTO \"Stored_Transactions\" (description, amount, date_of_creation, date_of_execution, account_number_sender, account_number_recipient, loan_id)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);"
                    + "COMMIT;";

            statement = connection.prepareStatement(sql);

            statement.setInt(1, accountNumber);
            statement.setInt(2, contraAcount);
            statement.setObject(3, creation);
            statement.setObject(4, maturity);
            statement.setInt(5, amountOriginal);
            statement.setInt(6, paymentIntervalAmount);
            statement.setInt(7, amountOriginal);
            statement.setInt(8, paymentIntervalDays);
            statement.setObject(9, nextInstalment);

            statement2 = connection.prepareStatement(sql2);
            LocalDate newNextInstalment = nextInstalment;

            statement2.setString(1, "loan instalment");
            statement2.setInt(2, paymentIntervalAmount);
            statement2.setObject(3, creation);
            statement2.setObject(4, newNextInstalment);
            statement2.setInt(5, accountNumber);
            statement2.setInt(6, contraAcount);
            statement2.setInt(7, loanId);

            statement.executeUpdate();
            System.out.println("Created loan");
            statement2.executeUpdate();
            System.out.println("Created stored transaction");

            numberOfIntervals--;

            while (numberOfIntervals > 0) {
                newNextInstalment = newNextInstalment.plusDays(paymentIntervalDays);

                statement3 = connection.prepareStatement(sql2);

                statement3.setString(1, "loan instalment");
                statement3.setInt(2, paymentIntervalAmount);
                statement3.setObject(3, creation);
                statement3.setObject(4, newNextInstalment);
                statement3.setInt(5, accountNumber);
                statement3.setInt(6, contraAcount);
                statement3.setInt(7, loanId);

                statement3.executeUpdate();
                System.out.println("Created stored transaction");
                numberOfIntervals--;
            }

            System.out.println("End of query");

            statement.close();
            connection.commit();
        }
        closeConnection();
    }
}
