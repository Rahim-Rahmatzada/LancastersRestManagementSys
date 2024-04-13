package Tests.SQLTests;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseFiller {
    public static void insertData(Connection connection) throws SQLException {
        insertMenuData(connection);
        insertIngredientData(connection);
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
}