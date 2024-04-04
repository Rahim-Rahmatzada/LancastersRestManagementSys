package model;

import java.time.LocalDate;

public class Order {
    private int orderID;
    private LocalDate dateOrdered;
    private LocalDate expectedDeliveryDate;

    public Order(int orderID, LocalDate dateOrdered, LocalDate expectedDeliveryDate) {
        this.orderID = orderID;
        this.dateOrdered = dateOrdered;
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public LocalDate getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(LocalDate dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }
}