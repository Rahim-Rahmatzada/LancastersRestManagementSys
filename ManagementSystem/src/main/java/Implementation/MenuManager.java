package Implementation;

import admin.MenuManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuManager implements MenuManagement {

    //Map to store menu items with details
    private final Map<String, Map<String, Object>> menus ;

    //constructor to initialize menus
    public MenuManager() {
        this.menus = new HashMap<>();
    }

    @Override
    public void UpdateMenu(String dishName, List<String> ingredients, double price) {
        // Check if the dish already exists in the menus
        if (menus.containsKey(dishName)) {
            // If it exists, update the existing menu item
            Map<String, Object> menuDetails = menus.get(dishName);
            menuDetails.put("ingredients", new ArrayList<>(ingredients)); // Use a new ArrayList to avoid external modifications
            menuDetails.put("price", price);
        } else {
            // If it doesn't exist, create a new menu item
            Map<String, Object> menuDetails = new HashMap<>();
            menuDetails.put("ingredients", new ArrayList<>(ingredients)); // Use a new ArrayList to avoid external modifications
            menuDetails.put("price", price);
            menus.put(dishName, menuDetails);
        }
    }

    @Override
    public Map<String, Double> getMenuPricing() {
        Map<String, Double> pricing = new HashMap<>();
        // Iterate over all menu items
        for (String dishName : menus.keySet()) {

            // Retrieve menu details for the current dish
            Map<String, Object> menuDetails = menus.get(dishName);

            // Extract the price from the menu details
            Double price = (Double) menuDetails.get("price");

            // Add the dish name and its price to the pricing map
            pricing.put(dishName, price);
        }
        return pricing;
    }

    @Override
    public void setDishPrice(String dishName, List<String> ingredients) {
        // Check if the dish exists in the menus
        if (menus.containsKey(dishName)) {
            // If it exists, calculate the price based on the cost of ingredients
            double totalPrice = calculateTotalPriceOfIngredients(ingredients);
            // Update the price of the dish
            Map<String, Object> menuDetails = menus.get(dishName);
            menuDetails.put("price", totalPrice);
        } else {
            // If the dish doesn't exist, throw an exception or handle it accordingly
            System.out.println("Error: Dish " + dishName + " does not exist.");
        }
    }

    // Method to calculate the total price of ingredients
    private double calculateTotalPriceOfIngredients(List<String> ingredients) {
        double totalPrice = 0.0;
        // Loop through the ingredients and calculate the total price
        for (String ingredient : ingredients) {
            // assume each ingredient costs $1
            totalPrice += 1.0;
        }
        return totalPrice;
    }



    @Override
    public void updateIngredientCost(String ingredient, double cost) {
    }


    @Override
    public void setAllergenInfo(String dishName, List<String> allergens) {
        if (menus.containsKey(dishName)) {
            Map<String, Object> menuDetails = menus.get(dishName);
            menuDetails.put("allergens", new ArrayList<>(allergens)); // Use a new ArrayList to avoid external modifications
        }
    }

    @Override
    public void archiveMenu(String menuName) {
        menus.remove(menuName);
    }
}
