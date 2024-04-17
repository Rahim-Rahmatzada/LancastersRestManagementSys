package model;

/**
 * Represents an entry for waste information.
 */
public class WasteEntry {
    private final String ingredient;
    private final String quantity;
    private final String reason;

    /**
     * Constructs a WasteEntry object with the specified ingredient, quantity, and reason.
     *
     * @param ingredient The ingredient wasted.
     * @param quantity   The quantity wasted.
     * @param reason     The reason for waste.
     */
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