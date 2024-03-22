package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class InventoryUI extends BaseUI {

    public InventoryUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Inventory");
        setTopText("Inventory Overviewwwwwwww");

        // Set the main content for the DashboardUI.
        VBox dashboardMainContent = new VBox();
        // Add components to dashboardMainContent as needed.
        setMainContent(dashboardMainContent);
    }



}


//    Inventory UI
//    Current Stock Levels: Monitor and update the quantities of ingredients and supplies currently in stock.
//        Order Management: Automate the creation of orders based on menu requirements and track orders placed with suppliers.
//        Waste Tracking: Record and analyze kitchen waste to improve future stock ordering accuracy.
//        Ingredient Usage: Log the use of ingredients for each dish to manage inventory efficiently.
//        Stock Alerts: Notification system for low stock items or discrepancies in orders.
//        Supplier Management: Maintain supplier information and manage supplier interactions.

//    Ordering Process: Review and finalize orders for produce and other supplies.