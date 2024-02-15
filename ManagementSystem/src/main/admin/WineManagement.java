package main.admin;

import java.util.Map;

public interface WineManagement {
    void updateWineStock(String wineName, String wineType , int quantity);
    Map<String, Integer> getWineStockLevels();
    void suggestWinePairing(String dishName, String wineName, String wineType);
    Map<String, Integer> reportWineUsage(String wineType);

}

