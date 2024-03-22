package model;

import java.util.Date;

public class Booking {
    private int bookingID;
    private Date bookingDate;
    private Table table;
    private String customerName;
    private String bookingType;
    private String bookingStatus;

    public Booking(int bookingID, int tableNumber, Table table, Date bookingDate, String customerName, String bookingType, String bookingStatus) {
        this.bookingID = bookingID;
        this.bookingDate = bookingDate;
        this.table = table;
        this.customerName = customerName;
        this.bookingType = bookingType;
        this.bookingStatus = bookingStatus;
    }

    // Getters and setters...
}
