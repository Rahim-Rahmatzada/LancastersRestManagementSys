package UI;


import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardUI extends BaseUI {



    public DashboardUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Dashboard");
        setTopText("Dashboard Overviewwwwwwww");

        // Set the main content for the DashboardUI.
        VBox dashboardMainContent = new VBox();
        // Add components to dashboardMainContent as needed.
        setMainContent(dashboardMainContent);
    }



}
