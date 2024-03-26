package Implementation;

import admin.StockControl;

import java.util.HashMap;
import java.util.Map;

public class StockManager implements StockControl {
    //Map to store current stock levels
    private Map<String, Integer> currentStock = new HashMap<>();

    // Map to store future stock orders
    private Map<String, Integer> futureStockOrders = new HashMap<>();

    //Map to store dish ingredients
    private Map<String, Map<String, Double>> dishIngredients = new HashMap<>();


    @Override
    public void updateStockLevels(Map<String, Integer> stockItems) {
        for (Map.Entry<String, Integer> entry : stockItems.entrySet()) {
            String item = entry.getKey();
            int quantity = entry.getValue();
            int currentQuantity = currentStock.getOrDefault(item, 0); // Gets current quantity, defaults to 0 if not found
            currentStock.put(item, currentQuantity + quantity); // Adds incoming quantity to current stock quantity and updates stock level
        }
    }
    // Method to identify items low in stock
    @Override
    public Map<String, Integer> getLowStockItems(Map<String, Integer> currentStock, Map<String, Integer> stockThresholds) {
        Map<String, Integer> lowStockItems = new HashMap<>();
        for (Map.Entry<String, Integer> entry : stockThresholds.entrySet()) {
            String item = entry.getKey();
            int threshold = entry.getValue();
            int currentQuantity = currentStock.getOrDefault(item, 0); // Gets current quantity, defaults to 0 if not found
            if (currentQuantity < threshold) {
                lowStockItems.put(item, currentQuantity); // Adds item that is below threshold to the map for low stock items
            }
        }
        return lowStockItems; // Returns the map of low stock items
    }

    // Method to record waste
    @Override
    public void recordWaste(String item, String wasteType, int quantity) {
        // In real application log to database
        System.out.println("Recording waste - Item: " + item + ", Waste Type: " + wasteType + ", Quantity: " + quantity);
    }


    // Method to generate an order based on menu items and their recipes
    @Override
    public Map<String, Integer> generateOrder(Map<String, Integer> menuItems, Map<String, Integer> recipes) {
        Map<String, Integer> order = new HashMap<>();

        for (Map.Entry<String, Integer> menuItem : menuItems.entrySet()) {
            String itemName = menuItem.getKey();
            int quantity = menuItem.getValue();

            if (recipes.containsKey(itemName)) {
                Map<String, Double> recipe = dishIngredients.get(itemName);
                if (recipe != null) {
                    for (Map.Entry<String, Double> ingredient : recipe.entrySet()) {
                        String ingredientName = ingredient.getKey();
                        double ingredientQuantity = ingredient.getValue();
                        int currentOrderQuantity = order.getOrDefault(ingredientName, 0);
                        order.put(ingredientName, (int) (currentOrderQuantity + (ingredientQuantity * quantity)));
                    }
                }
            }
        }

        return order;
    }



    // Method to alert for any discrepancies between ordered items and received stock
    @Override
    public void alertForDiscrepancies(Map<String, Integer> order, Map<String, Integer> receivedStock) {
        System.out.println("Alerting for discrepancies...");
        // Iterate through each item in the order
        for (Map.Entry<String, Integer> entry : order.entrySet()) {
            String item = entry.getKey();
            int orderedQuantity = entry.getValue();

            // Check if the ordered item is present in the received stock
            if (receivedStock.containsKey(item)) {
                int receivedQuantity = receivedStock.get(item);

                // Compare the ordered quantity with the received quantity

                if (orderedQuantity != receivedQuantity) {

                    // Alert about the discrepancy

                    System.out.println("Discrepancy found for item: " + item);

                    System.out.println("Ordered Quantity: " + orderedQuantity);

                    System.out.println("Received Quantity: " + receivedQuantity);

                }

            } else {

                // Alert if the ordered item is missing from the received stock

                System.out.println("Discrepancy found for item: " + item);

                System.out.println("Ordered Quantity: " + orderedQuantity);

                System.out.println("Received Quantity: 0 (Item not found in received stock)");
            }
        }
    }

    // Method to retrieve current stock levels of a specified ingredient
    @Override
    public int getCurrentStock(String ingredientName) {
        // Get the current stock level of the specified ingredient from the map
        return currentStock.getOrDefault(ingredientName, 0); // Return current stock level, or 0 as default if not found
    }

    // Method to retrieve future stock orders
    @Override
    public Map<String, Integer> getFutureStockOrders() {
        // Return the map containing future stock orders
        return futureStockOrders;
    }


    //Method to retrieve ingredients required for preparing a specific dish
    @Override
    public Map<String, Double> getDishIngredients(String dishName){
        // Returns ingredients and their quantities for the specified dish
        // If dish not found returns empty map
        return dishIngredients.getOrDefault(dishName, new HashMap<>());
    }
}







