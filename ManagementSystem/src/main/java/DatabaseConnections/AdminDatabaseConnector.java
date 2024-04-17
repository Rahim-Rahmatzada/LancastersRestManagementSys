package DatabaseConnections;

import java.sql.*;

/**
 * The AdminDatabaseConnector class provides methods to establish a connection
 * to the database used by the admin user.
 */

public class AdminDatabaseConnector {
    private static final String DB_URL = "jdbc:mysql://smcse-stuproj00.city.ac.uk:3306/in2033t36";
    private static final String DB_USER = "in2033t36_a";
    private static final String DB_PASSWORD = "xIYZBnWNwEA";


    /**
     * Establishes a connection to the database.
     * @return A Connection object representing the database connection.
     * @throws SQLException If a database access error occurs or the URL is null.
     */

    // Method to get a database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}