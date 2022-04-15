package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Client {

    Connection connection = null;
    PreparedStatement statement = null;

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
            String sql = "UPDATE \"Client\" SET (client_number=?, given_name=?, prefix=?, last_name=?, street_name=?, house_number=?, zip_code=?, town=?, wallet_number=?) WHERE client_number = ?";

            statement = connection.prepareStatement(sql);

            statement.setInt(1, clientNumber);
            statement.setString(2, givenName);
            statement.setString(3, prefix);
            statement.setString(4, lastName);
            statement.setString(5, streetName);
            statement.setInt(6, houseNumber);
            statement.setInt(7, zipCode);
            statement.setString(8, town);
            statement.setInt(9, walletNumber);
            statement.setInt(10, clientNumber);

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

        makeConnection();

        if (connection != null) {
            String sql = "SELECT * FROM \"Client\" WHERE client_number= ? ";

            statement = connection.prepareStatement(sql);

            statement.setInt(1, clientNumber);

            ResultSet result = statement.executeQuery();
            System.out.println("Executed query successfully");

            while (result.next()) {
                String givenName = result.getString("given_name");
                String prefix = result.getString("prefix");
                String lastName = result.getString("last_name");
                String streetName = result.getString("street_name");
                int houseNumber = result.getInt("house_number");
                int zipCode = result.getInt("zip_code");
                String town = result.getString("town");
                int walletNumber = result.getInt("wallet_number");

                System.out.println(clientNumber + " " + givenName + " " + prefix + " " + lastName + " " + streetName
                        + " " + houseNumber + " " + zipCode + " " + town + " " + walletNumber);
            }
        }
        closeConnection();
    }

    public void selectAccounts(int clientNumber) throws SQLException {

        makeConnection();

        if (connection != null) {

        }
        closeConnection();
    }

}
