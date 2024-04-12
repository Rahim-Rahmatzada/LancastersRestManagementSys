package FinalInterfaces.FOH;

import model.Dish;
import model.FOHStaff;
import model.Menu;
import model.Schedule;

import java.util.List;

public interface FOHInterface {
    // Menu and Dish Information
    List<Menu> getActiveMenus();
    List<Dish> getDishesByMenu(int menuID);
    Dish getDishDetails(int dishID);

    // Staff Management
    List<FOHStaff> getFOHStaff();
    List<Schedule> getStaffSchedule(int staffID);
    void assignStaffToTable(int staffID, int tableID);
}