package DatabaseConnections;

import java.sql.*;

public class DataUserDatabaseConnector {
    private static final String DB_URL = "jdbc:mysql://smcse-stuproj00.city.ac.uk:3306/in2033t36";
    private static final String DB_USER = "in2033t36_d";
    private static final String DB_PASSWORD = "uNwu4o0a3ow";




    // Method to get a database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}




