package model;

public class Wine {
    private int wineID;
    private String name;
    private String type;
    private int quantity;
    private int vintage; // Assuming vintage is stored as integer year

    public Wine(int wineID, String name, String type, int vintage, int quantity) {
        this.wineID = wineID;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.vintage = vintage;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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


}