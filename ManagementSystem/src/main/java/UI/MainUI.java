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
    private WasteUI wasteUI;
    private DishUI dishUI;


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

    /**
     * Switches the current scene to the Staff UI.
     */
    @Override
    public void switchToStaff() {
        if (staffUI == null) {
            staffUI = new StaffUI(this);
        }
        switchScene(staffUI, "Staff UI");
        staffUI.highlightButton("Staff");
    }

    /**
     * Switches the current scene to the Wine UI.
     */
    @Override
    public void switchToWine() {
        if (wineUI == null) {
            wineUI = new WineUI(this);
        }
        switchScene(wineUI, "Wine UI");
        wineUI.highlightButton("Wine");
    }

    /**
     * Switches the current scene to the Stock Orders UI.
     */
    @Override
    public void switchToStockOrders() {
        if (stockOrdersUI == null) {
            stockOrdersUI = new StockOrdersUI(this);
        }
        switchScene(stockOrdersUI, "Stock Orders UI");
        stockOrdersUI.highlightButton("Stock Orders");
    }

    /**
     * Switches the current scene to the Waste UI.
     */
    @Override
    public void switchToWaste() {
        if (wasteUI == null) {
            wasteUI = new WasteUI(this);
        }
        switchScene(wasteUI, "Waste UI");
        wasteUI.highlightButton("Waste");
    }

    /**
     * Switches the current scene to the Dish UI.
     */
    @Override
    public void switchToDish() {
        if (dishUI == null) {
            dishUI = new DishUI(this);
        }
        switchScene(dishUI, "Dish UI");
        dishUI.highlightButton("Dish");
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

    /**
     * Preloads the Inventory UI to improve performance.
     */

    @Override
    public void preloadInventoryUI() {
        if (inventoryUI == null) {
            inventoryUI = new InventoryUI(this);
        }
    }

    /**
     * Preloads the Sales UI to improve performance.
     */
    @Override
    public void preloadSalesUI() {
        if (salesUI == null) {
            salesUI = new SalesUI(this);
        }
    }

    /**
     * Preloads the Table Overview UI to improve performance.
     */
    @Override
    public void preloadTableOverviewUI() {
        if (tableOverviewUI == null) {
            tableOverviewUI = new TableOverviewUI(this);
        }
    }

    /**
     * Preloads the Menus UI to improve performance.
     */
    @Override
    public void preloadMenusUI() {
        if (menusUI == null) {
            menusUI = new MenusUI(this);
        }
    }

    /**
     * Preloads the Staff UI to improve performance.
     */
    @Override
    public void preloadStaffUI() {
        if (staffUI == null) {
            staffUI = new StaffUI(this);
        }
    }

    /**
     * Preloads the Wine UI to improve performance.
     */
    @Override
    public void preloadWineUI() {
        if (wineUI == null) {
            wineUI = new WineUI(this);
        }
    }

    /**
     * Preloads the Stock Orders UI to improve performance.
     */
    @Override
    public void preloadStockOrdersUI() {
        if (stockOrdersUI == null) {
            stockOrdersUI = new StockOrdersUI(this);
        }
    }

    /**
     * Preloads the Waste UI to improve performance.
     */
    @Override
    public void preloadWasteUI() {
        if (wasteUI == null) {
            wasteUI = new WasteUI(this);
        }
    }

    /**
     * Preloads the Dish UI to improve performance.
     */
    @Override
    public void preloadDishUI() {
        if (dishUI == null) {
            dishUI = new DishUI(this);
        }
    }

}
