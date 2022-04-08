package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Wallet {

    public void createWallet(int walletNumber) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "INSERT INTO \"Wallet\" (wallet_number) "
                        + "VALUES (?)";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, walletNumber);

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

    public void addCurrentAccountToWallet(int accountNumber, int walletNumber) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "UPDATE \"Wallet\" SET account_number_current = ? WHERE wallet_number = ?";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, accountNumber);
                statement.setInt(2, walletNumber);

                statement.executeUpdate();
                System.out.println("Executed query successfully");

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

    public void addSavingsAccountToWallet(int accountNumber, int walletNumber) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "UPDATE \"Wallet\" SET account_number_savings = ? WHERE wallet_number = ?";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, accountNumber);
                statement.setInt(2, walletNumber);

                statement.executeUpdate();
                System.out.println("Executed query successfully");

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

    public void addInvestAccountToWallet(int accountNumber, int walletNumber) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "UPDATE \"Wallet\" SET account_number_invest = ? WHERE wallet_number = ?";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, accountNumber);
                statement.setInt(2, walletNumber);

                statement.executeUpdate();
                System.out.println("Executed query successfully");

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

    public void deleteWallet(int walletNumber) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "DELETE FROM \"Wallet\" WHERE wallet_number= ? ";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, walletNumber);

                statement.executeUpdate();
                System.out.println("Executed query successfully");

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

    public void selectWallet(int number) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "SELECT * FROM \"Wallet\" WHERE wallet_number= ? ";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, number);

                ResultSet result = statement.executeQuery();
                System.out.println("Executed query successfully");

                while (result.next()) {
                    int walletNumber = result.getInt("wallet_number");
                    String accountNumberCurrent = result.getString("account_number_current");
                    String accountNumberSavings = result.getString("account_number_savings");
                    String accountNumberInvest = result.getString("account_number_invest");

                    System.out.println(walletNumber + " " + accountNumberCurrent + " " + accountNumberSavings + " "
                            + accountNumberInvest);
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
