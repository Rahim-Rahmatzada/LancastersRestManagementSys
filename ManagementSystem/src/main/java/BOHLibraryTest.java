import FinalInterTeamServices.BOH.BOHDataAccessor;
import FinalInterTeamServices.BOH.BOHFinalInterface;
import model.*;

import java.time.LocalDate;
import java.util.List;

public class BOHLibraryTest {
    public static void main(String[] args) {
        // Create an instance of the BOHDataAccessor
        BOHFinalInterface bohDataAccessor = new BOHDataAccessor();

        // Test getStockLevels
        List<Ingredient> stockLevels = bohDataAccessor.getStockLevels();
        System.out.println("Stock Levels:");
        for (Ingredient ingredient : stockLevels) {
            System.out.println(ingredient.getName() + ": " + ingredient.getQuantity());
        }
        System.out.println();

        // Test getStockOrders
        List<Order> stockOrders = bohDataAccessor.getStockOrders();
        System.out.println("Stock Orders:");
        for (Order order : stockOrders) {
            System.out.println("Order ID: " + order.getOrderID() + ", Status: " + order.getOrderStatus());
        }
        System.out.println();

        // Test getMenuDishesAndWineInfo
        int menuID = 1; // Replace with the desired menu ID
        List<Dish> menuDishes = bohDataAccessor.getMenuDishesAndWineInfo(menuID);
        System.out.println("Dishes and Wine Info for Menu " + menuID + ":");
        for (Dish dish : menuDishes) {
            System.out.println(dish.getName() + ", Wine ID: " + dish.getWineID());
        }
        System.out.println();

        // Test getSpecificMenuDetails
        Menu specificMenu = bohDataAccessor.getSpecificMenuDetails(menuID);
        if (specificMenu != null) {
            System.out.println("Details for Menu " + menuID + ":");
            System.out.println("Effective Date: " + specificMenu.getEffectiveDate() + ", Status: " + specificMenu.getMenuStatus());
        }
        System.out.println();

        // Test getMenuDetails
        List<Menu> menus = bohDataAccessor.getMenuDetails();
        System.out.println("Menu Details:");
        for (Menu menu : menus) {
            System.out.println("Menu ID: " + menu.getMenuID() + ", Effective Date: " + menu.getEffectiveDate());
        }
        System.out.println();

        // Test getStockOrdersWithinDate
        LocalDate startDate = LocalDate.of(2023, 1, 1); // Replace with the desired start date
        LocalDate endDate = LocalDate.of(2023, 12, 31); // Replace with the desired end date
        List<Order> ordersWithinDate = bohDataAccessor.getStockOrdersWithinDate(startDate, endDate);
        System.out.println("Stock Orders within " + startDate + " and " + endDate + ":");
        for (Order order : ordersWithinDate) {
            System.out.println("Order ID: " + order.getOrderID() + ", Expected Delivery Date: " + order.getExpectedDeliveryDate());
        }
        System.out.println();

        // Test getStockOrdersByDateRangeAndStatus
        String orderStatus = "Pending"; // Replace with the desired order status
        List<Order> ordersByDateAndStatus = bohDataAccessor.getStockOrdersByDateRangeAndStatus(startDate, endDate, orderStatus);
        System.out.println("Stock Orders within " + startDate + " and " + endDate + " with status " + orderStatus + ":");
        for (Order order : ordersByDateAndStatus) {
            System.out.println("Order ID: " + order.getOrderID() + ", Date Ordered: " + order.getDateOrdered());
        }
        System.out.println();

        // Test getMenusByStatus
        String menuStatus = "Active"; // Replace with the desired menu status
        List<Menu> menusByStatus = bohDataAccessor.getMenusByStatus(menuStatus);
        System.out.println("Menus with status " + menuStatus + ":");
        for (Menu menu : menusByStatus) {
            System.out.println("Menu ID: " + menu.getMenuID() + ", Effective Date: " + menu.getEffectiveDate());
        }

        int dishID = 5;
        Wine wine = bohDataAccessor.getWineByDish(dishID);
        if (wine != null) {
            System.out.println("Wine Details for Dish " + dishID + ":");
            System.out.println(wine.getName());
        }
        System.out.println();
    }
}