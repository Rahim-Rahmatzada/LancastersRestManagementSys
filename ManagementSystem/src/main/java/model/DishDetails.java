package model;

public class DishDetails extends Dish {
    private String wineName;

    public DishDetails(int dishID, String name, double price, String dishDescription, String allergyInfo, int wineID, String wineName) {
        super(dishID, name, price, dishDescription, allergyInfo, wineID);
        this.wineName = wineName;
    }

    public String getWineName() {
        return wineName;
    }
}

