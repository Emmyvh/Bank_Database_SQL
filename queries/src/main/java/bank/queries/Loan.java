package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Loan {

    public void createLoan(int loanId, int accountNumber, int contraAcount, int year, int month, int day, int year2,
            int month2, int day2, int amountOriginal, int paymentIntervalAmount, int paymentIntervalDays) {

        Connection connection = null;
        PreparedStatement statement = null;
        LocalDate creation = LocalDate.of(year, month, day);
        LocalDate maturity = LocalDate.of(year2, month2, day2);
        LocalDate nextInstalment = LocalDate.of(year, month, (day + paymentIntervalDays));

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "BEGIN TRANSACTION;"
                        + "INSERT INTO \"Outstanding_Loan\" (loan_id, account_number, contra_account, contract_date, maturity_date, original_amount, payment_interval_amount, remaining_amount, payment_interval_days, date_next_instalment)"
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"

                        + "COMMIT;";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, loanId);
                statement.setObject(2, accountNumber);
                statement.setObject(3, contraAcount);
                statement.setObject(4, creation);
                statement.setObject(5, maturity);
                statement.setInt(6, amountOriginal);
                statement.setInt(7, paymentIntervalAmount);
                statement.setInt(8, amountOriginal);
                statement.setInt(9, paymentIntervalDays);
                statement.setObject(10, nextInstalment);

                statement.executeUpdate();
                System.out.println("Created loan");

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

    public void payInstalment(int loanId) {
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        int account = 0;
        int contra = 0;
        int paymentIntervalAmount = 0;
        int amountRemaining = 0;
        int paymentIntervalDays = 0;
        String nextInstalment = "";

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "SELECT * FROM \"Outstanding_Loan\" WHERE loan_id = ?";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, loanId);

                ResultSet result = statement.executeQuery();
                System.out.println("Collected data");

                while (result.next()) {
                    account = result.getInt("account_number");
                    contra = result.getInt("contra_account");
                    paymentIntervalAmount = result.getInt("payment_interval_amount");
                    amountRemaining = result.getInt("remaining_amount");
                    paymentIntervalDays = result.getInt("payment_interval_days");
                    nextInstalment = result.getString("date_next_instalment");
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
                LocalDate nextInstalmentdate = LocalDate.parse(nextInstalment, formatter);
                LocalDate newNextInstalment = nextInstalmentdate.plusDays(paymentIntervalDays);
                LocalDate today = LocalDate.now();

                statement.close();

                if (amountRemaining <= 0 && newNextInstalment == today) {
                    String sql2 = "UPDATE \"Outstanding_Loan\" SET date_next_instalment = ? WHERE loan_id = ?";

                    statement2 = connection.prepareStatement(sql2);

                    statement2.setObject(1, null);
                    statement2.setInt(2, loanId);

                    statement2.executeUpdate();
                    System.out.println("Loan is already fully payed");
                    statement2.close();

                } else {

                    String sql3 = "BEGIN TRANSACTION;"
                            + "UPDATE \"Outstanding_Loan\" SET remaining_amount = remaining_amount - ?, date_next_instalment = ? WHERE loan_id = ?;"
                            + "UPDATE \"Account\" SET amount = amount-? WHERE account_number = ?;"
                            + "UPDATE \"Account\" SET amount = amount+? WHERE account_number = ?;"
                            + "COMMIT;";

                    statement3 = connection.prepareStatement(sql3);

                    statement3.setInt(1, paymentIntervalAmount);
                    statement3.setObject(2, newNextInstalment);
                    statement3.setInt(3, loanId);
                    statement3.setInt(4, paymentIntervalAmount);
                    statement3.setInt(5, account);
                    statement3.setInt(6, paymentIntervalAmount);
                    statement3.setInt(7, contra);

                    statement3.executeUpdate();
                    System.out.println("payed instalment");
                    statement3.close();
                }

                System.out.println("End of query");

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
