package UI;

import javafx.scene.layout.VBox;

public class BookingsUI extends BaseUI {

    public BookingsUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Bookings");

        setTopText("Bookings Overviewwwwwwww");

        // Set the main content for the DashboardUI.
        VBox dashboardMainContent = new VBox();
        // Add components to dashboardMainContent as needed.
        setMainContent(dashboardMainContent);
    }




}
