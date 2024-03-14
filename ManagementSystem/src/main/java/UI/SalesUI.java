package UI;


import javafx.scene.layout.VBox;

public class SalesUI extends BaseUI {

    public SalesUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Sales");
        setTopText("SALES Overviewwwwwwww");

        // Set the main content for the DashboardUI.
        VBox dashboardMainContent = new VBox();
        // Add components to dashboardMainContent as needed.
        setMainContent(dashboardMainContent);
    }


}
