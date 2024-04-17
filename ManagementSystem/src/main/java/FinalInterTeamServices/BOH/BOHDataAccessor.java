package FinalInterTeamServices.BOH;

import model.*;
import DatabaseConnections.DataUserDatabaseConnector;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The BOHDataAccessor class provides methods to access and retrieve data
 * related to Back-of-House (BOH) operations from our team's database with only user privileges.
 */


public class BOHDataAccessor implements BOHFinalInterface {



    @Override
    public List<Ingredient> getStockLevels() {
        List<Ingredient> stockLevels = new ArrayList<>();

        try (Connection connection = DataUserDatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Ingredient")) {

            while (resultSet.next()) {
                int ingredientID = resultSet.getInt("ingredientID");
                String name = resultSet.getString("ingredientName");
                double cost = resultSet.getDouble("ingredientCost");
                int quantity = resultSet.getInt("ingredientQuantity");
                int threshold = resultSet.getInt("ingredientThreshold");

                Ingredient ingredient = new Ingredient(ingredientID, name, cost, quantity, threshold);
                stockLevels.add(ingredient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockLevels;
    }

    @Override
    public List<Order> getStockOrders() {
        List<Order> stockOrders = new ArrayList<>();

        try (Connection connection = DataUserDatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM StockOrders")) {

            while (resultSet.next()) {
                int orderID = resultSet.getInt("orderID");
                Date dateOrdered = resultSet.getDate("dateOrdered");
                Date expectedDeliveryDate = resultSet.getDate("expectedDeliveryDate");
                String orderStatus = resultSet.getString("orderStatus");

                Order stockOrder = new Order(orderID, dateOrdered.toLocalDate(), expectedDeliveryDate.toLocalDate(), orderStatus);
                stockOrders.add(stockOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockOrders;
    }

    @Override
    public List<Dish> getMenuDishesAndWineInfo(int menuID) {
        List<Dish> menuDishes = new ArrayList<>();

        try (Connection connection = DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT d.dishID, d.name, d.price, d.dishDescription, d.allergyInfo, d.wineID " +
                             "FROM Dish d " +
                             "JOIN Menu_Dish md ON d.dishID = md.dishID " +
                             "WHERE md.menuID = ?")) {

            statement.setInt(1, menuID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int dishID = resultSet.getInt("dishID");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String description = resultSet.getString("dishDescription");
                String allergyInfo = resultSet.getString("allergyInfo");
                int wineID = resultSet.getInt("wineID");

                Dish dish = new Dish(dishID, name, price, description, allergyInfo, wineID);
                menuDishes.add(dish);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menuDishes;
    }

    @Override
    public Menu getSpecificMenuDetails(int menuID) {
        Menu menu = null;

        try (Connection connection = DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT menuID, effectiveDate, menuStatus " +
                             "FROM Menu " +
                             "WHERE menuID = ?")) {

            statement.setInt(1, menuID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("menuID");
                LocalDate effectiveDate = resultSet.getDate("effectiveDate").toLocalDate();
                String menuStatus = resultSet.getString("menuStatus");

                menu = new Menu(id, effectiveDate, menuStatus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menu;
    }

    @Override
    public List<Menu> getMenuDetails() {
        List<Menu> menus = new ArrayList<>();

        try (Connection connection = DataUserDatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT menuID, effectiveDate, menuStatus FROM Menu")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("menuID");
                LocalDate effectiveDate = resultSet.getDate("effectiveDate").toLocalDate();
                String menuStatus = resultSet.getString("menuStatus");

                Menu menu = new Menu(id, effectiveDate, menuStatus);
                menus.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menus;
    }

    @Override
    public List<Order> getStockOrdersWithinDate(LocalDate startDate, LocalDate endDate) {
        List<Order> stockOrders = new ArrayList<>();

        try (Connection connection = DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT orderID, dateOrdered, expectedDeliveryDate, orderStatus " +
                             "FROM StockOrders " +
                             "WHERE expectedDeliveryDate BETWEEN ? AND ?")) {

            statement.setDate(1, java.sql.Date.valueOf(startDate));
            statement.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int orderID = resultSet.getInt("orderID");
                Date dateOrdered = resultSet.getDate("dateOrdered");
                Date expectedDeliveryDate = resultSet.getDate("expectedDeliveryDate");
                String orderStatus = resultSet.getString("orderStatus");

                Order stockOrder = new Order(orderID, dateOrdered.toLocalDate(), expectedDeliveryDate.toLocalDate(), orderStatus);
                stockOrders.add(stockOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockOrders;
    }

    @Override
    public List<Order> getStockOrdersByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, String orderStatus) {
        List<Order> stockOrders = new ArrayList<>();

        try (Connection connection = DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT orderID, dateOrdered, expectedDeliveryDate, orderStatus " +
                             "FROM StockOrders " +
                             "WHERE dateOrdered BETWEEN ? AND ? AND orderStatus = ?")) {

            statement.setDate(1, java.sql.Date.valueOf(startDate));
            statement.setDate(2, java.sql.Date.valueOf(endDate));
            statement.setString(3, orderStatus);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int orderID = resultSet.getInt("orderID");
                Date dateOrdered = resultSet.getDate("dateOrdered");
                Date expectedDeliveryDate = resultSet.getDate("expectedDeliveryDate");
                String status = resultSet.getString("orderStatus");

                Order stockOrder = new Order(orderID, dateOrdered.toLocalDate(), expectedDeliveryDate.toLocalDate(), status);
                stockOrders.add(stockOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockOrders;
    }

    @Override
    public List<Menu> getMenusByStatus(String status) {
        List<Menu> menus = new ArrayList<>();

        try (Connection connection = DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT menuID, effectiveDate, menuStatus " +
                             "FROM Menu " +
                             "WHERE menuStatus = ?")) {

            statement.setString(1, status);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("menuID");
                LocalDate effectiveDate = resultSet.getDate("effectiveDate").toLocalDate();
                String menuStatus = resultSet.getString("menuStatus");

                Menu menu = new Menu(id, effectiveDate, menuStatus);
                menus.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menus;
    }

    @Override
    public Wine getWineByDish(int dishID) {
        Wine wine = null;

        try (Connection connection = DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT w.wineID, w.wineName, w.wineType, w.wineVintage, w.wineQuantity " +
                             "FROM Dish d " +
                             "JOIN Wine w ON d.wineID = w.wineID " +
                             "WHERE d.dishID = ?")) {

            statement.setInt(1, dishID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("wineID");
                String name = resultSet.getString("wineName");
                String type = resultSet.getString("wineType");
                int vintage = resultSet.getInt("wineVintage");
                int quantity = resultSet.getInt("wineQuantity");

                wine = new Wine(id, name, type, vintage, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wine;
    }
}