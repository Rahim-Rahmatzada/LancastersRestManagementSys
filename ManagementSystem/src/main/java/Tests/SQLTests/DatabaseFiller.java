package Tests.SQLTests;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseFiller {
    public static void insertData(Connection connection) throws SQLException {
        insertMenuData(connection);
        insertIngredientData(connection);
        insertSaleData(connection);
        insertWineData(connection); // Call insertWineData before insertDishData
        insertDishData(connection);
    }

    public static void insertMenuData(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        // Insert data into the Menu table
        String insertMenuDataQuery = "INSERT INTO Menu (menuID, effectiveDate, menuStatus) VALUES" +
                "(1, '2024-03-01', 'Active')," +
                "(2, '2024-03-08', 'Active')," +
                "(3, '2024-03-15', 'Inactive')";
        statement.execute(insertMenuDataQuery);

        statement.close();
    }

    public static void insertIngredientData(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        // Insert data into the Ingredient table
        String insertIngredientDataQuery = "INSERT INTO Ingredient VALUES " +
                "(1, 'Ingredient 1', 10.0, 100, 50)," +
                "(2, 'Ingredient 2', 20.0, 200, 100)," +
                "(3, 'Ingredient 3', 30.0, 300, 150)";
        statement.execute(insertIngredientDataQuery);

        statement.close();
    }

    public static void insertSaleData(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        // Sample sale data
        String insertSaleDataQuery = "INSERT INTO Sale (saleID, date, numOfCovers) VALUES " +
                "(1, '2024-09-01', 10), " +
                "(2, '2024-09-02', 8), " +
                "(3, '2024-09-02', 12)";
        statement.execute(insertSaleDataQuery);

        statement.close();
    }

    public static void insertDishData(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        String insertDishDataQuery = "INSERT INTO Dish (dishID, name, price, dishDescription, allergyInfo, wineID) VALUES " +
                "(1, 'Pasta Carbonara', 14.50, 'Creamy pasta dish with bacon and egg', 'Eggs, Dairy', 1), " +
                "(2, 'Fish and Chips', 12.00, 'Classic British dish', 'Fish', 2), " +
                "(3, 'Grilled Steak', 18.00, 'Served with vegetables', 'None', 3)";
               // "(4, 'Caesar Salad', 9.50, 'Crisp romaine lettuce with Caesar dressing', 'Dairy, Anchovies', 4)";
        statement.execute(insertDishDataQuery);

        statement.close();
    }

    public static void insertWineData(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        String insertWineDataQuery = "INSERT INTO Wine (wineID, name, type, quantity) VALUES " +
                "(1, 'Cabernet Sauvignon', 'Red', 20), " +
                "(2, 'Sauvignon Blanc', 'White', 15), " +
                "(3, 'Merlot', 'Red', 18)";
               // "(4, 'Chardonnay', 'White', 22)";
        statement.execute(insertWineDataQuery);

        statement.close();
    }
}