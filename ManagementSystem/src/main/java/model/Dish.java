package model;

import java.util.ArrayList;
import java.util.List;

public class Dish {
    private int dishID;
    private String name;
    private List<Ingredient> ingredients;
    private double price;
    private String dishDescription;
    private String allergyInfo;
    private int wineID;

    public Dish(int dishID, String name, double price, String dishDescription, String allergyInfo, int wineID) {
        this.dishID = dishID;
        this.name = name;
        this.price = price;
        this.dishDescription = dishDescription;
        this.allergyInfo = allergyInfo;
        this.wineID = wineID;
        this.ingredients = new ArrayList<>();
    }

    public Dish(int dishID, String name, double price, String dishDescription, String allergyInfo) {
        this.dishID = dishID;
        this.name = name;
        this.price = price;
        this.dishDescription = dishDescription;
        this.allergyInfo = allergyInfo;
    }

    public int getDishID() {
        return dishID;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public double getPrice() {
        return price;
    }

    public String getDishDescription() {
        return dishDescription;
    }

    public String getAllergyInfo() {
        return allergyInfo;
    }

    public int getWineID() {
        return wineID;
    }

    public void setDishID(int dishID) {
        this.dishID = dishID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDishDescription(String dishDescription) {
        this.dishDescription = dishDescription;
    }

    public void setAllergyInfo(String allergyInfo) {
        this.allergyInfo = allergyInfo;
    }

    public void setWineID(int wineID) {
        this.wineID = wineID;
    }

    public void setDescription(String newValue) {
    }

    public String getDescription() { return dishDescription;
    }

    public int getId() { return dishID;
    }
}