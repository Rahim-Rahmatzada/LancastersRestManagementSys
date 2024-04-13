package FinalInterTeamServices.FOH;

import model.Dish;
import model.Menu;
import model.Schedule;
import model.Wine;

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
}

    //given a wineID get the quantity in stock
    //given a dishID get the


//    //given role as a parameter get list of staff
//
//    //given one staff id find their schedule
//    List<Schedule> getIndividualStaffSchedule(int staffID);
//
//    //assing a staff to table
//    void assignStaffToTable(int staffID, int tableID);
//
//    //given a range of date output the staff name who are on holiday, the startDate and endDate and duration all values from the StaffHoliday table
//}
