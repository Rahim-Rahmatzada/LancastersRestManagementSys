package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainUI extends Application implements UISwitcher {

    private Stage primaryStage;
    private DashboardUI dashboardUI;
    private InventoryUI inventoryUI;
    private SalesUI salesUI;
    private BookingsUI bookingsUI;
    private MenusUI menusUI;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        switchToDashboard();
        primaryStage.show();
    }

    @Override
    public void switchToDashboard() {
        if (dashboardUI == null) {
            dashboardUI = new DashboardUI(this);
        }
        VBox dashboardContent = dashboardUI.createContent();
        switchScene(dashboardContent, "Dashboard UI");
        dashboardUI.highlightButton("Dashboard");
    }

    @Override
    public void switchToInventory() {
        if (inventoryUI == null) {
            inventoryUI = new InventoryUI(this);
        }
        VBox inventoryContent = inventoryUI.createContent();
        switchScene(inventoryContent, "Inventory UI");
        inventoryUI.highlightButton("Inventory");
    }


    @Override
    public void switchToSales() {
        if (salesUI == null) {
            salesUI = new SalesUI(this);
        }
        VBox inventoryContent = salesUI.createContent();
        switchScene(inventoryContent, "Sales UI");
        salesUI.highlightButton("Sales");

    }

    @Override
    public void switchToBookings() {
        if (bookingsUI == null) {
            bookingsUI = new BookingsUI(this);
        }
        VBox inventoryContent = bookingsUI.createContent();
        switchScene(inventoryContent, "Bookings UI");
        bookingsUI.highlightButton("Bookings");
    }

    @Override
    public void switchToMenus() {
        if (menusUI == null) {
            menusUI = new MenusUI(this);
        }
        VBox inventoryContent = menusUI.createContent();
        switchScene(inventoryContent, "Menus UI");
        menusUI.highlightButton("Menus");
    }

    private void switchScene(VBox content, String title) {
        Scene scene = new Scene(content, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
    }
}
