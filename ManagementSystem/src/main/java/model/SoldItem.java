package model;

/**
 * Represents an item that has been sold.
 */
public class SoldItem {
    private String name;
    private double price;
    private int quantity;

    /**
     * Constructs a SoldItem object with the specified details.
     *
     * @param name     The name of the item.
     * @param price    The price of the item.
     * @param quantity The quantity of the item.
     */
    public SoldItem(String name, double price,int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;

    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
