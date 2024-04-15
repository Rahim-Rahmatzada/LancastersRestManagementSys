package UI;

/**
 * Interface to switch between different UI screens.
 */

public interface UISwitcher {
    void switchToDashboard();
    void switchToInventory();
    void switchToSales();
    void switchToTableOverview();
    void switchToMenus();
    void switchToStaff();
    void switchToWine();
    void switchToStockOrders();
    void switchToWaste();
    void switchToDish();


    void preloadInventoryUI();
    void preloadSalesUI();
    void preloadTableOverviewUI();
    void preloadMenusUI();
    void preloadStaffUI();
    void preloadWineUI();
    void preloadStockOrdersUI();
    void preloadWasteUI();
    void preloadDishUI();
}
