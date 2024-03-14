package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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
        switchScene(dashboardUI, "Dashboard UI");
        dashboardUI.highlightButton("Dashboard");
    }

    @Override
    public void switchToInventory() {
        if (inventoryUI == null) {
            inventoryUI = new InventoryUI(this);
        }
        switchScene(inventoryUI, "Inventory UI"); // Use the updated switchScene method
        inventoryUI.highlightButton("Inventory");
    }



    @Override
    public void switchToSales() {
        if (salesUI == null) {
            salesUI = new SalesUI(this);
        }
        switchScene(salesUI, "Sales UI"); // Use the updated switchScene method
        salesUI.highlightButton("Sales");
    }


    @Override
    public void switchToBookings() {
        if (bookingsUI == null) {
            bookingsUI = new BookingsUI(this);
        }
        switchScene(bookingsUI, "Bookings UI"); // Use the updated switchScene method
        bookingsUI.highlightButton("Bookings");
    }


    @Override
    public void switchToMenus() {
        if (menusUI == null) {
            menusUI = new MenusUI(this);
        }
        switchScene(menusUI, "Menus UI"); // Use the updated switchScene method
        menusUI.highlightButton("Menus");
    }


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

}
