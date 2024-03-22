package UI;

import javafx.scene.layout.VBox;

public class WineUI extends BaseUI {

    public WineUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Wine");
        setTopText("Wine Overview");

        // Set the main content for the WineUI.
        VBox wineMainContent = new VBox();
        // Add components to wineMainContent as needed.
        setMainContent(wineMainContent);
    }

}

// wine vintage year
//    Wine Cellar Management: Manage inventory of wines, suggest pairings, and track usage.