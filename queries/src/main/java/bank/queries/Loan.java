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

    public void createLoan(int accountNumber, int contraAcount, int year, int month, int day,
            int year2,
            int month2, int day2, int amountOriginal, int paymentIntervalAmount, int paymentIntervalDays)
            throws SQLException {

        LocalDate creation = LocalDate.of(year, month, day);
        LocalDate maturity = LocalDate.of(year2, month2, day2);
        LocalDate nextInstalment = LocalDate.of(year, month, (day + paymentIntervalDays));
        int numberOfIntervals = amountOriginal / paymentIntervalAmount;
        int loanId = 0;

        makeConnection();

        if (connection != null) {

            String sql = "BEGIN TRANSACTION;"
                    + "INSERT INTO \"Outstanding_Loan\" (account_number, contra_account, contract_date, maturity_date, original_amount, payment_interval_amount, payment_interval_days, date_next_instalment)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?); "
                    + "UPDATE \"Account\" SET amount = amount - ? WHERE account_number = ?;"
                    + "UPDATE \"Account\" SET amount = amount + ? WHERE account_number = ?;";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, accountNumber);
            statement.setInt(2, contraAcount);
            statement.setObject(3, creation);
            statement.setObject(4, maturity);
            statement.setInt(5, amountOriginal);
            statement.setInt(6, paymentIntervalAmount);
            statement.setInt(7, paymentIntervalDays);
            statement.setObject(8, nextInstalment);
            statement.setInt(9, amountOriginal);
            statement.setInt(10, contraAcount);
            statement.setInt(11, amountOriginal);
            statement.setInt(12, accountNumber);
            statement.executeUpdate();

            String sql2 = "SELECT MAX (loan_id) FROM \"Outstanding_Loan\"";

            statement2 = connection.prepareStatement(sql2);
            ResultSet result = statement2.executeQuery();
            while (result.next()) {
                loanId = result.getInt("max");
            }

            String sql3 = "INSERT INTO \"Transactions\" (description, amount, date_of_creation, date_of_execution, account_number_sender, account_number_recipient, loan_id)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?);";

            LocalDate newNextInstalment = nextInstalment;

            statement3 = connection.prepareStatement(sql3);
            statement3.setString(1, "loan instalment");
            statement3.setInt(2, paymentIntervalAmount);
            statement3.setObject(3, creation);
            statement3.setObject(4, newNextInstalment);
            statement3.setInt(5, accountNumber);
            statement3.setInt(6, contraAcount);
            statement3.setInt(7, loanId);
            statement3.executeUpdate();

            numberOfIntervals--;

            while (numberOfIntervals > 0) {
                newNextInstalment = newNextInstalment.plusDays(paymentIntervalDays);

                statement4 = connection.prepareStatement(sql3);
                statement4.setString(1, "loan instalment");
                statement4.setInt(2, paymentIntervalAmount);
                statement4.setObject(3, creation);
                statement4.setObject(4, newNextInstalment);
                statement4.setInt(5, accountNumber);
                statement4.setInt(6, contraAcount);
                statement4.setInt(7, loanId);
                statement4.executeUpdate();

                numberOfIntervals--;
            }

            String sql5 = "COMMIT;";

            statement5 = connection.prepareStatement(sql5);
            statement5.executeUpdate();

            statement.close();
            statement2.close();
            statement3.close();
            statement4.close();
            statement5.close();
            connection.commit();
        }
        closeConnection();
    }
}
