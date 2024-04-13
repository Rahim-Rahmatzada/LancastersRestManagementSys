package Tests.SQLTests;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Ingredient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.assertEquals;

public class InventorySQLTests {
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        // Create an in-memory H2 database for testing
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");

        // Create the Ingredient table in the test database
        String createTableQuery = "CREATE TABLE Ingredient (" +
                "ingredientID INT PRIMARY KEY," +
                "ingredientName VARCHAR(255)," +
                "ingredientCost DOUBLE," +
                "ingredientQuantity INT," +
                "ingredientThreshold INT" +
                ")";
        Statement statement = connection.createStatement();
        statement.execute(createTableQuery);

        // Insert test data into the Ingredient table
        String insertDataQuery = "INSERT INTO Ingredient VALUES " +
                "(1, 'Ingredient 1', 10.0, 100, 50)," +
                "(2, 'Ingredient 2', 20.0, 200, 100)," +
                "(3, 'Ingredient 3', 30.0, 300, 150)";
        statement.execute(insertDataQuery);
    }

    @After
    public void tearDown() throws SQLException {
        // Close the database connection after each test
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testGetIngredientDataFromDatabase() {
        ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

        try (Statement stmt = connection.createStatement();
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
            e.printStackTrace();
        }

        // Verify the expected results
        assertEquals(3, ingredientList.size());

        // Verify the details of each ingredient
        Ingredient ingredient1 = ingredientList.get(0);
        assertEquals(1, ingredient1.getIngredientID());
        assertEquals("Ingredient 1", ingredient1.getName());
        assertEquals(10.0, ingredient1.getCost(), 0.01);
        assertEquals(100, ingredient1.getQuantity());
        assertEquals(50, ingredient1.getThreshold());

        
    }
}