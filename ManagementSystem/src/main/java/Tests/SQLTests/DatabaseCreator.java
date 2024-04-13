package Tests.SQLTests;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreator {
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

        // Create the Sale table
        String createSaleTableQuery = "CREATE TABLE Sale (" +
                "saleID INT PRIMARY KEY," +
                "date DATE," +
                "numOfCovers INT" +
                ")";
        statement.execute(createSaleTableQuery);

        // Create the Dish table
        String createDishTableQuery = "CREATE TABLE Dish (" +
                "dishID INT PRIMARY KEY," +
                "name VARCHAR(255)," +
                "price DOUBLE," +
                "dishDescription VARCHAR(255)," +
                "allergyInfo VARCHAR(255)," +
                "wineID INT," +
                "FOREIGN KEY (wineID) REFERENCES Wine(wineID)" +
                ")";
        statement.execute(createDishTableQuery);

        statement.close();

        System.out.println("Tables created successfully.");
    }
}