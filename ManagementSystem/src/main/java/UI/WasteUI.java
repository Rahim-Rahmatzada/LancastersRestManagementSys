package UI;

import DatabaseConnections.AdminDatabaseConnector;
import forAdmin.KitchenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.WasteEntry;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class WasteUI extends BaseUI {

    private TableView<WasteEntry> wasteTableView;
    private Text highestWasteIngredientText;
    private Text biggestReasonForWasteText;

    public WasteUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Waste");
        setTopText("Waste Information");

        // Set the main content for the WasteUI
        VBox wasteMainContent = new VBox();
        wasteMainContent.setPadding(new Insets(20));
        wasteMainContent.setSpacing(10);

        VBox highestWasteIngredientBox = createHighestWasteIngredientBox();
        VBox biggestReasonForWasteBox = createBiggestReasonForWasteBox();
        HBox wasteInfoBox = new HBox();
        wasteInfoBox.setSpacing(50);
        wasteInfoBox.setAlignment(Pos.CENTER);
        wasteInfoBox.getChildren().addAll(highestWasteIngredientBox, biggestReasonForWasteBox);

        // Create a TableView to display waste data
        wasteTableView = createWasteTableView();



        wasteMainContent.getChildren().addAll(wasteTableView,wasteInfoBox);
        setMainContent(wasteMainContent);

        showHighestWasteIngredient();
        showBiggestReasonForWaste();

    }

    private void showHighestWasteIngredient() {
        KitchenController kitchenController = new KitchenController();
        HashMap<String, String> wasteInfo = kitchenController.getWasteInfo();

        if (!wasteInfo.isEmpty()) {
            String highestWasteIngredient = wasteInfo.entrySet().stream()
                    .max(Comparator.comparingDouble(entry -> extractQuantity(entry.getValue())))
                    .map(Map.Entry::getKey)
                    .orElse("");

            String wasteData = wasteInfo.get(highestWasteIngredient);
            double quantity = extractQuantity(wasteData);

            highestWasteIngredientText.setText(highestWasteIngredient + ": " + quantity);
        } else {
            highestWasteIngredientText.setText("No waste data available.");
        }
    }

    private void showBiggestReasonForWaste() {
        KitchenController kitchenController = new KitchenController();
        HashMap<String, String> wasteInfo = kitchenController.getWasteInfo();

        if (!wasteInfo.isEmpty()) {
            String biggestReason = wasteInfo.values().stream()
                    .max(Comparator.comparingDouble(this::extractQuantity))
                    .map(this::extractReason)
                    .orElse("");

            double totalWaste = wasteInfo.values().stream()
                    .filter(data -> data.contains(biggestReason))
                    .mapToDouble(this::extractQuantity)
                    .sum();

            biggestReasonForWasteText.setText(biggestReason + ": " + totalWaste);
        } else {
            biggestReasonForWasteText.setText("No waste data available.");
        }
    }

    private double extractQuantity(String wasteData) {
        String[] parts = wasteData.split(", ");
        String quantityPart = parts[0].split(": ")[1];
        return Double.parseDouble(quantityPart);
    }

    private String extractReason(String wasteData) {
        String[] parts = wasteData.split(", ");
        return parts[1].split(": ")[1];
    }

    private VBox createHighestWasteIngredientBox() {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 10;");
        box.setPadding(new Insets(10));
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);

        Text titleText = new Text("Ingredient With Highest Waste");
        titleText.setFont(Font.font(16));
        titleText.setFill(Color.BLACK);

        highestWasteIngredientText = new Text();
        highestWasteIngredientText.setFont(Font.font(12));
        highestWasteIngredientText.setFill(Color.BLACK);
        highestWasteIngredientText.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(titleText, highestWasteIngredientText);

        box.setMinWidth(250);
        box.setMaxWidth(250);

        return box;
    }

    private VBox createBiggestReasonForWasteBox() {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 10;");
        box.setPadding(new Insets(10));
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);

        Text titleText = new Text("Biggest Reason For Waste");
        titleText.setFont(Font.font(16));
        titleText.setFill(Color.BLACK);

        biggestReasonForWasteText = new Text();
        biggestReasonForWasteText.setFont(Font.font(12));
        biggestReasonForWasteText.setFill(Color.BLACK);
        biggestReasonForWasteText.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(titleText, biggestReasonForWasteText);

        box.setMinWidth(250);
        box.setMaxWidth(250);

        return box;
    }

    private TableView<WasteEntry> createWasteTableView() {
        TableView<WasteEntry> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #1A1A1A;");

        // Create table columns
        TableColumn<WasteEntry, String> ingredientColumn = new TableColumn<>("Ingredient");
        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
        ingredientColumn.setStyle("-fx-text-fill: white;");

        TableColumn<WasteEntry, String> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setStyle("-fx-text-fill: white;");

        TableColumn<WasteEntry, String> reasonColumn = new TableColumn<>("Reason");
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        reasonColumn.setStyle("-fx-text-fill: white;");

        // Add columns to the table view
        tableView.getColumns().addAll(ingredientColumn, quantityColumn, reasonColumn);

        // Load waste data from the KitchenController
        ObservableList<WasteEntry> wasteData = getWasteDataFromKitchenController();

        // Set the data to the table view
        tableView.setItems(wasteData);

        // Set the cell factory to style table cells
        tableView.setRowFactory(tv -> {
            TableRow<WasteEntry> row = new TableRow<>();
            row.setStyle("-fx-background-color: #1A1A1A;");

            // Change the highlight color of the selected cell
            row.setOnMouseEntered(event -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: #333333;");
                }
            });

            row.setOnMouseExited(event -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: #1A1A1A;");
                }
            });

            return row;
        });

        // Remove the empty column
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return tableView;
    }

    private ObservableList<WasteEntry> getWasteDataFromKitchenController() {
        ObservableList<WasteEntry> wasteList = FXCollections.observableArrayList();

        KitchenController kitchenController = new KitchenController();
        HashMap<String, String> wasteInfo = kitchenController.getWasteInfo();

        for (String ingredientName : wasteInfo.keySet()) {
            String[] wasteData = wasteInfo.get(ingredientName).split(", ");
            String quantity = wasteData[0].split(": ")[1];
            String reason = wasteData[1].split(": ")[1];

            WasteEntry wasteEntry = new WasteEntry(ingredientName, quantity, reason);
            wasteList.add(wasteEntry);
        }

        return wasteList;
    }
}