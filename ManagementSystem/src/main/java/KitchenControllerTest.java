//import forAdmin.KitchenControllerInterface;
//import forAdmin.KitchenController;
//
//import java.util.HashMap;
//import java.util.List;
//
//public class KitchenControllerTest {
//    public static void main(String[] args) {
//        // Create an instance of the KitchenController
//        KitchenControllerInterface kitchenController = new KitchenController();
//
//        // Test getMenu
//        int week = 20230101; // Replace with a valid week value
//        List<String> menu = kitchenController.getMenu(week);
//        System.out.println("Menu for week " + week + ":");
//        for (String dish : menu) {
//            System.out.println(dish);
//        }
//        System.out.println();
//
//        // Test getIngredients
//        List<String> ingredients = kitchenController.getIngredients();
//        System.out.println("Ingredients:");
//        for (String ingredient : ingredients) {
//            System.out.println(ingredient);
//        }
//        System.out.println();
//
//        // Test getStock
//        HashMap<Integer, Double> stock = kitchenController.getStock();
//        System.out.println("Stock:");
//        for (Integer ingredientId : stock.keySet()) {
//            System.out.println("Ingredient ID: " + ingredientId + ", Quantity: " + stock.get(ingredientId));
//        }
//        System.out.println();
//
//        // Test isLowStock
//        HashMap<Integer, Double> lowStock = kitchenController.isLowStock();
//        System.out.println("Low Stock:");
//        for (Integer ingredientId : lowStock.keySet()) {
//            System.out.println("Ingredient ID: " + ingredientId + ", Quantity: " + lowStock.get(ingredientId));
//        }
//        System.out.println();
//
//        // Test getDishIngredients
//        int dishId = 1; // Replace with a valid dish ID
//        HashMap<Integer, Double> dishIngredients = kitchenController.getDishIngredients(dishId);
//        System.out.println("Ingredients for Dish " + dishId + ":");
//        for (Integer ingredientId : dishIngredients.keySet()) {
//            System.out.println("Ingredient ID: " + ingredientId + ", Quantity: " + dishIngredients.get(ingredientId));
//        }
//        System.out.println();
//
//        // Test getAllergens
//        List<String> allergens = kitchenController.getAllergens(dishId);
//        System.out.println("Allergens for Dish " + dishId + ":");
//        for (String allergen : allergens) {
//            System.out.println(allergen);
//        }
//        System.out.println();
//
//        // Test checkIngredientsUsage
//        HashMap<Integer, Double> ingredientUsage = kitchenController.checkIngredientsUsage(week);
//        System.out.println("Ingredient Usage for week " + week + ":");
//        for (Integer ingredientId : ingredientUsage.keySet()) {
//            System.out.println("Ingredient ID: " + ingredientId + ", Quantity Used: " + ingredientUsage.get(ingredientId));
//        }
//        System.out.println();
//
//        // Test getWasteInfo
//        HashMap<String, String> wasteInfo = kitchenController.getWasteInfo();
//        System.out.println("Waste Information:");
//        for (String ingredientName : wasteInfo.keySet()) {
//            System.out.println("Ingredient: " + ingredientName + ", " + wasteInfo.get(ingredientName));
//        }
//    }
//}