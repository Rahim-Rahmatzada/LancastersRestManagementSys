package model;

public class WasteEntry {
    private final String ingredient;
    private final String quantity;
    private final String reason;

    public WasteEntry(String ingredient, String quantity, String reason) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.reason = reason;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getReason() {
        return reason;
    }
}