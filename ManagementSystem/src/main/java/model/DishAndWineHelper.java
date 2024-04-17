package model;

/**
 * Helper class representing information about a dish and its associated wine.
 */
public class DishAndWineHelper {
    private final String dishName;
    private final String allergyInfo;
    private final String wineName;
    private final double dishPrice;
    private final String dishDescription;
    private final double winePrice;
    private final double profit;


    /**
     * Constructs a DishAndWineHelper object with the specified parameters.
     *
     * @param dishName        The name of the dish.
     * @param allergyInfo     Information about allergens in the dish.
     * @param wineName        The name of the associated wine.
     * @param dishPrice       The price of the dish.
     * @param dishDescription The description of the dish.
     * @param winePrice       The price of the associated wine.
     * @param profit          The profit margin for the dish.
     */
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