package UI;


import javafx.scene.layout.VBox;

public class MenusUI extends BaseUI {

    public MenusUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Menus");
        setTopText("MENUUU Overviewwwwwwww");

        // Set the main content for the DashboardUI.
        VBox dashboardMainContent = new VBox();
        // Add components to dashboardMainContent as needed.
        setMainContent(dashboardMainContent);
    }




}
