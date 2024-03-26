package main.admin;

import java.util.Date;
import java.util.Map;

public interface SalesBookingsManagement {
    void recordSale(String dishName, int quantity, String paymentType);
    Map<String, Integer> getSalesPerformance(Date startDate, Date endDate);
    void manageBooking(int tableId, String customerName, Date bookingDate, String status, String contactInfo, String specialRequests, String typeOfBooking);
    void recordAbsence(String staffName, Date startDate, Date endDate);
    void updateBookingLimitsBasedOnStaff(Date date);
}

