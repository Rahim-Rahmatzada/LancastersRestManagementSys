package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MenusUI extends BaseUI {

    public MenusUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Menus");
    }

    @Override
    protected void handleButtonAction(String label) {
        switch (label) {
            case "Dashboard":
                uiSwitcher.switchToDashboard();
                break;
            case "Inventory":
                uiSwitcher.switchToInventory();
                break;
            case "Sales":
                uiSwitcher.switchToSales();
                break;
            case "Bookings":
                uiSwitcher.switchToBookings();
                break;
            case "Menus":
                uiSwitcher.switchToMenus();
                break;
            default:
                break;
        }
    }
}
