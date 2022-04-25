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
            int houseNumber, int zipCode, String town) throws SQLException {

        makeConnection();

        if (connection != null) {
            String sql = "UPDATE \"Client\" SET (given_name=?, prefix=?, last_name=?, street_name=?, house_number=?, zip_code=?, town=?) WHERE client_number = ?";

            statement = connection.prepareStatement(sql);
            statement.setString(1, givenName);
            statement.setString(2, prefix);
            statement.setString(3, lastName);
            statement.setString(4, streetName);
            statement.setInt(5, houseNumber);
            statement.setInt(6, zipCode);
            statement.setString(7, town);
            statement.setInt(8, clientNumber);
            statement.executeUpdate();

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
        makeConnection();

        if (connection != null) {
            String sql = "SELECT * FROM \"Client\" WHERE client_number= ? ";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, clientNumber);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                givenName = result.getString("given_name");
                prefix = result.getString("prefix");
                lastName = result.getString("last_name");
                streetName = result.getString("street_name");
                houseNumber = result.getInt("house_number");
                zipCode = result.getInt("zip_code");
                town = result.getString("town");
            }

            System.out.println(clientNumber + " " + givenName + " " + prefix + " " + lastName + " " + streetName
                    + " " + houseNumber + " " + zipCode + " " + town);

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
                    + "INNER JOIN \"Wallet\" ON \"Client\".client_number = \"Wallet\".client_number "
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
                    + "INNER JOIN \"Wallet\" ON \"Client\".client_number = \"Wallet\".client_number "
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

    public void transactionTotalBalance(int clientNumber) throws SQLException {

        int amountSend = 0;
        int amountSendTotal = 0;
        int amountReceived = 0;
        int amountReceivedTotal = 0;
        int total = 0;

        makeConnection();

        if (connection != null) {
            String sql = "SELECT \"Client\".client_number, \"Wallet\".wallet_number, \"Account\".wallet_number, \"Account\".account_number, \"Transactions\".account_number_sender, \"Transactions\".amount "
                    + "FROM \"Client\" "
                    + "INNER JOIN \"Wallet\" ON \"Client\".client_number = \"Wallet\".client_number "
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
                    + "INNER JOIN \"Wallet\" ON \"Client\".client_number = \"Wallet\".client_number "
                    + "INNER JOIN \"Account\" ON \"Wallet\".wallet_number = \"Account\".wallet_number "
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

    public void transactions(int clientNumber) throws SQLException {

        int id = 0;
        String description = "";
        int amount = 0;
        String dateCreation = "";
        String dateExecution = "";
        int sender = 0;
        int recipient = 0;
        int loanId = 0;

        makeConnection();

        if (connection != null) {
            String sql = "SELECT \"Transactions\".transaction_id, \"Transactions\".description, \"Transactions\".amount, "
                    + "\"Transactions\".date_of_creation, \"Transactions\".date_of_execution, \"Transactions\".account_number_sender, "
                    + "\"Transactions\".account_number_recipient, \"Transactions\".loan_id "
                    + "FROM \"Client\" "
                    + "INNER JOIN \"Wallet\" ON \"Client\".client_number = \"Wallet\".client_number "
                    + "INNER JOIN \"Account\" ON \"Wallet\".wallet_number = \"Account\".wallet_number "
                    + "INNER JOIN \"Transactions\" ON \"Account\".account_number= \"Transactions\".account_number_recipient OR \"Account\".account_number= \"Transactions\".account_number_sender "
                    + "WHERE \"Client\".client_number = ? ";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, clientNumber);
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

                System.out.println("client number: " + clientNumber + ", transaction id: " + id + ", description: "
                        + description + ", amount: " + amount + ", date of creation: " + dateCreation
                        + ", date of Execution: " + dateExecution + ", account of sender: " + sender
                        + ", account of recipient: " + recipient + ", loan id (optional): " + loanId);
            }

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

    public void loans(int clientNumber) throws SQLException {

        int loanId = 0;
        int account = 0;
        int contra = 0;
        String dateCreation = "";
        String dateMaturity = "";
        int amount = 0;
        int paymentIntervalAmount = 0;
        int paymentIntervalDays = 0;

        makeConnection();

        if (connection != null) {
            String sql = "SELECT \"Outstanding_Loan\".loan_id, \"Outstanding_Loan\".account_number, \"Outstanding_Loan\".contra_account, "
                    + "\"Outstanding_Loan\".contract_date, \"Outstanding_Loan\".maturity_date, \"Outstanding_Loan\".original_amount, "
                    + "\"Outstanding_Loan\".payment_interval_amount, \"Outstanding_Loan\".payment_interval_days "
                    + "FROM \"Client\" "
                    + "INNER JOIN \"Wallet\" ON \"Client\".client_number = \"Wallet\".client_number "
                    + "INNER JOIN \"Account\" ON \"Wallet\".wallet_number = \"Account\".wallet_number "
                    + "INNER JOIN \"Outstanding_Loan\" ON \"Account\".account_number= \"Outstanding_Loan\".account_number OR \"Account\".account_number= \"Outstanding_Loan\".contra_account "
                    + "WHERE \"Client\".client_number = ? ";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, clientNumber);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                loanId = result.getInt("loan_id");
                account = result.getInt("account_number");
                contra = result.getInt("contra_account");
                dateCreation = result.getString("contract_date");
                dateMaturity = result.getString("maturity_date");
                amount = result.getInt("original_amount");
                paymentIntervalAmount = result.getInt("payment_interval_amount");
                paymentIntervalDays = result.getInt("payment_interval_days");

                System.out.println("client number: " + clientNumber + ", loan id: " + loanId + ", account: "
                        + account + ", contra: " + contra + ", date of creation: " + dateCreation
                        + ", date of Maturity: " + dateMaturity + ", Amount: " + amount
                        + ", payment Interval Amount: " + paymentIntervalAmount + ", payment Interval Days: "
                        + paymentIntervalDays);
            }

            statement.close();
            connection.commit();
        }
        closeConnection();
    }

}
