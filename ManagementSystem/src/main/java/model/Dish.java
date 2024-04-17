package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a dish on the menu.
 */
public class Dish {
    private int dishID;
    private String name;
    private List<Ingredient> ingredients;
    private double price;
    private String dishDescription;
    private String allergyInfo;
    private int wineID;


    /**
     * Constructs a new Dish object with specified parameters.
     *
     * @param dishID          The ID of the dish.
     * @param name            The name of the dish.
     * @param price           The price of the dish.
     * @param dishDescription The description of the dish.
     * @param allergyInfo     Information about allergens present in the dish.
     * @param wineID          The ID of the associated wine, if any.
     */
    public Dish(int dishID, String name, double price, String dishDescription, String allergyInfo, int wineID) {
        this.dishID = dishID;
        this.name = name;
        this.price = price;
        this.dishDescription = dishDescription;
        this.allergyInfo = allergyInfo;
        this.wineID = wineID;
        this.ingredients = new ArrayList<>();
    }

    /**
     * Constructs a new Dish object with specified parameters.
     *
     * @param dishID          The ID of the dish.
     * @param name            The name of the dish.
     * @param price           The price of the dish.
     * @param dishDescription The description of the dish.
     * @param allergyInfo     Information about allergens present in the dish.
     */
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