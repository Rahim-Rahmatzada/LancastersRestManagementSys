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
}
