import FinalInterTeamServices.FOH.FOHDataAccessor;
import FinalInterTeamServices.FOH.FOHFinalInterface;
import model.*;

import java.time.LocalDate;
import java.util.List;

public class FOHLibraryUsage {
    public static void main(String[] args) {
        // Create an instance of the FOHDataAccessor
        FOHFinalInterface fohDataAccessor = new FOHDataAccessor();

        // Test getMenusByStatus
        String menuStatus = "Active";
        List<Menu> activeMenus = fohDataAccessor.getMenusByStatus(menuStatus);
        System.out.println("Active Menus:");
        for (Menu menu : activeMenus) {
            System.out.println(menu.getMenuStatus());
        }
        System.out.println();

        // Test getDishesByMenu
        int menuID = 1; // Replace with the desired menu ID
        List<Dish> dishes = fohDataAccessor.getDishesByMenu(menuID);
        System.out.println("Dishes for Menu " + menuID + ":");
        for (Dish dish : dishes) {
            System.out.println(dish.getName());
        }
        System.out.println();

        // Test getDishDetails
        int dishID = 5; // Replace with the desired dish ID
        Dish dishDetails = fohDataAccessor.getDishDetails(dishID);
        if (dishDetails != null) {
            System.out.println("Dish Details for Dish " + dishID + ":");
            System.out.println(dishDetails.getName());
        }
        System.out.println();

//        // Test getWineByDish
//        int dishID = 1;
        Wine wine = fohDataAccessor.getWineByDish(dishID);
        if (wine != null) {
            System.out.println("Wine Details for Dish " + dishID + ":");
            System.out.println(wine.getName());
        }
        System.out.println();
//
        // Test getDishPrice
        double dishPrice = fohDataAccessor.getDishPrice(dishID);
        System.out.println("Price for Dish " + dishID + ": " + dishPrice);
        System.out.println();

//        // Test getWinePrice
        int wineID = 3; // Replace with the desired wine ID
        double winePrice = fohDataAccessor.getWinePrice(wineID);
        System.out.println("Price for Wine " + wineID + ": " + winePrice);
        System.out.println();

        // Test getWineQuantity
        int wineQuantity = fohDataAccessor.getWineQuantity(wineID);
        System.out.println("Quantity for Wine " + wineID + ": " + wineQuantity);
        System.out.println();
//
        // Test getStaffByRole
        String role = "Waiter";
        List<StaffInfo> waiters = fohDataAccessor.getStaffByRole(role);
        System.out.println("Staff with Role " + role + ":");
        for (StaffInfo staff : waiters) {
            System.out.println(staff.getStaffName());
        }
        System.out.println();
//
        // Test getStaffSchedule
        int staffID = 1; // Replace with the desired staff ID
        List<Schedule> schedule = fohDataAccessor.getStaffSchedule(staffID);
        System.out.println("Schedule for Staff " + staffID + ":");
        for (Schedule shift : schedule) {
            System.out.println(shift.getDuration());
        }
        System.out.println();

        // Test assignWaiterToTable
        String waiterName = "Ryan"; // Replace with the desired staff name
        int tableID = 1; // Replace with the desired table ID
        boolean assignmentSuccess = fohDataAccessor.assignWaiterToTable(waiterName, tableID);
        System.out.println("Assigned " + waiterName + " to Table " + tableID + ": " + assignmentSuccess);
        System.out.println();

        // Test getStaffHolidaysWithinDateRange
        LocalDate startDate = LocalDate.of(2020, 6, 1); // Replace with the desired start date
        LocalDate endDate = LocalDate.of(2025, 6, 30); // Replace with the desired end date
        List<StaffHolidayAssociation> staffHolidays = fohDataAccessor.getStaffHolidaysWithinDateRange(startDate, endDate);
        System.out.println("Staff Holidays between " + startDate + " and " + endDate + ":");
        for (StaffHolidayAssociation holiday : staffHolidays) {
            System.out.println(holiday.getStaffID());
        }
    }
}