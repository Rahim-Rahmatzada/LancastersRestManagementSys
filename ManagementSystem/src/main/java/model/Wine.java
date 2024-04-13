package model;

public class Wine {
    private int wineID;
    private String name;
    private String type;
    private double price;
    private int quantity;
    private int vintage; // Assuming vintage is stored as integer year


    public Wine(int wineID, String name, String type, int vintage, int quantity, double price) {
        this.wineID = wineID;
        this.name = name;
        this.price = price;
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