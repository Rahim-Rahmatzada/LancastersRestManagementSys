package main.admin;

import java.util.Map;

public interface WineManagement {
    void updateWineStock(String wineName, String wineType , int quantity);
    Map<String, Integer> getWineStockLevels();
    Map<String, String> getSuggestedWinePairingForDish(String dishName);

    Map<String, Integer> reportWineUsage(String wineType);

}

