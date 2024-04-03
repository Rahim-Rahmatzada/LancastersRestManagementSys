package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This abstract class serves as a base for creating UI components in the application.
 * It provides common UI elements and functionalities such as side menu and top text.
 */

public abstract class BaseUI {
    protected UISwitcher uiSwitcher;
    protected Button lastSelectedButton = null;
    protected Map<String, Button> buttons = new LinkedHashMap<>();
    private BorderPane rootLayout = new BorderPane();
    private Label topText = new Label();
    protected VBox mainContentContainer;

    /**
     * Constructs the base UI and initializes the layout.
     *
     * @param uiSwitcher The UI switcher for navigation between different views.
     */

    public BaseUI(UISwitcher uiSwitcher) {
        this.uiSwitcher = uiSwitcher;
        initializeLayout();

    }

    /**
     * Initializes the UI layout, including the top text and the side menu.
     */

    private void initializeLayout() {
        topText.setStyle("-fx-font-size: 32px; -fx-padding: 10px; -fx-text-fill: white;");
        topText.setAlignment(Pos.CENTER_LEFT); // Align the top text to the left

        // Create a new VBox for the top text and the main content
        VBox centerContent = new VBox();
        centerContent.setStyle("-fx-background-color: #1A1A1A;");
        centerContent.setAlignment(Pos.TOP_LEFT); // Align the content to the top and left

        // Add the topText to the top of the VBox with padding on the left
        VBox.setMargin(topText, new Insets(0, 0, 0, 20)); // Adjust the left padding as needed
        centerContent.getChildren().add(topText);

        // Create a container for the main content
        mainContentContainer = new VBox();
        mainContentContainer.setStyle("-fx-background-color: #1A1A1A;");

        // Add the mainContentContainer below the topText in the VBox
        centerContent.getChildren().add(mainContentContainer);

        // Set the side menu on the left side of the BorderPane
        VBox buttonContainer = createSideMenuContent();
        rootLayout.setLeft(buttonContainer);

        // Set the centerContent VBox as the center content of the BorderPane
        rootLayout.setCenter(centerContent);
    }

    /**
     * Sets the main content displayed in the center of the UI.
     *
     * @param content The node to be set as the main content.
     */

    protected void setMainContent(Node content) {
        mainContentContainer.getChildren().clear(); // Clear any previous content
        mainContentContainer.getChildren().add(content); // Add new content
    }

    /**
     * Sets the text displayed at the top of the UI.
     *
     * @param text The text to display.
     */

    protected void setTopText(String text) {
        topText.setText(text);
    }

    /**
     * Returns the root layout of the UI.
     *
     * @return The root layout as a BorderPane.
     */

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    /**
     * Creates the content for the side menu, including buttons for navigation.
     *
     * @return A VBox containing the side menu content.
     */

    public VBox createSideMenuContent() {
        VBox buttonContainer = new VBox();
        buttonContainer.setAlignment(Pos.TOP_LEFT);
        buttonContainer.setPadding(new Insets(10));
        buttonContainer.setSpacing(10);
        buttonContainer.setStyle("-fx-background-color: #1A1A1A;");

        // Using LinkedHashMap to preserve the order of insertion
        Map<String, String> buttonInfo = new LinkedHashMap<>();
        buttonInfo.put("Dashboard", "graph.png");
        buttonInfo.put("Inventory", "box.png");
        buttonInfo.put("Sales", "money.png");
        buttonInfo.put("Restaurant Capacity", "calender.png");
        buttonInfo.put("Menus", "book.png");
        buttonInfo.put("Staff", "staff.png");
        buttonInfo.put("Wine", "wine.png");
        buttonInfo.put("Stock Orders", "calender.png");

        for (Map.Entry<String, String> entry : buttonInfo.entrySet()) {
            buttonContainer.getChildren().add(createSideMenuButtons(entry.getKey(), 10, entry.getValue()));
        }

        return buttonContainer;
    }

    /**
     * Creates a button for the side menu.
     *
     * @param label     The text label of the button.
     * @param leftPadding The padding on the left side of the button.
     * @param imagePath The path to the image icon for the button.
     * @return A button for use in the side menu.
     */

    protected Button createSideMenuButtons(String label, int leftPadding, String imagePath) {
        Button button = new Button(label);

        button.setPrefSize(200, 50);
        button.setPadding(new Insets(0, 0, 0, leftPadding));
        button.setStyle("-fx-background-color: #1A1A1A; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-background-radius: 15; -fx-alignment: center-left;");

        // Load image and set as graphic on the button
        Image image = new Image(getClass().getResourceAsStream("/Images/" + imagePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(24); // Set the image size appropriately
        imageView.setFitHeight(24);
        button.setGraphic(imageView);


        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-background-radius: 15; -fx-alignment: center-left;"));
        button.setOnMouseExited(e -> {
            if (button != lastSelectedButton) {
                button.setStyle("-fx-background-color: #1A1A1A; -fx-text-fill: white; -fx-font-size: 14px; " +
                        "-fx-background-radius: 15; -fx-alignment: center-left;");
            }
        });

        button.setOnAction(e -> handleButtonAction(label));

        buttons.put(label,button);

        return button;
    }

    /**
     * Highlights the specified button and unhighlights all others. This method
     * first unhighlights all buttons, then highlights the button associated with the given label.
     *
     * @param label The label of the button to highlight.
     */

    protected void highlightButton(String label) {
        // First, unhighlight all buttons
        buttons.forEach((name, btn) -> unhighlightButton(btn));

        // Highlight the button with the given label
        Button button = buttons.get(label);
        if (button != null) {
            highlightButton(button);
        }
    }

    /**
     * Applies a highlight style to the specified button. This method changes the button's
     * style to indicate that it is currently selected or active.
     * This method is intended for internal use by highlightButton(String label).
     *
     * @param button The button to highlight.
     */

    private void highlightButton(Button button) {
        button.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-background-radius: 15; -fx-alignment: center-left;");
        lastSelectedButton = button;
    }

    /**
     * Removes the highlight style from the specified button. This method resets the button's
     * style to its default state, indicating that it is not currently selected.
     * This method is intended for internal use by highlightButton(String label).
     *
     * @param button The button to unhighlight.
     */

    private void unhighlightButton(Button button) {
        button.setStyle("-fx-background-color: #1A1A1A; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-background-radius: 15; -fx-alignment: center-left;");
    }

    //java doc this



    /**
     * Handles the action when a button is clicked.
     *
     * @param label The label of the button that was clicked.
     */

    protected void handleButtonAction(String label) {
        switch (label) {
            case "Dashboard" -> uiSwitcher.switchToDashboard();
            case "Inventory" -> uiSwitcher.switchToInventory();
            case "Sales" -> uiSwitcher.switchToSales();
            case "Restaurant Capacity" -> uiSwitcher.switchToRestaurantCapacity();
            case "Menus" -> uiSwitcher.switchToMenus();
            case "Staff" -> uiSwitcher.switchToStaff();
            case "Wine" -> uiSwitcher.switchToWine();
            case "Stock Orders" -> uiSwitcher.switchToStockOrders();

        }
        highlightButton(label); // Highlight the clicked button
    }

    /**
     * Returns the main content container.
     *
     * @return The main content container as a VBox.
     */
    protected VBox getMainContent() {
        return mainContentContainer;
    }
}
