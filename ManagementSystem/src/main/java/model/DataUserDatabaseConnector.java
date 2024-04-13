package model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DataUserDatabaseConnector {
    private static final String DB_URL = loadProperty("db.url");
    private static final String DB_USER = loadProperty("db.user");
    private static final String DB_PASSWORD = loadProperty("db.password");

    private static final String CONFIG_FILE = "database.properties";

    private static String loadProperty(String propertyName) {
        Properties properties = new Properties();
        try (InputStream inputStream = DataUserDatabaseConnector.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            properties.load(inputStream);
            return properties.getProperty(propertyName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }


    // Method to get a database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}




