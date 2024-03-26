//package InterfaceSent.BOH;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//public interface BOHInterface {
//
//    // Updates or adds a dish with ingredients and price to the menu
//    void UpdateMenu(String dishName, List<String> ingredients, double price);
//
//    // Retrieves current pricing for all menu items
//    Map<String, Double> getMenuPricing();
//
//    // Updates the cost of a specific ingredient
//    void updateIngredientCost(String ingredient, double cost);
//
//    // Sets allergen information for a specific dish
//    void setAllergenInfo(String dishName, List<String> allergens);
//
//    // Archives a specific menu
//    void archiveMenu(String menuName);
//
//    // Incorrectly named, should update price but lacks price parameter
//    void setDishPrice(String dishName, List<String> ingredients);
//
//    // Records a sale including dish, quantity, payment type, and date
//    void recordSale(String dishName, int quantity, String paymentType, Date saleDate);
//
//    // Retrieves sales performance data between two dates
//    List<Map<String, Object>> getSalesPerformance(Date startDate, Date endDate);
//
//    // Schedules staff for work between specified dates
//    void scheduleStaff(String staffName, String staffRole, Date startDate, Date endDate);
//
//    // Retrieves staff schedules between two dates
//    Map<String, Map<String, String>> getStaffSchedules(Date startDate, Date endDate);
//
//    // Records holiday for a staff member
//    void recordHoliday(String staffName, Date startDate, Date endDate);
//
//    // Updates the role of a staff member
//    void updateStaffRole(String staffName, String newRole);
//
//    // Records absences for multiple staff members
//    void recordAbsences(List<String> staffNames, List<Date> startDates, List<Date> endDates);
//
//    // Retrieves performance metrics for a staff member
//    Map<String, Double> getStaffPerformance(String staffName, Date date);
//
//    // Updates current stock levels for items
//    void updateStockLevels(Map<String, Integer> stockItems);
//
//    // Identifies items with stock below thresholds
//    Map<String, Integer> getLowStockItems(Map<String, Integer> currentStock, Map<String, Integer> stockThresholds);
//
//    // Records wasted items, categorizing the waste type
//    void recordWaste(String item, String wasteType, int quantity);
//
//    // Generates an order based on menu demand and recipe requirements
//    Map<String, Integer> generateOrder(Map<String, Integer> menuItems, Map<String, Integer> recipes);
//
//    // Alerts if discrepancies between ordered and received stock
//    void alertForDiscrepancies(Map<String, Integer> order, Map<String, Integer> receivedStock);
//
//    // Retrieves current stock level for a specific ingredient
//    int getCurrentStock(String ingredientName);
//
//    // Retrieves all future stock orders
//    Map<String, Integer> getFutureStockOrders();
//
//    // Retrieves ingredients and their quantities for a dish
//    Map<String, Double> getDishIngredients(String dishName);
//
//    // Suggests wine pairings for a specific dish
//    Map<String, String> getSuggestedWinePairingForDish(String dishName);
//
//    // Retrieves the price of a specific dish
//    float getDishPrice(String dishName);
//}
