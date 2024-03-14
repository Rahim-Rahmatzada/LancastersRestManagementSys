import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String URL = "jdbc:mysql://smcse-stuproj00.city.ac.uk:3306/in2033t36";
    private static final String USER = "in2033t36_a"; // replace with your username
    private static final String PASSWORD = "xIYZBnWNwEA"; // replace with your password

    public static void main(String[] args) {
        // Ensure the JDBC driver is available.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found.");
            e.printStackTrace();
            return;
        }

        // Connect to the database and retrieve sales data.
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Sale");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int saleId = resultSet.getInt("saleID");
                java.sql.Date date = resultSet.getDate("date");
                double totalSales = resultSet.getDouble("totalSales");
                int numOfCovers = resultSet.getInt("numOfCovers");

                System.out.printf("Sale ID: %d, Date: %s, Total Sales: %.2f, Number of Covers: %d%n",
                        saleId, date, totalSales, numOfCovers);
            }

        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }
    }
}
