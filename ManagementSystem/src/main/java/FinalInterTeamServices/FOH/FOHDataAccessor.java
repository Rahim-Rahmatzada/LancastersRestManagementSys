package FinalInterTeamServices.FOH;

import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FOHDataAccessor implements FOHFinalInterface {

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
    public List<Dish> getDishesByMenu(int menuID) {
        List<Dish> dishes = new ArrayList<>();

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
                dishes.add(dish);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dishes;
    }

    @Override
    public Dish getDishDetails(int dishID) {
        Dish dish = null;

        try (Connection connection = DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT d.dishID, d.name, d.price, d.dishDescription, d.allergyInfo, d.wineID, w.name AS wineName " +
                             "FROM Dish d " +
                             "LEFT JOIN Wine w ON d.wineID = w.wineID " +
                             "WHERE d.dishID = ?")) {

            statement.setInt(1, dishID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("dishID");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String description = resultSet.getString("dishDescription");
                String allergyInfo = resultSet.getString("allergyInfo");
                int wineID = resultSet.getInt("wineID");
                String wineName = resultSet.getString("wineName");

                dish = new Dish(id, name, price, description, allergyInfo, wineID);
                dish.setWineName(wineName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dish;
    }

    @Override
    public Wine getWineByDish(int dishID) {
        Wine wine = null;

        try (Connection connection = DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT w.wineID, w.name, w.type, w.quantity " +
                             "FROM Dish d " +
                             "JOIN Wine w ON d.wineID = w.wineID " +
                             "WHERE d.dishID = ?")) {

            statement.setInt(1, dishID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("wineID");
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                int quantity = resultSet.getInt("quantity");

                wine = new Wine(id, name, type, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wine;
    }

    @Override
    public double getDishPrice(int dishID) {
        double price = 0.0;

        try (Connection connection = DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT price FROM Dish WHERE dishID = ?")) {

            statement.setInt(1, dishID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                price = resultSet.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return price;
    }

    @Override
    public double getWinePrice(int wineID) {
        double price = 0.0;

        try (Connection connection = DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT d.price " +
                             "FROM Dish d " +
                             "JOIN Wine w ON d.wineID = w.wineID " +
                             "WHERE w.wineID = ?")) {

            statement.setInt(1, wineID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                price = resultSet.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return price;
    }
}