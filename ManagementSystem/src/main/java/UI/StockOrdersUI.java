package UI;

import javafx.scene.layout.VBox;

public class StockOrdersUI extends BaseUI {

    
    public StockOrdersUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Stock Orders");
        setTopText("Stock Orders Overview");

        // Set the main content for the WineUI.
        VBox ordersMainContent = new VBox();
        // Add components to wineMainContent as needed.
        setMainContent(ordersMainContent);
    }
}

//    Include a section or a separate tab to store and manage supplier information.
//        Display details such as supplier name, contact information, delivery schedule, and any specific notes or instructions.
//        This will help the management team easily access supplier details when placing orders or resolving issues.


//    Order History and Tracking:
//        Implement a feature to view and track the history of ingredient orders.
//        Display information such as order date, supplier, quantities ordered, and expected delivery date.
//        Allow the management team to update the status of each order (e.g., pending, shipped, received) and add notes or comments.