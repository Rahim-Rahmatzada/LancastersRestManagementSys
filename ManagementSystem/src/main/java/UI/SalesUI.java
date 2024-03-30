package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import model.DatabaseConnector;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SalesUI extends BaseUI {
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private VBox graphContainer;
    private GraphCreator graphCreator;

    private CheckBox dishCheckBox;
    private CheckBox wineCheckBox;
    private CheckBox totalCheckBox;

    private Text popularDishText;
    private Text popularWineText;

    public SalesUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Sales");
        setTopText("SALES OVERVIEW");

        // Set the main content for the SalesUI.
        VBox salesMainContent = new VBox();
        salesMainContent.setPadding(new Insets(10));
        salesMainContent.setSpacing(10);

        // Create date pickers for start and end dates
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();

        // Create check boxes for selecting dish, wine, or total
        dishCheckBox = new CheckBox("Dish");
        wineCheckBox = new CheckBox("Wine");
        totalCheckBox = new CheckBox("Total");

        dishCheckBox.setStyle("-fx-text-fill: white;");
        wineCheckBox.setStyle("-fx-text-fill: white;");
        totalCheckBox.setStyle("-fx-text-fill: white;");


        // Create a button to generate the graph using ButtonCreator
        Button generateGraphButton = ButtonCreator.createButton(
                "Generate Graph",
                14, // font size
                "#FFFFFF", // text color in hex
                button -> generateGraph() // click action
        );

        generateGraphButton.setTranslateX(20);
        generateGraphButton.setTranslateY(-10);

        // Create an HBox to hold the date pickers, check boxes, and generate button
        HBox controlsBox = new HBox();
        controlsBox.setSpacing(10);
        controlsBox.getChildren().addAll(
                startDatePicker,
                endDatePicker,
                dishCheckBox,
                wineCheckBox,
                totalCheckBox
        );

        controlsBox.getChildren().add(generateGraphButton);

        // Instantiate the GraphCreator
        graphCreator = new GraphCreator();

        // Create a container for the graph
        graphContainer = new VBox();
        graphContainer.getChildren().add(graphCreator.getLineChart());

        // Create the popular dish and wine boxes
        HBox popularItemsBox = createPopularItemsBox();

        salesMainContent.getChildren().addAll(controlsBox, graphContainer, popularItemsBox);
        setMainContent(salesMainContent);
    }

    private void generateGraph() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                showAlert("Invalid Dates", "Start date cannot be after end date.");
                return;
            }

            if (!dishCheckBox.isSelected() && !wineCheckBox.isSelected() && !totalCheckBox.isSelected()) {
                showAlert("No Variable Selected", "Please select at least one option: Dish, Wine, or Total.");
                return;
            }

            graphCreator.clearGraph();

            try (Connection conn = DatabaseConnector.getConnection()) {
                updatePopularItems(startDate, endDate, conn);

                if (dishCheckBox.isSelected()) {
                    updateDishSalesGraph(startDate, endDate, conn);
                }

                if (wineCheckBox.isSelected()) {
                    updateWineSalesGraph(startDate, endDate, conn);
                }

                if (totalCheckBox.isSelected()) {
                    updateTotalSalesGraph(startDate, endDate, conn);
                }
            } catch (SQLException e) {
                showAlert("Database Error", "An error occurred while accessing the database.");
            }
        }
    }

    private void updatePopularItems(LocalDate startDate, LocalDate endDate, Connection conn) throws SQLException {
        String popularDishQuery = "SELECT d.name AS dishName, COUNT(*) AS totalSold " +
                "FROM Sale_Dish sd " +
                "JOIN Dish d ON sd.dishID = d.dishID " +
                "JOIN Sale s ON sd.saleID = s.saleID " +
                "WHERE s.date BETWEEN ? AND ? " +
                "GROUP BY d.name " +
                "ORDER BY totalSold DESC " +
                "LIMIT 1";

        String popularWineQuery = "SELECT w.name AS wineName, COUNT(*) AS totalSold " +
                "FROM Sale_Dish sd " +
                "JOIN Dish d ON sd.dishID = d.dishID " +
                "JOIN Wine w ON d.wineID = w.wineID " +
                "JOIN Sale s ON sd.saleID = s.saleID " +
                "WHERE s.date BETWEEN ? AND ? " +
                "GROUP BY w.name " +
                "ORDER BY totalSold DESC " +
                "LIMIT 1";

        updatePopularItem(popularDishQuery, startDate, endDate, conn, popularDishText);
        updatePopularItem(popularWineQuery, startDate, endDate, conn, popularWineText);
    }

    private void updatePopularItem(String query, LocalDate startDate, LocalDate endDate, Connection conn, Text itemText) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String itemName = rs.getString(1);
                    itemText.setText(itemName);
                } else {
                    itemText.setText("N/A");
                }
            }
        }
    }

    private void updateDishSalesGraph(LocalDate startDate, LocalDate endDate, Connection conn) throws SQLException {
        String dishQuery = "SELECT DATE(s.date) AS saleDate, SUM(d.price) AS totalSales " +
                "FROM Sale s " +
                "JOIN Sale_Dish sd ON s.saleID = sd.saleID " +
                "JOIN Dish d ON sd.dishID = d.dishID " +
                "WHERE s.date BETWEEN ? AND ? " +
                "GROUP BY DATE(s.date)";

        updateSalesGraph(dishQuery, startDate, endDate, conn, "Dish Sales");
    }

    private void updateWineSalesGraph(LocalDate startDate, LocalDate endDate, Connection conn) throws SQLException {
        String wineQuery = "SELECT DATE(s.date) AS saleDate, SUM(w.winePrice) AS totalSales " +
                "FROM Sale s " +
                "JOIN Sale_Dish sd ON s.saleID = sd.saleID " +
                "JOIN Dish d ON sd.dishID = d.dishID " +
                "JOIN Wine w ON d.wineID = w.wineID " +
                "WHERE s.date BETWEEN ? AND ? " +
                "GROUP BY DATE(s.date)";

        updateSalesGraph(wineQuery, startDate, endDate, conn, "Wine Sales");
    }

    private void updateTotalSalesGraph(LocalDate startDate, LocalDate endDate, Connection conn) throws SQLException {
        String totalQuery = "SELECT DATE(s.date) AS saleDate, SUM(d.price + w.winePrice) AS totalSales " +
                "FROM Sale s " +
                "JOIN Sale_Dish sd ON s.saleID = sd.saleID " +
                "JOIN Dish d ON sd.dishID = d.dishID " +
                "JOIN Wine w ON d.wineID = w.wineID " +
                "WHERE s.date BETWEEN ? AND ? " +
                "GROUP BY DATE(s.date)";

        updateSalesGraph(totalQuery, startDate, endDate, conn, "Total Sales");
    }

    private void updateSalesGraph(String query, LocalDate startDate, LocalDate endDate, Connection conn, String seriesName) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                List<Double> salesData = new ArrayList<>();
                while (rs.next()) {
                    double totalSales = rs.getDouble("totalSales");
                    salesData.add(totalSales);
                }
                graphCreator.addSeriesToGraph(salesData, seriesName, startDate, "Total Sales");
            }
        }
    }

    private HBox createPopularItemsBox() {
        HBox popularItemsBox = new HBox();
        popularItemsBox.setSpacing(20);


        popularItemsBox.setTranslateX(250);


        popularDishText = new Text();
        popularWineText = new Text();

        VBox popularDishBox = createPopularItemBox("Most Popular Dish", popularDishText, 16, 12);
        VBox popularWineBox = createPopularItemBox("Most Popular Wine", popularWineText, 16, 12);

        popularDishBox.setPrefWidth(200); // Set the preferred width
        popularDishBox.setPrefHeight(100); // Set the preferred height

        popularWineBox.setPrefWidth(200); // Set the preferred width
        popularWineBox.setPrefHeight(100); // Set the preferred height


        popularItemsBox.getChildren().addAll(popularDishBox, popularWineBox);

        return popularItemsBox;
    }

    private VBox createPopularItemBox(String title, Text itemText, int titleFontSize, int itemFontSize) {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 10;");
        box.setPadding(new Insets(10));
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER); // Center align the content

        Text titleText = new Text(title);
        titleText.setFont(Font.font(titleFontSize));
        titleText.setFill(Color.BLACK);

        itemText.setFont(Font.font(itemFontSize));
        itemText.setFill(Color.BLACK);
        itemText.setTextAlignment(TextAlignment.CENTER); // Center align the item name

        box.getChildren().addAll(titleText, itemText);

        return box;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}