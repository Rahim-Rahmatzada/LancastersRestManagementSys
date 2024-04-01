package model;

public class Order {
    private String ingredient;
    private int quantity;
    private double cost;
    private String status;


    public Order(String ingredient, int quantity, double cost) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.cost = cost;
        this.status = "Pending"; // Set default status to "Pending"
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}