package FinalInterTeamServices.FOH;

import model.*;

import java.time.LocalDate;
import java.util.List;

public interface FOHFinalInterface {

    /**
     * Retrieves a list of menus based on their status.
     *
     * @return A list of Menu objects representing the menus with the specified status.
     */
    List<Menu> getMenusByStatus(String status);

    /**
     * Retrieves a list of dishes associated with a specific menu.
     *
     * @param menuID The ID of the menu to retrieve the dishes from.
     * @return A list of Dish objects representing the dishes in the specified menu.
     */
    List<Dish> getDishesByMenu(int menuID);

    /**
     * Retrieves the details of a specific dish, including the associated wine information.
     *
     * @param dishID The ID of the dish to retrieve the details for.
     * @return A Dish object representing the details of the specified dish, including the associated wine information.
     */
    Dish getDishDetails(int dishID);

    /**
     * Retrieves the wine associated with a specific dish.
     *
     * @param dishID The ID of the dish to retrieve the associated wine for.
     * @return A Wine object representing the wine associated with the specified dish, including its name, type, quantity, and price.
     */
    Wine getWineByDish(int dishID);

    /**
     * Retrieves the price of a specific dish.
     *
     * @param dishID The ID of the dish to retrieve the price for.
     * @return The price of the specified dish.
     */
    double getDishPrice(int dishID);

    /**
     * Retrieves the price of a specific wine.
     *
     * @param wineID The ID of the wine to retrieve the price for.
     * @return The price of the specified wine.
     */
    double getWinePrice(int wineID);

    /**
     * Retrieves the quantity of a specific wine in stock.
     *
     * @param wineID The ID of the wine to retrieve the quantity for.
     * @return The quantity of the specified wine in stock.
     */
    int getWineQuantity(int wineID);

    /**
     * Retrieves a list of staff members with the specified role.
     *
     * @param role The role to filter the staff members by.
     * @return A list of StaffInfo objects representing the staff members with the specified role.
     */
    List<StaffInfo> getStaffByRole(String role);

    /**
     * Retrieves the schedule of a specific staff member.
     *
     * @param staffID The ID of the staff member to retrieve the schedule for.
     * @return A list of StaffSchedule objects representing the schedule of the specified staff member.
     */
    List<Schedule> getStaffSchedule(int staffID);

    /**
     * Assigns a staff member with the role "waiter" to a specific table.
     *
     * @param staffName The name of the staff member to assign to the table.
     * @param tableID The ID of the table to assign the staff member to.
     * @return true if the assignment is successful, false otherwise.
     */
    boolean assignWaiterToTable(String staffName, int tableID);

    /**
     * Retrieves the staff members on holiday within a specified date range.
     *
     * @param startDate The start date of the date range (inclusive).
     * @param endDate The end date of the date range (inclusive).
     * @return A list of StaffHoliday objects representing the staff members on holiday within the specified date range.
     */
    List<StaffHolidayAssociation> getStaffHolidaysWithinDateRange(LocalDate startDate, LocalDate endDate);
}

