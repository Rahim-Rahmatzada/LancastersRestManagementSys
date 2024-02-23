package main.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SalesBookingsManagement {
    void recordSale(String dishName, int quantity, String paymentType, Date saleDate);
    List<Map<String, Object>> getSalesPerformance(Date startDate, Date endDate);
    void manageBooking(int tableId, String customerName, Date bookingDate, String status, String contactInfo, String specialRequests, String typeOfBooking);

    void updateBookingLimitsBasedOnStaff(Date date);
}

