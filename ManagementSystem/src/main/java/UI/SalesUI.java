package UI;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import DatabaseConnections.AdminDatabaseConnector;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.cell.PropertyValueFactory;
import model.SoldItem;

/**
 * The `SalesUI` class extends the `BaseUI` class and represents the user interface
 * for managing sales, generating sales reports, and viewing popular items
 * in the restaurant management system.
 */
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

    private Button viewItemsButton;
    private TableView<SoldItem> itemTableView;
    private TableView<SoldItem> dishTableView;
    private TableView<SoldItem> wineTableView;

    /**
     * Constructs a new instance of the `SalesUI` class.
     *
     * @param uiSwitcher The `UISwitcher` instance for navigating between UI screens.
     */
    public SalesUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Sales");
        setTopText("Sales Overview");

        dishTableView = createDishTableView();
        wineTableView = createWineTableView();

        // Set the main content for the SalesUI.
        VBox salesMainContent = new VBox();
        salesMainContent.setPadding(new Insets(10));
        salesMainContent.setSpacing(10);

        // Create date pickers for start and end dates
        startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Starting Date");

        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Ending date");

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

        viewItemsButton = ButtonCreator.createButton(
                "View List of Items Sold",
                14, // font size
                "#FFFFFF", // text color in hex
                button -> viewSoldItems() // click action
        );

        viewItemsButton.setTranslateX(40);
        viewItemsButton.setTranslateY(-10);

        // Create an HBox to hold the date pickers, check boxes, and generate button
        HBox controlsBox = new HBox();
        controlsBox.setSpacing(10);
        controlsBox.getChildren().addAll(
                startDatePicker,
                endDatePicker,
                dishCheckBox,
                wineCheckBox,
                totalCheckBox,
                generateGraphButton,
                viewItemsButton
        );



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

    /**
     * Creates and configures the `TableView` for displaying sold dish data.
     *
     * @return The configured `TableView` instance for displaying sold dishes.
     */
    private TableView<SoldItem> createDishTableView() {
        TableView<SoldItem> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #1A1A1A;");

        TableColumn<SoldItem, String> nameColumn = new TableColumn<>("Dish Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setStyle("-fx-text-fill: white;");

        TableColumn<SoldItem, Double> priceColumn = new TableColumn<>("Price (£)");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setStyle("-fx-text-fill: white;");

        TableColumn<SoldItem, Integer> quantityColumn = new TableColumn<>("Quantity Sold");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setStyle("-fx-text-fill: white;");

        TableColumn<SoldItem, Double> totalSaleColumn = new TableColumn<>("Total Sale For Item (£)");
        totalSaleColumn.setCellValueFactory(cellData -> {
            SoldItem soldItem = cellData.getValue();
            double totalSale = soldItem.getPrice() * soldItem.getQuantity();
            return new SimpleDoubleProperty(totalSale).asObject();
        });
        totalSaleColumn.setStyle("-fx-text-fill: white;");

        tableView.getColumns().addAll(nameColumn, priceColumn,quantityColumn,totalSaleColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set the cell factory to style table cells
        tableView.setRowFactory(tv -> {
            TableRow<SoldItem> row = new TableRow<>();
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

        return tableView;
    }

    /**
     * Creates and configures the `TableView` for displaying sold wine data.
     *
     * @return The configured `TableView` instance for displaying sold wines.
     */
    private TableView<SoldItem> createWineTableView() {
        TableView<SoldItem> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #1A1A1A;");

        TableColumn<SoldItem, String> nameColumn = new TableColumn<>("Wine Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setStyle("-fx-text-fill: white;");

        TableColumn<SoldItem, Double> priceColumn = new TableColumn<>("Price (£)");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setStyle("-fx-text-fill: white;");

        TableColumn<SoldItem, Integer> quantityColumn = new TableColumn<>("Quantity Sold");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setStyle("-fx-text-fill: white;");

        TableColumn<SoldItem, Double> totalSaleColumn = new TableColumn<>("Total Sale For Item (£)");
        totalSaleColumn.setCellValueFactory(cellData -> {
            SoldItem soldItem = cellData.getValue();
            double totalSale = soldItem.getPrice() * soldItem.getQuantity();
            return new SimpleDoubleProperty(totalSale).asObject();
        });
        totalSaleColumn.setStyle("-fx-text-fill: white;");

        tableView.getColumns().addAll(nameColumn, priceColumn,quantityColumn,totalSaleColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set the cell factory to style table cells
        tableView.setRowFactory(tv -> {
            TableRow<SoldItem> row = new TableRow<>();
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

        return tableView;
    }

    /**
     * Views the list of sold items based on the selected options (dish, wine, or total).
     */
    private void viewSoldItems() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showAlert("Invalid Date Range", "Please select both start and end dates.");
            return;
        }

        if (!dishCheckBox.isSelected() && !wineCheckBox.isSelected() && !totalCheckBox.isSelected()) {
            showAlert("No Selection", "Please select at least one option: Dish, Wine, or Total.");
            return;
        }

        // Clear the main content
        VBox mainContent = (VBox) getMainContent();
        mainContent.getChildren().clear();

        // Clear the table views
        dishTableView.getItems().clear();
        wineTableView.getItems().clear();

        if (dishCheckBox.isSelected()) {
            List<SoldItem> soldDishes = getSoldDishesWithQuantities(startDate, endDate);
            dishTableView.getItems().addAll(soldDishes);
            mainContent.getChildren().add(dishTableView);
        }

        if (wineCheckBox.isSelected()) {
            List<SoldItem> soldWines = getSoldWinesWithQuantities(startDate, endDate);
            wineTableView.getItems().addAll(soldWines);
            mainContent.getChildren().add(wineTableView);
        }

        // Create a "Back" button to go back to the sales overview
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showSalesOverview());

        backButton.setTranslateY(0);

        // Add the itemTableView and "Back" button to the main content
        mainContent.getChildren().addAll(backButton);
    }

    /**
     * Shows the sales overview with the date pickers, checkboxes, and buttons.
     */
    private void showSalesOverview() {
        VBox mainContent = (VBox) getMainContent();
        mainContent.getChildren().clear();

        // Clear the table views
        dishTableView.getItems().clear();
        wineTableView.getItems().clear();

        // Create an HBox to hold the date pickers, check boxes, and buttons
        HBox controlsBox = new HBox();
        controlsBox.setSpacing(10);
        controlsBox.setAlignment(Pos.CENTER_LEFT);
        controlsBox.getChildren().addAll(
                startDatePicker,
                endDatePicker,
                dishCheckBox,
                wineCheckBox,
                totalCheckBox,
                ButtonCreator.createButton(
                        "Generate Graph",
                        14,
                        "#FFFFFF",
                        button -> generateGraph()
                ),
                ButtonCreator.createButton(
                        "View List of Items Sold",
                        14,
                        "#FFFFFF",
                        button -> viewSoldItems()
                )
        );

        // Add the original sales overview components back to the main content
        mainContent.getChildren().addAll(
                controlsBox,
                graphContainer,
                createPopularItemsBox()
        );
    }

    /**
     * Retrieves the list of sold dishes with quantities for the given date range.
     *
     * @param startDate The start date of the date range.
     * @param endDate   The end date of the date range.
     * @return A list of `SoldItem` objects representing the sold dishes.
     */
    private List<SoldItem> getSoldDishesWithQuantities(LocalDate startDate, LocalDate endDate) {
        List<SoldItem> soldDishes = new ArrayList<>();

        try (Connection conn = AdminDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT d.name, d.price, COUNT(*) AS quantity " +
                             "FROM Sale s " +
                             "JOIN Sale_Dish sd ON s.saleID = sd.saleID " +
                             "JOIN Dish d ON sd.dishID = d.dishID " +
                             "WHERE s.date BETWEEN ? AND ? " +
                             "GROUP BY d.name, d.price")) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");
                    SoldItem soldItem = new SoldItem(name, price, quantity);
                    soldDishes.add(soldItem);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (soldDishes.isEmpty()) {
            showAlert("No Dishes Sold", "No dishes were sold within the selected date range.");
        }

        return soldDishes;
    }

    /**
     * Retrieves the list of sold wines with quantities for the given date range.
     *
     * @param startDate The start date of the date range.
     * @param endDate   The end date of the date range.
     * @return A list of `SoldItem` objects representing the sold wines.
     */
    private List<SoldItem> getSoldWinesWithQuantities(LocalDate startDate, LocalDate endDate) {
        List<SoldItem> soldWines = new ArrayList<>();

        try (Connection conn = AdminDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT w.wineName AS name, w.winePrice AS price, COUNT(*) AS quantity " +
                             "FROM Sale s " +
                             "JOIN Sale_Dish sd ON s.saleID = sd.saleID " +
                             "JOIN Dish d ON sd.dishID = d.dishID " +
                             "JOIN Wine w ON d.wineID = w.wineID " +
                             "WHERE s.date BETWEEN ? AND ? " +
                             "GROUP BY w.wineName, w.winePrice")) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");
                    SoldItem soldItem = new SoldItem(name, price, quantity);
                    soldWines.add(soldItem);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (soldWines.isEmpty()) {
            showAlert("No Wines Sold", "No wines were sold within the selected date range.");
        }

        return soldWines;
    }

    /**
     * Generates the sales graph based on the selected options and date range.
     */
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

            try (Connection conn = AdminDatabaseConnector.getConnection()) {
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

    /**
     * Updates the popular dish and wine items for the given date range.
     *
     * @param startDate The start date of the date range.
     * @param endDate   The end date of the date range.
     * @param conn      The database connection.
     * @throws SQLException If an error occurs while accessing the database.
     */
    private void updatePopularItems(LocalDate startDate, LocalDate endDate, Connection conn) throws SQLException {
        String popularDishQuery = "SELECT d.name AS dishName, COUNT(*) AS totalSold " +
                "FROM Sale_Dish sd " +
                "JOIN Dish d ON sd.dishID = d.dishID " +
                "JOIN Sale s ON sd.saleID = s.saleID " +
                "WHERE s.date BETWEEN ? AND ? " +
                "GROUP BY d.name " +
                "ORDER BY totalSold DESC " +
                "LIMIT 1";

        String popularWineQuery = "SELECT w.wineName AS wineName, COUNT(*) AS totalSold " +
                "FROM Sale_Dish sd " +
                "JOIN Dish d ON sd.dishID = d.dishID " +
                "JOIN Wine w ON d.wineID = w.wineID " +
                "JOIN Sale s ON sd.saleID = s.saleID " +
                "WHERE s.date BETWEEN ? AND ? " +
                "GROUP BY w.wineName " +
                "ORDER BY totalSold DESC " +
                "LIMIT 1";

        updatePopularItem(popularDishQuery, startDate, endDate, conn, popularDishText);
        updatePopularItem(popularWineQuery, startDate, endDate, conn, popularWineText);
    }

    /**
     * Updates the popular item text with the given SQL query and date range.
     *
     * @param query    The SQL query to retrieve the popular item.
     * @param startDate The start date of the date range.
     * @param endDate   The end date of the date range.
     * @param conn      The database connection.
     * @param itemText  The `Text` component to display the popular item name.
     * @throws SQLException If an error occurs while accessing the database.
     */
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

    /**
     * Updates the dish sales graph for the given date range.
     *
     * @param startDate The start date of the date range.
     * @param endDate   The end date of the date range.
     * @param conn      The database connection.
     * @throws SQLException If an error occurs while accessing the database.
     */
    private void updateDishSalesGraph(LocalDate startDate, LocalDate endDate, Connection conn) throws SQLException {
        String dishQuery = "SELECT DATE(s.date) AS saleDate, SUM(d.price) AS totalDishSales " +
                "FROM Sale s " +
                "JOIN Sale_Dish sd ON s.saleID = sd.saleID " +
                "JOIN Dish d ON sd.dishID = d.dishID " +
                "WHERE s.date BETWEEN ? AND ? " +
                "GROUP BY DATE(s.date)";

        updateSalesGraph(dishQuery, startDate, endDate, conn, "Dish Sales");
    }

    /**
     * Updates the wine sales graph for the given date range.
     *
     * @param startDate The start date of the date range.
     * @param endDate   The end date of the date range.
     * @param conn      The database connection.
     * @throws SQLException If an error occurs while accessing the database.
     */
    private void updateWineSalesGraph(LocalDate startDate, LocalDate endDate, Connection conn) throws SQLException {
        String wineQuery = "SELECT DATE(s.date) AS saleDate, SUM(w.winePrice) AS totalWineSales " +
                "FROM Sale s " +
                "JOIN Sale_Dish sd ON s.saleID = sd.saleID " +
                "JOIN Dish d ON sd.dishID = d.dishID " +
                "JOIN Wine w ON d.wineID = w.wineID " +
                "WHERE s.date BETWEEN ? AND ? " +
                "GROUP BY DATE(s.date)";

        updateSalesGraph(wineQuery, startDate, endDate, conn, "Wine Sales");
    }

    /**
     * Updates the total sales graph for the given date range.
     *
     * @param startDate The start date of the date range.
     * @param endDate   The end date of the date range.
     * @param conn      The database connection.
     * @throws SQLException If an error occurs while accessing the database.
     */
    private void updateTotalSalesGraph(LocalDate startDate, LocalDate endDate, Connection conn) throws SQLException {
        String totalQuery = "SELECT DATE(s.date) AS saleDate, SUM(d.price + COALESCE(w.winePrice, 0)) AS totalSales " +
                "FROM Sale s " +
                "JOIN Sale_Dish sd ON s.saleID = sd.saleID " +
                "JOIN Dish d ON sd.dishID = d.dishID " +
                "LEFT JOIN Wine w ON d.wineID = w.wineID " +
                "WHERE s.date BETWEEN ? AND ? " +
                "GROUP BY DATE(s.date)";

        updateSalesGraph(totalQuery, startDate, endDate, conn, "Total Sales");
    }

    /**
     * Updates the sales graph with the given SQL query, date range, and series name.
     *
     * @param query      The SQL query to retrieve the sales data.
     * @param startDate  The start date of the date range.
     * @param endDate    The end date of the date range.
     * @param conn       The database connection.
     * @param seriesName The name of the series to be added to the graph.
     * @throws SQLException If an error occurs while accessing the database.
     */
    private void updateSalesGraph(String query, LocalDate startDate, LocalDate endDate, Connection conn, String seriesName) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                List<Double> salesData = new ArrayList<>();
                while (rs.next()) {
                    double totalSales = rs.getDouble(2);
                    salesData.add(totalSales);
                }
                graphCreator.addSeriesToGraph(salesData, seriesName, startDate, "Total Sales");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error executing sales query: " + e.getMessage());
        }
    }

    /**
     * Creates the container for displaying the most popular dish and wine items.
     *
     * @return The `HBox` containing the popular dish and wine boxes.
     */
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

    /**
     * Creates a box for displaying a popular item (dish or wine).
     *
     * @param title        The title of the box.
     * @param itemText     The `Text` component to display the popular item name.
     * @param titleFontSize The font size for the title.
     * @param itemFontSize  The font size for the item name.
     * @return The `VBox` containing the popular item box.
     */
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

    /**
     * Shows an alert dialog with the provided title and content.
     *
     * @param title   The title of the alert dialog.
     * @param content The content to be displayed in the alert dialog.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}