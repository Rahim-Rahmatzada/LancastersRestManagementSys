package model;

import DatabaseConnections.AdminDatabaseConnector;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Manages user authentication and account creation.
 */
public class LoginManager {

    /**
     * Authenticates a user based on the provided username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean authenticate(String username, String password) {
        String hashedPassword = hashPassword(password);
        try (Connection conn = AdminDatabaseConnector.getConnection()) {
            String query = "SELECT * FROM User WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a new user account with the provided username and password.
     *
     * @param username The username of the new account.
     * @param password The password of the new account.
     * @return true if account creation is successful, false otherwise.
     */
    public boolean createAccount(String username, String password) {
        String hashedPassword = hashPassword(password);
        try (Connection conn = AdminDatabaseConnector.getConnection()) {
            String query = "INSERT INTO User (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Hashes the provided password using SHA-256 algorithm.
     *
     * @param password The password to be hashed.
     * @return The hashed password as a hexadecimal string.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param hash The byte array to be converted.
     * @return The hexadecimal string representation of the byte array.
     */
    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}