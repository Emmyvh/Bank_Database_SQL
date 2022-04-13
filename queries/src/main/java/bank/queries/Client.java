package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Client {

    public void createClient(int clientNumber, String givenName, String prefix, String lastName, String streetName,
            int houseNumber, int zipCode, String town) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "INSERT INTO \"Client\" (client_number, given_name, prefix, last_name, street_name, house_number, zip_code, town) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, clientNumber);
                statement.setString(2, givenName);
                statement.setString(3, prefix);
                statement.setString(4, lastName);
                statement.setString(5, streetName);
                statement.setInt(6, houseNumber);
                statement.setInt(7, zipCode);
                statement.setString(8, town);

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

    public void UpdateClient(int clientNumber, String givenName, String prefix, String lastName, String streetName,
            int houseNumber, int zipCode, String town, int walletNumber) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

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
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Error in database connection");
        }
        System.exit(0);
    }

    public void deleteClient(int clientNumber) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "DELETE FROM \"Client\" WHERE client_number= ? ";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, clientNumber);

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

    public void selectClient(int number) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Bank_Database", "postgres", "123");

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            if (connection != null) {
                String sql = "SELECT * FROM \"Client\" WHERE client_number= ? ";

                statement = connection.prepareStatement(sql);

                statement.setInt(1, number);

                ResultSet result = statement.executeQuery();
                System.out.println("Executed query successfully");

                while (result.next()) {
                    int clientNumber = result.getInt("client_number");
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
