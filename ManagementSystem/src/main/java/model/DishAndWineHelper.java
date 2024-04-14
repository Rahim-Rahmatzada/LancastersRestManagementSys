package model;

public class DishAndWineHelper {
    private final String dishName;
    private final String allergyInfo;
    private final String wineName;
    private final double dishPrice;
    private final String dishDescription;
    private final double winePrice;

    public DishAndWineHelper(String dishName, String allergyInfo, String wineName, double dishPrice, String dishDescription, double winePrice) {
        this.dishName = dishName;
        this.allergyInfo = allergyInfo;
        this.wineName = wineName;
        this.dishPrice = dishPrice;
        this.dishDescription = dishDescription;
        this.winePrice = winePrice;
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