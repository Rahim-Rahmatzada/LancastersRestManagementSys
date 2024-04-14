package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Sale {
    private int saleID;
    private Date date;
    private Map<Dish, Integer> dishQuantities;
    private double totalCost;
    private int numOfCovers;

    public Sale(int saleID, Date date,int numOfCovers) {
        this.saleID = saleID;
        this.date = date;
        this.numOfCovers = numOfCovers;
        this.dishQuantities = new HashMap<>();
    }

    public int getNumOfCovers() {
        return numOfCovers;
    }

    public void setNumOfCovers(int numOfCovers) {
        this.numOfCovers = numOfCovers;
    }

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<Dish, Integer> getDishQuantities() {
        return dishQuantities;
    }

    public void setDishQuantities(Map<Dish, Integer> dishQuantities) {
        this.dishQuantities = dishQuantities;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

}
