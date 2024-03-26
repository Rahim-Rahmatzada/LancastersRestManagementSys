package model;

import java.util.ArrayList;
import java.util.List;

public class Dish {
    private int dishID;
    private String name;
    private List<Ingredient> ingredients;
    private double price;

    public Dish(int dishID, String name, double price) {
        this.dishID = dishID;
        this.name = name;
        this.price = price;
        this.ingredients = new ArrayList<>();
    }

    // Getters and setters...
}
