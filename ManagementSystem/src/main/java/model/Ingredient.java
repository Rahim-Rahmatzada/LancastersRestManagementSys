package model;

public class Ingredient {
    private int ingredientID;
    private String name;
    private double cost;
    private int quantity;
    private int threshold;
    private int quantityUsed;


    public Ingredient(int ingredientID, String name, double cost, int quantity, int threshold) {
        this.ingredientID = ingredientID;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.threshold = threshold;
    }

    public Ingredient(int ingredientID, String name, int quantityUsed) {
        this.ingredientID = ingredientID;
        this.name = name;
        this.quantityUsed = quantityUsed;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
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
    public int getQuantityUsed() {
        return quantityUsed;
    }

    public void setQuantityUsed(int quantityUsed) {
        this.quantityUsed = quantityUsed;
    }
// Getters and setters...
}
