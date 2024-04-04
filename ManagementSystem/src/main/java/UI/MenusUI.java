package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import javafx.util.Pair;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class MenusUI extends BaseUI {

    public MenusUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Menus");
        setTopText("MENU Overview");

        // Set up the main content
        VBox menusMainContent = createMenusMainContent();
        setMainContent(menusMainContent);
    }

    private VBox createMenusMainContent() {
        VBox mainContent = new VBox(20);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(50));

        Button createMenuButton = new Button("Create Menu");
        createMenuButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 5px;");
        createMenuButton.setOnAction(event -> createMenu());

        Button editMenuButton = new Button("Edit Menu");
        editMenuButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 5px;");
        editMenuButton.setOnAction(event -> editMenu());

        Button archiveMenuButton = new Button("Archive Menu");
        archiveMenuButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 5px;");
        archiveMenuButton.setOnAction(event -> archiveMenu());

        Button manageDishesButton = new Button("Manage Dishes");
        manageDishesButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 5px;");
        manageDishesButton.setOnAction(event -> manageDishes());

        mainContent.getChildren().addAll(createMenuButton, editMenuButton, archiveMenuButton, manageDishesButton);

        return mainContent;
    }

    private void createMenu() {
        // Create a dialog for menu creation
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(getRootLayout().getScene().getWindow());
        dialog.setTitle("Create Menu");

        // Set the button types
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create a VBox to hold the content
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        // Create text field for menu name
        TextField menuNameField = new TextField();
        menuNameField.setPromptText("Menu Name");

        // Create ListView for dishes
        ObservableList<String> dishes = FXCollections.observableArrayList();
        ListView<String> dishListView = new ListView<>(dishes);
        dishListView.setPrefHeight(200);
        dishListView.setEditable(true);

        // Create HBox for adding new dish
        HBox addDishBox = new HBox(10);
        TextField dishNameField = new TextField();
        dishNameField.setPromptText("Dish Name");
        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        CheckBox allergenCheckBox = new CheckBox("Contains Allergen");
        Button addButton = new Button("Add Dish");
        addButton.setOnAction(event -> {
            String dishName = dishNameField.getText();
            String price = priceField.getText();
            String allergenInfo = allergenCheckBox.isSelected() ? " (Contains Allergen)" : "";
            if (!dishName.isEmpty() && !price.isEmpty()) {
                dishes.add(dishName + " - $" + price + allergenInfo);
                dishNameField.clear();
                priceField.clear();
                allergenCheckBox.setSelected(false);
            }
        });
        addDishBox.getChildren().addAll(dishNameField, priceField, allergenCheckBox, addButton);

        content.getChildren().addAll(menuNameField, dishListView, addDishBox);

        // Create HBox for allergen search
        HBox searchBox = new HBox(10);
        TextField searchField = new TextField();
        searchField.setPromptText("Search Allergen");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> searchAllergen(searchField.getText()));
        searchBox.getChildren().addAll(searchField, searchButton);

        content.getChildren().add(searchBox);

        // Set the content of the dialog pane
        dialog.getDialogPane().setContent(content);

        // Set the result converter
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // Retrieve menu name and dish details
                String menuName = menuNameField.getText();
                List<String> dishDetails = new ArrayList<>(dishes);
                // Perform menu creation logic here
                System.out.println("Menu Name: " + menuName);
                System.out.println("Dishes: " + dishDetails);
            }
            return null;
        });

        // Show the dialog
        dialog.showAndWait();    }
    // Method to add dish input fields dynamically
    private void addDishInputFields(VBox content, List<TextField> dishNameFields, List<TextField> priceFields) {
        TextField dishNameField = new TextField();
        dishNameField.setPromptText("Dish Name");
        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(dishNameField, priceField);
        dishNameFields.add(dishNameField);
        priceFields.add(priceField);
        content.getChildren().add(hbox);
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void searchAllergen(String allergen) {
        // Implement allergen search functionality here
        System.out.println("Searching for allergen: " + allergen);
        // Display search results in a dialog or update UI accordingly
    }
    private void reviewMenuCost(String menuName, List<String> dishDetails) {
        double totalCost = 0.0;
        for (String dish : dishDetails) {
            // Extract dish price from the string
            String[] parts = dish.split(" - \\$");
            if (parts.length == 2) {
                double price = Double.parseDouble(parts[1]);
                totalCost += price;
            }
        }
        // Display total menu cost in an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Menu Cost Review");
        alert.setHeaderText("Menu: " + menuName);
        alert.setContentText("Total Cost: $" + totalCost);
        alert.showAndWait();
    }

    private void editMenu() {
        // Implement functionality to edit an existing menu
        
    }

    private void archiveMenu() {
        // Implement functionality to archive a menu
    }

    private void manageDishes() {
        // Implement functionality to manage dishes
    }

    @Override
    public BorderPane getRootLayout() {
        return super.getRootLayout();
    }
}


//    Menus UI
//    Menu Creation and Management: Create new menus, edit existing ones, and archive outdated menus.
//        Dish Construction and Editing: Combine approved recipes into dishes; edit ingredients, preparation, and presentation details.
//        Price Setting: Determine and update pricing for each dish based on cost and markup strategy.
//        Allergen Information: Ensure all allergen information is up to date and clearly labeled on menu items.
//        Menu Review: Allow management and other stakeholders to review and provide feedback on draft menus.
//        Printable Menus: Generate printable versions of the final menus for use in the restaurant.

//    Menu Costing and Review: Oversee menu pricing, ingredient cost tracking, and ensure profitability.