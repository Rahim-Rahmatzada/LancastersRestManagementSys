package model;

public class Ingredient {
    private int ingredientID;
    private String name;
    private double cost;
    private int quantity;

    public Ingredient(int ingredientID, String name, double cost, int quantity) {
        this.ingredientID = ingredientID;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
    }

    public int getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
// Getters and setters...
}
