package main.Implementation;

import main.admin.SalesBookingsManagement;

import java.util.*;

public class SalesManagement implements SalesBookingsManagement {
    private Map<Integer, Integer> bookingLimits = new HashMap<>();

    private Map<String, List<Map<String, Object>>> salesData = new HashMap<>();



    @Override
    public void recordSale(String dishName, int quantity, String paymentType, Date saleDate) {
        // Ensure dishName entry exists, initialize new ArrayList if absent
        salesData.putIfAbsent(dishName, new ArrayList<>());

        // Create a new sale record
        Map<String, Object> saleRecord = new HashMap<>();
        saleRecord.put("quantity", quantity);
        saleRecord.put("paymentType", paymentType);
        saleRecord.put("date", saleDate);

        // Add the new sale record to the list for the dish
        salesData.get(dishName).add(saleRecord);
    }

    //Gets the sales data in a specific time period
    @Override
    public List<Map<String, Object>> getSalesPerformance(Date startDate, Date endDate) {
        List<Map<String, Object>> filteredSales = new ArrayList<>(); // List to hold filtered sale records

        for (String dishName : salesData.keySet()) { // Iterate over all dishes in sales data
            for (Map<String, Object> record : salesData.get(dishName)) { // Iterate over each sale record for the dish
                Date saleDate = (Date) record.get("date"); // Extract the sale date from the record
                if (!saleDate.before(startDate) && !saleDate.after(endDate)) { // Check if the sale date is within the range
                    Map<String, Object> filteredRecord = new HashMap<>(record); // Copy the record to avoid modifications
                    filteredRecord.put("dishName", dishName); // Add dish name to the filtered record
                    filteredSales.add(filteredRecord); // Add the filtered record to the list
                }
            }
        }

        return filteredSales; // Return the list of filtered sales records
    }

    @Override ///checehcehche
    public void manageBooking(int tableId, String customerName, Date bookingDate, String status, String contactInfo, String specialRequests, String typeOfBooking) {
        // Assuming booking limits are set to a maximum of 10 tables per day
        int currentBookings = bookingLimits.getOrDefault(tableId, 0);
        if (currentBookings < 10) {
            bookingLimits.put(tableId, currentBookings + 1);
        }
    }

    @Override /// cehchehcehchehce
    public void updateBookingLimitsBasedOnStaff(Date date) {
        // Assuming booking limits are updated based on the number of available staff
        // and a maximum of 10 tables per staff member
        int totalStaff = 5;  // Assuming there are 5 staff members available
        int maxTablesPerStaff = 10;
        int updatedBookingLimit = totalStaff * maxTablesPerStaff;

        // Update the booking limits for the day
        bookingLimits.clear();
        for (int i = 1; i <= updatedBookingLimit; i++) {
            bookingLimits.put(i, 0);
        }
    }
}
