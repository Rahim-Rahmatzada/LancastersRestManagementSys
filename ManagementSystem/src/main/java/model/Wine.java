package model;

/**
 * Represents a wine entity.
 */

public class Wine {
    private int wineID;
    private String name;
    private String type;
    private double price;
    private int quantity;
    private int vintage; // Assuming vintage is stored as integer year

    /**
     * Constructs a Wine object with the specified attributes.
     *
     * @param wineID   The ID of the wine.
     * @param name     The name of the wine.
     * @param type     The type of the wine.
     * @param vintage  The vintage year of the wine.
     * @param quantity The quantity of the wine.
     * @param price    The price of the wine.
     */
    public Wine(int wineID, String name, String type, int vintage, int quantity, double price) {
        this.wineID = wineID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.vintage = vintage;
        this.type = type;
    }

    /**
     * Constructs a Wine object with the specified attributes except for price.
     * This constructor is used in the BOH interface method.
     *
     * @param wineID   The ID of the wine.
     * @param name     The name of the wine.
     * @param type     The type of the wine.
     * @param vintage  The vintage year of the wine.
     * @param quantity The quantity of the wine.
     */
    public Wine(int wineID, String name, String type, int vintage, int quantity) {
        this.wineID = wineID;
        this.name = name;
        this.quantity = quantity;
        this.vintage = vintage;
        this.type = type;
    }

    public int getWineID() {
        return wineID;
    }

    public void setWineID(int wineID) {
        this.wineID = wineID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getVintage() {
        return vintage;
    }

    public void setVintage(int vintage) {

        this.vintage = vintage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}