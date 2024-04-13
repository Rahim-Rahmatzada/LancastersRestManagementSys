package FinalInterTeamServices;
import model.DataUserDatabaseConnector;
import model.Ingredient;
import model.Order;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BOHImplementationFinal {

    // should get all stock orders and return everything given a range of date
    public List<Order> getOrdersFromDatabase(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DataUserDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT orderID, dateOrdered, expectedDeliveryDate, orderStatus " +
                             "FROM StockOrders " +
                             "WHERE expectedDeliveryDate BETWEEN ? AND ?")) {

            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int orderID = rs.getInt("orderID");
                    LocalDate dateOrdered = rs.getDate("dateOrdered").toLocalDate();
                    LocalDate expectedDeliveryDate = rs.getDate("expectedDeliveryDate").toLocalDate();
                    String orderStatus = rs.getString("orderStatus");
                    Order order = new Order(orderID, dateOrdered, expectedDeliveryDate, orderStatus);
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    // gets ingredients given an order ID
    public List<Ingredient> getOrderIngredientsFromDatabase(int orderID) {
        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection conn = DataUserDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT i.ingredientID, i.ingredientName, soi.ingredientQuantityOrdered " +
                             "FROM Ingredient i " +
                             "JOIN StockOrders_Ingredient soi ON i.ingredientID = soi.ingredientID " +
                             "WHERE soi.stockOrdersID = ?")) {

            stmt.setInt(1, orderID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int ingredientID = rs.getInt("ingredientID");
                    String name = rs.getString("ingredientName");
                    int quantity = rs.getInt("ingredientQuantityOrdered");
                    Ingredient ingredient = new Ingredient(ingredientID, name, 0, quantity, 0);
                    ingredients.add(ingredient);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredients;
    }
    //given a range of date, return a list of stock orders where order status = in process
    public List<Order> getInProgressOrdersFromDatabase(LocalDate startDate, LocalDate endDate) {
        List<Order> inProgressOrders = new ArrayList<>();

        try (Connection conn = DataUserDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT orderID, dateOrdered, expectedDeliveryDate, orderStatus " +
                             "FROM StockOrders " +
                             "WHERE orderStatus = 'In Process' " +
                             "AND expectedDeliveryDate BETWEEN ? AND ?")) {

            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int orderID = rs.getInt("orderID");
                    LocalDate dateOrdered = rs.getDate("dateOrdered").toLocalDate();
                    LocalDate expectedDeliveryDate = rs.getDate("expectedDeliveryDate").toLocalDate();
                    String orderStatus = rs.getString("orderStatus");
                    Order order = new Order(orderID, dateOrdered, expectedDeliveryDate, orderStatus);
                    inProgressOrders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inProgressOrders;
    }

    //menusUI still need to be implemented
    //return menu with status "x"

}