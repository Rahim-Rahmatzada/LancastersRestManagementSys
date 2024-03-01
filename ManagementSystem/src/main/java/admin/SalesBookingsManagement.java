package admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SalesBookingsManagement {
    void recordSale(String dishName, int quantity, String paymentType, Date saleDate);
    List<Map<String, Object>> getSalesPerformance(Date startDate, Date endDate);
    void manageBooking(int tableId, String customerName, Date bookingDate, String status, String contactInfo, String specialRequests, String typeOfBooking);
    void setDailyBookingLimit(Date date, int limit);
    int getDailyBookingLimit(Date date);
    void adjustBookingLimitForSpecialEvent(Date date, int newLimit);
    List<Map<String, Object>> getBookingsForDate(Date date);
    Map<String, Object> generateBookingReport(Date startDate, Date endDate);

}

