package main.admin;

import java.util.Map;

public interface StockControl {
    void updateStockLevels(Map<String, Integer> stockItems);
    Map<String, Integer> getLowStockItems(Map<String, Integer> currentStock, Map<String, Integer> stockThresholds);
    void recordWaste(String item, String wasteType , int quantity);
    Map<String, Integer> generateOrder(Map<String, Integer> menuItems, Map<String, Integer> recipes);
    void alertForDiscrepancies(Map<String, Integer> order, Map<String, Integer> receivedStock);
    int getCurrentStock(String ingredientName);
    Map<String, Integer> getFutureStockOrders();
    Map<String, Map<String, Double>> getDishIngredients(String dishName);

}

