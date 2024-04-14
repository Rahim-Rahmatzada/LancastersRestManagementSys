package Tests.SQLTests;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Ingredient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;

public class InventorySQLTests {
    private static Connection staticConnection;

    @BeforeClass
    public static void setupClass() throws SQLException {
        // Create an in-memory H2 database for testing
        staticConnection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");

        // Set up the database (create tables and insert data)
        new InventorySQLTests().setupDatabase(staticConnection);
    }

    private void setupDatabase(Connection connection) throws SQLException {
        System.out.println("Setting up the database...");
        DatabaseCreator.createTables(connection);
        DatabaseFiller.insertData(connection);
    }

    @After
    public void tearDown() {
        // No code needed here
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        // Close the database connection after all tests
        if (staticConnection != null) {
            staticConnection.close();
        }
    }

    @Test
    public void testGetIngredientDataFromDatabase() {
        ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

        try (Statement stmt = staticConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ingredientID, ingredientName, ingredientCost, ingredientQuantity, ingredientThreshold FROM Ingredient")) {

            while (rs.next()) {
                int ingredientID = rs.getInt("ingredientID");
                String name = rs.getString("ingredientName");
                double cost = rs.getDouble("ingredientCost");
                int quantity = rs.getInt("ingredientQuantity");
                int threshold = rs.getInt("ingredientThreshold");

                Ingredient ingredient = new Ingredient(ingredientID, name, cost, quantity, threshold);
                ingredientList.add(ingredient);
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve ingredient data: " + e.getMessage());
        }

        // Verify the expected results
        if (ingredientList.size() == 3) {
            System.out.println("Test passed: Ingredient list size is correct.");
        } else {
            System.out.println("Test failed: Expected 3 ingredients, but found " + ingredientList.size());
        }

        // Verify the details of each ingredient
        Ingredient ingredient1 = ingredientList.get(0);
        if (ingredient1.getIngredientID() == 1 &&
                ingredient1.getName().equals("Ingredient 1") &&
                Math.abs(ingredient1.getCost() - 10.0) < 0.01 &&
                ingredient1.getQuantity() == 100 &&
                ingredient1.getThreshold() == 50) {
            System.out.println("Test passed: Ingredient 1 details are correct.");
        } else {
            System.out.println("Test failed: Incorrect details for Ingredient 1.");
        }
    }

    @Test
    public void testGetMenuData() throws SQLException {
        try (Statement stmt = staticConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Menu")) {

            while (rs.next()) {
                int menuID = rs.getInt("menuID");
                Date effectiveDate = rs.getDate("effectiveDate");
                String menuStatus = rs.getString("menuStatus");

                System.out.println("Menu ID: " + menuID + ", Effective Date: " + effectiveDate + ", Status: " + menuStatus);
            }
        }
    }
}