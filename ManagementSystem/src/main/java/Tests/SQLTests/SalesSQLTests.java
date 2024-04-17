package Tests.SQLTests;

import model.Sale;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for verifying SQL operations related to Sales UI.
 */
public class SalesSQLTests {
    private static Connection staticConnection;


    @BeforeClass
    public static void setupClass() throws SQLException {
        // Create an in-memory H2 database for testing
        staticConnection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");

        // Set up the database (create tables and insert data)
        new SalesSQLTests().setupDatabase(staticConnection);
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

    /**
     * Tests the retrieval of sale data from the database.
     */
    @Test
    public void testGetSaleDataFromDatabase() {
        List<Sale> saleList = new ArrayList<>();

        try (Statement stmt = staticConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT saleID, date, numOfCovers FROM Sale")) {

            while (rs.next()) {
                int saleID = rs.getInt("saleID");
                Date date = rs.getDate("date");
                int numOfCovers = rs.getInt("numOfCovers");

                Sale sale = new Sale(saleID, date, numOfCovers);
                saleList.add(sale);
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve sale data: " + e.getMessage());
        }

        // Verify the expected results
        if (saleList.size() == 3) {
            System.out.println("Test passed: Sale list size is correct.");
        } else {
            System.out.println("Test failed: Expected 3 sales, but found " + saleList.size());
        }

        // Verify the details of each sale
        Sale sale1 = saleList.get(0);
        if (sale1.getSaleID() == 1 &&
                sale1.getDate().equals(Date.valueOf("2024-09-01")) &&
                sale1.getNumOfCovers() == 10) {
            System.out.println("Test passed: Sale 1 details are correct.");
        } else {
            System.out.println("Test failed: Incorrect details for Sale 1.");
        }
    }
}

//    @Test
//    public void testGetDishDataFromDatabase() {
//        // Implement a test to retrieve dish data from the database
//        // Similar to the testGetSaleDataFromDatabase() method
//    }
//
//    @Test
//    public void testGetWineDataFromDatabase() {
//        // Implement a test to retrieve wine data from the database
//        // Similar to the testGetSaleDataFromDatabase() method
//    }