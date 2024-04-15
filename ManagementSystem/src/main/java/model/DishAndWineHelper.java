package model;

public class DishAndWineHelper {
    private final String dishName;
    private final String allergyInfo;
    private final String wineName;
    private final double dishPrice;
    private final String dishDescription;
    private final double winePrice;
    private final double profit;


    public DishAndWineHelper(String dishName, String allergyInfo, String wineName, double dishPrice, String dishDescription, double winePrice, double profit) {
        this.dishName = dishName;
        this.allergyInfo = allergyInfo;
        this.wineName = wineName;
        this.dishPrice = dishPrice;
        this.dishDescription = dishDescription;
        this.winePrice = winePrice;
        this.profit = profit;

    }

    public double getProfit() {
        return profit;
    }



    public String getDishName() {
        return dishName;
    }

    public String getAllergyInfo() {
        return allergyInfo;
    }

    public String getWineName() {
        return wineName;
    }

    public double getDishPrice() {
        return dishPrice;
    }

    public String getDishDescription() {
        return dishDescription;
    }

    public double getWinePrice() {
        return winePrice;
    }


}