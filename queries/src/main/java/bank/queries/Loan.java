package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Loan {

    public void createLoan(int loanId, int clientNumber, int accountNumber, int contraAcount, int year,
            int month, int day,
            int year2, int month2, int day2, int amountOriginal, int paymentIntervalAmount,
            int paymentIntervalDays) {

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
                String sql = "INSERT INTO \"Loan\" (loan_id, client_number, account_number, contra_account, contract_date, maturity_date, Original_amount, payment_interval_amount, remaining_amount, payment_interval_days)"
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, loanId);
                statement.setInt(2, clientNumber);
                statement.setObject(3, accountNumber);
                statement.setObject(4, contraAcount);
                statement.setObject(5, localDate);
                statement.setObject(6, localDate2);
                statement.setInt(7, amountOriginal);
                statement.setInt(8, paymentIntervalAmount);
                statement.setInt(9, amountOriginal);
                statement.setInt(10, paymentIntervalDays);

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

    public static void paymentInstallment(int loanId) {
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        int amountRemaining = 0;
        int paymentIntervalAmount = 0;
        int account = 0;
        int contra = 0;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "SELECT * FROM \"Loan\" WHERE loan_id = ?";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, loanId);

                ResultSet result = statement.executeQuery();
                System.out.println("Collected data");

                while (result.next()) {
                    amountRemaining = result.getInt("remaining_amount");
                    paymentIntervalAmount = result.getInt("payment_interval_amount");
                    account = result.getInt("account_number");
                    contra = result.getInt("contra_account");
                }

                statement.close();

                if (amountRemaining <= 0) {
                    String sql2 = "DELETE FROM \"Loan\" WHERE loan_id = ?";

                    statement2 = connection.prepareStatement(sql2);
                    statement2.setInt(1, loanId);

                    statement2.executeUpdate();
                    System.out.println("Loan is fully payed and has been removed");
                    statement2.close();

                } else {

                    String sql3 = "BEGIN TRANSACTION;"
                            + "UPDATE \"Loan\" SET remaining_amount = remaining_amount - ? WHERE loan_id = ?;"
                            + "UPDATE \"Account\" SET amount = amount-? WHERE account_number = ?;"
                            + "UPDATE \"Account\" SET amount = amount+? WHERE account_number = ?;"
                            + "COMMIT;";

                    statement3 = connection.prepareStatement(sql3);

                    statement3.setInt(1, paymentIntervalAmount);
                    statement3.setInt(2, loanId);
                    statement3.setInt(3, paymentIntervalAmount);
                    statement3.setInt(4, account);
                    statement3.setInt(5, paymentIntervalAmount);
                    statement3.setInt(6, contra);

                    statement3.executeUpdate();
                    System.out.println("payed installment");
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
