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
