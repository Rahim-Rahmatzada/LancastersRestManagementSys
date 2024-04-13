package FinalInterTeamServices.BOH;

import model.*;

import java.time.LocalDate;
import java.util.List;

public interface BOHFinalInterface {
    /**
     * Retrieves the current stock levels of ingredients.
     *
     * @return A list of Ingredient objects representing the current stock levels.
     */
    List<Ingredient> getStockLevels();

    /**
     * Retrieves the list of stock orders.
     *
     * @return A list of StockOrder objects representing the stock orders.
     */
    List<Order> getStockOrders();

    /**
     * Retrieves the dishes and associated wine information for a specific menu.
     *
     * @param menuID The ID of the menu to retrieve.
     * @return A list of Dish objects representing the dishes and associated wine information in the specified menu.
     */
    List<Dish> getMenuDishesAndWineInfo(int menuID);

    /**
     * Retrieves the details of a specific menu.
     *
     * @param menuID The ID of the menu to retrieve.
     * @return A Menu object representing the menu details.
     */
    Menu getSpecificMenuDetails(int menuID);

    /**
     * Retrieves the details of all menus.
     *
     * @return A list of Menu objects representing all menus.
     */
    List<Menu> getMenuDetails();

    /**
     * Retrieves the list of stock orders where expected delivery date matches input dates
     *
     * @param startDate The start date of the range (inclusive).
     * @param endDate   The end date of the range (inclusive).
     * @return A list of StockOrder objects representing the stock orders within the specified date range.
     */
    List<Order> getStockOrdersWithinDate(LocalDate startDate, LocalDate endDate);

    /**
     * Retrieves the list of stock orders within a specified date range and with a specific order status.
     *
     * @param startDate  The start date of the range (inclusive).
     * @param endDate    The end date of the range (inclusive).
     * @param orderStatus The order status to filter by.
     * @return A list of StockOrder objects representing the stock orders within the specified date range and with the specified order status.
     */
    List<Order> getStockOrdersByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, String orderStatus);

    /**
     * Retrieves the menus with a specific status.
     *
     * @param status The status to filter the menus by.
     * @return A list of Menu objects representing the menus with the specified status.
     */
    List<Menu> getMenusByStatus(String status);

    /**
     * Retrieves the wine associated with a specific dish.
     *
     * @param dishID The ID of the dish to retrieve the associated wine for.
     * @return A Wine object representing the wine associated with the specified dish, including its name, type, quantity, and price.
     */
    Wine getWineByDish(int dishID);

}