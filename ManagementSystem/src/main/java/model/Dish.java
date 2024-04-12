package model;

import java.util.ArrayList;
import java.util.List;

public class Dish {
    private int dishID;
    private String name;
    private List<Ingredient> ingredients;
    private double price;
    private String description;
    private String allergyInfo;
    private int wineID;

    public Dish(int dishID, String name, double price, String description, String allergyInfo, int wineID) {
        this.dishID = dishID;
        this.name = name;
        this.price = price;
        this.description = description;
        this.allergyInfo = allergyInfo;
        this.wineID = wineID;
        this.ingredients = new ArrayList<>();
    }

    public int getDishID() {
        return dishID;
    }

    public void setDishID(int dishID) {
        this.dishID = dishID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAllergyInfo() {
        return allergyInfo;
    }

    public void setAllergyInfo(String allergyInfo) {
        this.allergyInfo = allergyInfo;
    }

    public int getWineID() {
        return wineID;
    }

    public void setWineID(int wineID) {
        this.wineID = wineID;
    }
}
