package UI;

/**
 * Interface to switch between different UI screens.
 */

public interface UISwitcher {
    void switchToDashboard();
    void switchToInventory();
    void switchToSales();
    void switchToBookings();
    void switchToMenus();
    void switchToStaff();
    void switchToWine();


    void preloadInventoryUI();
    void preloadSalesUI();
    void preloadBookingsUI();
    void preloadMenusUI();
    void preloadStaffUI();
    void preloadWineUI();
}
