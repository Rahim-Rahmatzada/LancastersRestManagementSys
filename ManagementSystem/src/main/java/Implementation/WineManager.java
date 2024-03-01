package Implementation;


import admin.WineManagement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WineManager implements WineManagement {

    // Map to store the current wine stock levels
    private final Map<String, Integer> wineStock = new HashMap<>();

    // Map to store suggested wine pairings for dishes
    private final Map<String, String> winePairings = new HashMap<>();

    // Map to track the usage of wines
    private final Map<String, Map<Date, Integer>> wineUsage = new HashMap<>();

    // Updates or adds to the stock level of a specific wine
    @Override
    public void updateWineStock(String wineName, String wineType, int quantity) {
        //Creating unique key for each wine by combining name and type
        String key = wineName + " - " + wineType;
        //Update current stock or initialize
        wineStock.put(key, wineStock.getOrDefault(key, 0) + quantity);
    }

    //Gets a copy of current wine stock levels
    @Override
    public Map<String, Integer> getWineStockLevels() {
        return new HashMap<>(wineStock);
    }

    //Assumption that we know the paring in our database
    @Override
    public Map<String, String> getSuggestedWinePairingForDish(String dishName) {
        // Initialize an empty result map
        Map<String, String> result = new HashMap<>();
        // Check if there is a wine pairing for the given dish
        if (winePairings.containsKey(dishName)) {
            // Retrieve the wine pairing, which is stored as "wineName - wineType"
            String wineNameAndType = winePairings.get(dishName);
            // Split stored string to separate the wine name and type
            String[] parts = wineNameAndType.split(" - ");
            // Put the wine name and type into the result map
            result.put("wineName", parts[0]);
            result.put("wineType", parts[1]);
        }
        // Return the result map with the wine pairing information
        return result;
    }

    //Method to see how much of a wine name and its type has been had on a specific date
    @Override
    public Map<String, Integer> reportWineUsage(String wineName, String wineType, Date date) {
        Map<String, Integer> usageByDetails = new HashMap<>();
        String key = wineName + " - " + wineType;
        // Check if the wineUsage map contains the specified wine
        if (wineUsage.containsKey(key)) {
            Map<Date, Integer> dateMap = wineUsage.get(key);
            // Check if there's a record for the specified date
            if (dateMap.containsKey(date)) {
                usageByDetails.put(key + " on " + date.toString(), dateMap.get(date));
            }
        }
        return usageByDetails;
    }
}
