package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main user interface for the application. Manages primary stage and switches scenes based on UI interaction.
 */


public class MainUI extends Application implements UISwitcher {



    private Stage primaryStage;
    private DashboardUI dashboardUI;
    private InventoryUI inventoryUI;
    private SalesUI salesUI;
    private TableOverviewUI tableOverviewUI;
    private MenusUI menusUI;
    private StaffUI staffUI;
    private WineUI wineUI;
    private StockOrdersUI stockOrdersUI;

    /**
     * Launches the application.
     *
     * @param args command line arguments
     */

    public static void main(String[] args) {

        launch(args);
    }


    /**
     * Initializes and shows the primary stage with the Dashboard UI.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;


        switchToDashboard();
        primaryStage.show();
    }

    /**
     * Switches the current scene to the Dashboard UI.
     */

    @Override
    public void switchToDashboard() {
        if (dashboardUI == null) {
            dashboardUI = new DashboardUI(this);
        }
        switchScene(dashboardUI, "Dashboard UI");
        dashboardUI.highlightButton("Dashboard");
    }

    /**
     * Switches the current scene to the Inventory UI.
     */
    @Override
    public void switchToInventory() {
        if (inventoryUI == null) {
            inventoryUI = new InventoryUI(this);
        }
        switchScene(inventoryUI, "Inventory UI"); // Use the updated switchScene method
        inventoryUI.highlightButton("Inventory");
    }

    /**
     * Switches the current scene to the Sales UI.
     */

    @Override
    public void switchToSales() {
        if (salesUI == null) {
            salesUI = new SalesUI(this);
        }
        switchScene(salesUI, "Sales UI"); // Use the updated switchScene method
        salesUI.highlightButton("Sales");
    }

    /**
     * Switches the current scene to the Bookings UI.
     */

    @Override
    public void switchToTableOverview() {
        if (tableOverviewUI == null) {
            tableOverviewUI = new TableOverviewUI(this);
        }
        switchScene(tableOverviewUI, "Table Overview UI"); // Use the updated switchScene method
        tableOverviewUI.highlightButton("Table Overview");
    }

    /**
     * Switches the current scene to the Menus UI.
     */

    @Override
    public void switchToMenus() {
        if (menusUI == null) {
            menusUI = new MenusUI(this);
        }
        switchScene(menusUI, "Menus UI"); // Use the updated switchScene method
        menusUI.highlightButton("Menus");
    }

    @Override
    public void switchToStaff() {
        if (staffUI == null) {
            staffUI = new StaffUI(this);
        }
        switchScene(staffUI, "Staff UI");
        staffUI.highlightButton("Staff");
    }

    @Override
    public void switchToWine() {
        if (wineUI == null) {
            wineUI = new WineUI(this);
        }
        switchScene(wineUI, "Wine UI");
        wineUI.highlightButton("Wine");
    }

    @Override
    public void switchToStockOrders() {
        if (stockOrdersUI == null) {
            stockOrdersUI = new StockOrdersUI(this);
        }
        switchScene(stockOrdersUI, "Stock Orders UI");
        stockOrdersUI.highlightButton("Stock Orders");
    }

    /**
     * Helper method to switch the scene on the primary stage.
     *
     * @param ui The UI to switch to.
     * @param title The title for the new scene.
     */

    private void switchScene(BaseUI ui, String title) {
        BorderPane rootLayout = ui.getRootLayout(); // Get the root layout from BaseUI

        // Check if the rootLayout already has a scene, and if so, use it
        Scene scene = rootLayout.getScene();
        if (scene == null) {
            // If there is no scene, create a new one
            scene = new Scene(rootLayout, 1200, 700);
        }

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
    }



    @Override
    public void preloadInventoryUI() {
        if (inventoryUI == null) {
            inventoryUI = new InventoryUI(this);
        }
    }

    @Override
    public void preloadSalesUI() {
        if (salesUI == null) {
            salesUI = new SalesUI(this);
        }
    }

    @Override
    public void preloadTableOverviewUI() {
        if (tableOverviewUI == null) {
            tableOverviewUI = new TableOverviewUI(this);
        }
    }

    @Override
    public void preloadMenusUI() {
        if (menusUI == null) {
            menusUI = new MenusUI(this);
        }
    }

    @Override
    public void preloadStaffUI() {
        if (staffUI == null) {
            staffUI = new StaffUI(this);
        }
    }

    @Override
    public void preloadWineUI() {
        if (wineUI == null) {
            wineUI = new WineUI(this);
        }
    }

    @Override
    public void preloadStockOrdersUI() {
        if (stockOrdersUI == null) {
            stockOrdersUI = new StockOrdersUI(this);
        }
    }

}
