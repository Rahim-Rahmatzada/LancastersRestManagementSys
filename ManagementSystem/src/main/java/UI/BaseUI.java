package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseUI {
    protected UISwitcher uiSwitcher;
    protected Button lastSelectedButton = null;

    protected Map<String, Button> buttons = new LinkedHashMap<>();

    private BorderPane rootLayout = new BorderPane();
    private Label topText = new Label();

    protected VBox mainContentContainer;


    public BaseUI(UISwitcher uiSwitcher) {
        this.uiSwitcher = uiSwitcher;
        initializeLayout();

    }

    private void initializeLayout() {
        topText.setStyle("-fx-font-size: 32px; -fx-padding: 10px; -fx-text-fill: white;");

        // Create a new container for the top text and the main content.
        HBox topAndMainContent = new HBox();
        topAndMainContent.setStyle("-fx-background-color: #1A1A1A;");
        // Add the topText to the left side of the HBox
        topAndMainContent.getChildren().add(topText);
        // Now create a container for the main content.
        mainContentContainer = new VBox();
        mainContentContainer.setStyle("-fx-background-color: #1A1A1A;");
        // Add the mainContentContainer to the right side of the HBox.
        topAndMainContent.getChildren().add(mainContentContainer);

        // Set the side menu on the left side of the BorderPane.
        VBox buttonContainer = createSideMenuContent();
        rootLayout.setLeft(buttonContainer);

        // Set the HBox as the center content of the BorderPane.
        rootLayout.setCenter(topAndMainContent);

        System.out.println("it happened");
    }

    protected void setMainContent(Node content) {
        mainContentContainer.getChildren().clear(); // Clear any previous content
        mainContentContainer.getChildren().add(content); // Add new content
    }

    protected void setTopText(String text) {
        topText.setText(text);
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

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
        buttonInfo.put("Bookings", "calender.png");
        buttonInfo.put("Menus", "book.png");

        for (Map.Entry<String, String> entry : buttonInfo.entrySet()) {
            buttonContainer.getChildren().add(createSideMenuButtons(entry.getKey(), 10, entry.getValue()));
        }

        return buttonContainer;
    }

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

    protected void highlightButton(String label) {
        // First, unhighlight all buttons
        buttons.forEach((name, btn) -> unhighlightButton(btn));

        // Highlight the button with the given label
        Button button = buttons.get(label);
        if (button != null) {
            highlightButton(button);
        }
    }

    private void highlightButton(Button button) {
        button.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-background-radius: 15; -fx-alignment: center-left;");
        lastSelectedButton = button;
    }

    private void unhighlightButton(Button button) {
        button.setStyle("-fx-background-color: #1A1A1A; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-background-radius: 15; -fx-alignment: center-left;");
    }


    protected void handleButtonAction(String label) {
        switch (label) {
            case "Dashboard" -> uiSwitcher.switchToDashboard();
            case "Inventory" -> uiSwitcher.switchToInventory();
            case "Sales" -> uiSwitcher.switchToSales();
            case "Bookings" -> uiSwitcher.switchToBookings();
            case "Menus" -> uiSwitcher.switchToMenus();

        }
        highlightButton(label); // Highlight the clicked button
    }
}
