package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Client {

    Connection connection = null;
    PreparedStatement statement = null;
    PreparedStatement statement2 = null;

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

    public void createClient(String givenName, String prefix, String lastName, String streetName,
            int houseNumber, int zipCode, String town) throws SQLException {

        makeConnection();

        if (connection != null) {
            String sql = "INSERT INTO \"Client\" (given_name, prefix, last_name, street_name, house_number, zip_code, town) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?);";

            statement = connection.prepareStatement(sql);

            statement.setString(1, givenName);
            statement.setString(2, prefix);
            statement.setString(3, lastName);
            statement.setString(4, streetName);
            statement.setInt(5, houseNumber);
            statement.setInt(6, zipCode);
            statement.setString(7, town);

            statement.executeUpdate();
            System.out.println("Executed query successfully");

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void UpdateClient(int clientNumber, String givenName, String prefix, String lastName, String streetName,
            int houseNumber, int zipCode, String town, int walletNumber) throws SQLException {

        makeConnection();

        if (connection != null) {
            String sql = "UPDATE \"Client\" SET (given_name=?, prefix=?, last_name=?, street_name=?, house_number=?, zip_code=?, town=?, wallet_number=?) WHERE client_number = ?";

            statement = connection.prepareStatement(sql);

            statement.setString(1, givenName);
            statement.setString(2, prefix);
            statement.setString(3, lastName);
            statement.setString(4, streetName);
            statement.setInt(5, houseNumber);
            statement.setInt(6, zipCode);
            statement.setString(7, town);
            statement.setInt(8, walletNumber);
            statement.setInt(9, clientNumber);

            statement.executeUpdate();
            System.out.println("Executed query successfully");

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void deleteClient(int clientNumber) throws SQLException {

        makeConnection();

        if (connection != null) {
            String sql = "DELETE FROM \"Client\" WHERE client_number= ? ";

            statement = connection.prepareStatement(sql);

            statement.setInt(1, clientNumber);

            statement.executeUpdate();
            System.out.println("Executed query successfully");

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void selectClient(int clientNumber) throws SQLException {

        String givenName = "";
        String prefix = "";
        String lastName = "";
        String streetName = "";
        int houseNumber = 0;
        int zipCode = 0;
        String town = "";
        int walletNumber = 0;
        makeConnection();

        if (connection != null) {
            String sql = "SELECT * FROM \"Client\" WHERE client_number= ? ";

            statement = connection.prepareStatement(sql);

            statement.setInt(1, clientNumber);

            ResultSet result = statement.executeQuery();
            System.out.println("Executed query successfully");

            while (result.next()) {
                givenName = result.getString("given_name");
                prefix = result.getString("prefix");
                lastName = result.getString("last_name");
                streetName = result.getString("street_name");
                houseNumber = result.getInt("house_number");
                zipCode = result.getInt("zip_code");
                town = result.getString("town");
                walletNumber = result.getInt("wallet_number");
            }

            System.out.println(clientNumber + " " + givenName + " " + prefix + " " + lastName + " " + streetName
                    + " " + houseNumber + " " + zipCode + " " + town + " " + walletNumber);

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void selectAccounts(int clientNumber) throws SQLException {
        int account = 0;
        String type = "";
        int amount = 0;

        makeConnection();

        if (connection != null) {

            String sql = "SELECT \"Client\".client_number, \"Wallet\".wallet_number, \"Account\".account_number, \"Account\".account_type, \"Account\".amount, \"Account\".wallet_number "
                    + "FROM \"Client\" "
                    + "INNER JOIN \"Wallet\" ON \"Client\".wallet_number = \"Wallet\".wallet_number "
                    + "INNER JOIN \"Account\" ON \"Wallet\".wallet_number = \"Account\".wallet_number "
                    + "WHERE client_number = ? ";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, clientNumber);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                account = result.getInt("account_number");
                type = result.getString("account_type");
                amount = result.getInt("amount");

                System.out.println("client number: " + clientNumber + ", account: " + account + ", type: " + type
                        + ", balance: " + amount);
            }
            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void balance(int clientNumber) throws SQLException {

        int amount = 0;
        int total = 0;

        makeConnection();

        if (connection != null) {
            String sql = "SELECT \"Client\".client_number, \"Wallet\".wallet_number, \"Account\".wallet_number, \"Account\".amount "
                    + "FROM \"Client\" "
                    + "INNER JOIN \"Wallet\" ON \"Client\".wallet_number = \"Wallet\".wallet_number "
                    + "INNER JOIN \"Account\" ON \"Wallet\".wallet_number= \"Account\".wallet_number "
                    + "WHERE \"Client\".client_number = ? ";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, clientNumber);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                amount = result.getInt("amount");

                total = total + amount;
            }

            System.out.println("client number: " + clientNumber + ", account value: " + total);

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void transactionTotal(int clientNumber) throws SQLException {

        int amountSend = 0;
        int amountSendTotal = 0;
        int amountReceived = 0;
        int amountReceivedTotal = 0;
        int total = 0;

        makeConnection();

        if (connection != null) {
            String sql = "SELECT \"Client\".client_number, \"Wallet\".wallet_number, \"Account\".wallet_number, \"Account\".account_number, \"Transactions\".account_number_sender, \"Transactions\".amount "
                    + "FROM \"Client\" "
                    + "INNER JOIN \"Wallet\" ON \"Client\".wallet_number = \"Wallet\".wallet_number "
                    + "INNER JOIN \"Account\" ON \"Wallet\".wallet_number= \"Account\".wallet_number "
                    + "INNER JOIN \"Transactions\" ON \"Account\".account_number= \"Transactions\".account_number_sender "
                    + "WHERE \"Client\".client_number = ? ";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, clientNumber);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                amountSend = result.getInt("amount");

                total = total - amountSend;
                amountSendTotal = amountSendTotal + amountSend;
            }

            String sql2 = "SELECT \"Client\".client_number, \"Wallet\".wallet_number, \"Account\".wallet_number, \"Account\".account_number, \"Transactions\".account_number_recipient, \"Transactions\".amount "
                    + "FROM \"Client\" "
                    + "INNER JOIN \"Wallet\" ON \"Client\".wallet_number = \"Wallet\".wallet_number "
                    + "INNER JOIN \"Account\" ON \"Wallet\".wallet_number= \"Account\".wallet_number "
                    + "INNER JOIN \"Transactions\" ON \"Account\".account_number= \"Transactions\".account_number_recipient "
                    + "WHERE \"Client\".client_number = ? ";

            statement2 = connection.prepareStatement(sql2);
            statement2.setInt(1, clientNumber);
            ResultSet result2 = statement2.executeQuery();

            while (result2.next()) {
                amountReceived = result2.getInt("amount");

                total = total + amountReceived;
                amountReceivedTotal = amountReceivedTotal + amountReceived;
            }

            System.out.println("client number: " + clientNumber + ", send: -" + amountSendTotal + ", received: "
                    + amountReceivedTotal + ", transaction total: " + total);

            statement.close();
            statement2.close();
            connection.commit();
        }
        closeConnection();
    }

}
