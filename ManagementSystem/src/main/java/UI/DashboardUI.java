package UI;



public class DashboardUI extends BaseUI {



    public DashboardUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Dashboard");
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
