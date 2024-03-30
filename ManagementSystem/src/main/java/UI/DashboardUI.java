package UI;

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

        preloadUIClasses();

    }

    private void preloadUIClasses() {
        Thread preloadThread = new Thread(() -> {
            // Preload other UI classes
            uiSwitcher.preloadInventoryUI();
            uiSwitcher.preloadSalesUI();
            uiSwitcher.preloadBookingsUI();
            uiSwitcher.preloadMenusUI();
            uiSwitcher.preloadStaffUI();
            uiSwitcher.preloadWineUI();
        });
        preloadThread.setDaemon(true); // Set the thread as a daemon thread
        preloadThread.start();
    }

}
