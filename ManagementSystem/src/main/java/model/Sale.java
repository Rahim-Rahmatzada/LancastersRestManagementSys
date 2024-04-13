package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Sale {
    private int saleID;
    private Date date;
    private Map<Dish, Integer> dishQuantities;
    private double totalCost;

    public Sale(int saleID, Date date, double totalCost) {
        this.saleID = saleID;
        this.date = date;
        this.totalCost = totalCost;
        this.dishQuantities = new HashMap<>();
    }

}
