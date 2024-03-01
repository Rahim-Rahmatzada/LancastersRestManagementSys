package main.InterfaceSent.FOH;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FOHInterface {

    // Manages bookings including updating status and handling special requests
    void manageBooking(int tableId, String customerName, Date bookingDate, String status, String contactInfo, String specialRequests, String typeOfBooking);

    // Sets a limit on the number of bookings for a specific date
    void setDailyBookingLimit(Date date, int limit);

    // Retrieves the set limit for bookings on a specific date
    int getDailyBookingLimit(Date date);

    // Adjusts the booking limit for a date, typically for special events
    void adjustBookingLimitForSpecialEvent(Date date, int newLimit);

    // Retrieves all bookings for a specific date
    List<Map<String, Object>> getBookingsForDate(Date date);

    // Generates a report of bookings within a specified date range
    Map<String, Object> generateBookingReport(Date startDate, Date endDate);

    // Updates the stock level of a specific wine
    void updateWineStock(String wineName, String wineType, int quantity);

    // Retrieves current stock levels for all wines
    Map<String, Integer> getWineStockLevels();

    // Suggests wine pairings for a specific dish
    Map<String, String> getSuggestedWinePairingForDish(String dishName);

    // Reports usage of a specific wine on a specific date
    Map<String, Integer> reportWineUsage(String wineName, String wineType, Date date);

    // Retrieves staff schedules within a specified date range
    Map<String, Map<String, String>> getStaffSchedules(Date startDate, Date endDate);

    // Retrieves the price of a specific dish
    float getDishPrice(String dishName);

    // Provides table setup options based on the number of seats
    Map<String, Integer> getTableSetup(int NumOfSeats);

}
