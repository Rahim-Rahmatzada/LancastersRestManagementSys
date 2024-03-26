//package InterfaceSent.BOH;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//public class BOHImplementation implements  BOHInterface {
//    @Override
//    public void UpdateMenu(String dishName, List<String> ingredients, double price) {
//
//    }
//
//    @Override
//    public Map<String, Double> getMenuPricing() {
//        return null;
//    }
//
//    @Override
//    public void updateIngredientCost(String ingredient, double cost) {
//
//    }
//
//    @Override
//    public void setAllergenInfo(String dishName, List<String> allergens) {
//
//    }
//
//    @Override
//    public void archiveMenu(String menuName) {
//
//    }
//
//    @Override
//    public void setDishPrice(String dishName, List<String> ingredients) {
//
//    }
//
//    @Override
//    public void recordSale(String dishName, int quantity, String paymentType, Date saleDate) {
//
//    }
//
//    @Override
//    public List<Map<String, Object>> getSalesPerformance(Date startDate, Date endDate) {
//        return null;
//    }
//
//    @Override
//    public void scheduleStaff(String staffName, String staffRole, Date startDate, Date endDate) {
//
//    }
//
//    @Override
//    public Map<String, Map<String, String>> getStaffSchedules(Date startDate, Date endDate) {
//        return null;
//    }
//
//    @Override
//    public void recordHoliday(String staffName, Date startDate, Date endDate) {
//
//    }
//
//    @Override
//    public void updateStaffRole(String staffName, String newRole) {
//
//    }
//
//    @Override
//    public void recordAbsences(List<String> staffNames, List<Date> startDates, List<Date> endDates) {
//
//    }
//
//    @Override
//    public Map<String, Double> getStaffPerformance(String staffName, Date date) {
//        return null;
//    }
//
//    @Override
//    public void updateStockLevels(Map<String, Integer> stockItems) {
//
//    }
//
//    @Override
//    public Map<String, Integer> getLowStockItems(Map<String, Integer> currentStock, Map<String, Integer> stockThresholds) {
//        return null;
//    }
//
//    @Override
//    public void recordWaste(String item, String wasteType, int quantity) {
//
//    }
//
//    @Override
//    public Map<String, Integer> generateOrder(Map<String, Integer> menuItems, Map<String, Integer> recipes) {
//        return null;
//    }
//
//    @Override
//    public void alertForDiscrepancies(Map<String, Integer> order, Map<String, Integer> receivedStock) {
//
//    }
//
//    @Override
//    public int getCurrentStock(String ingredientName) {
//        return 0;
//    }
//
//    @Override
//    public Map<String, Integer> getFutureStockOrders() {
//        return null;
//    }
//
//    @Override
//    public Map<String, Double> getDishIngredients(String dishName) {
//        return null;
//    }
//
//    @Override
//    public Map<String, String> getSuggestedWinePairingForDish(String dishName) {
//        return null;
//    }
//
//    @Override
//    public float getDishPrice(String dishName) {
//        return 0;
//    }
//}
//
