package UI;

/**
 * Interface to switch between different UI screens.
 */

public interface UISwitcher {
    void switchToDashboard();
    void switchToInventory();
    void switchToSales();
    void switchToRestaurantCapacity();
    void switchToMenus();
    void switchToStaff();
    void switchToWine();
    void switchToStockOrders();


    void preloadInventoryUI();
    void preloadSalesUI();
    void preloadBookingsUI();
    void preloadMenusUI();
    void preloadStaffUI();
    void preloadWineUI();
    void preloadStockOrdersUI();
}
