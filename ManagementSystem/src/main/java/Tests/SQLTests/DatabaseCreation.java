package Tests.SQLTests;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreation {
    public static void createTables(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        System.out.println("Creating tables...");


        // Create the Menu table
        String createMenuTableQuery = "CREATE TABLE Menu (" +
                "menuID INT PRIMARY KEY," +
                "effectiveDate DATE," +
                "menuStatus VARCHAR(255)" +
                ")";
        statement.execute(createMenuTableQuery);

        // Create the Wine table
        String createWineTableQuery = "CREATE TABLE Wine (" +
                "wineID INT PRIMARY KEY," +
                "name VARCHAR(255)," +
                "type VARCHAR(255)," +
                "quantity INT" +
                ")";
        statement.execute(createWineTableQuery);

        // Create the StockOrders table
        String createStockOrdersTableQuery = "CREATE TABLE StockOrders (" +
                "orderID INT PRIMARY KEY," +
                "dateOrdered DATE," +
                "expectedDeliveryDate DATE," +
                "orderStatus VARCHAR(255)" +
                ")";
        statement.execute(createStockOrdersTableQuery);

        // Create the Ingredient table
        String createIngredientTableQuery = "CREATE TABLE Ingredient (" +
                "ingredientID INT PRIMARY KEY," +
                "ingredientName VARCHAR(255)," +
                "ingredientCost DOUBLE," +
                "ingredientQuantity INT," +
                "ingredientThreshold INT" +
                ")";
        statement.execute(createIngredientTableQuery);

        // Create other tables as needed

        statement.close();

        System.out.println("Tables created successfully.");

    }


}