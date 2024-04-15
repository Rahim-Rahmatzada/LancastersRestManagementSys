package UI;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import DatabaseConnections.AdminDatabaseConnector;
import model.SoldItem;

import java.sql.*;
import java.time.LocalDate;



public class TableOverviewUI extends BaseUI {
    private GridPane tableLayout;
    private Label totalTablesLabel;
    private Label availableTablesLabel;
    private Label occupancyPercentageLabel;
    protected DatePicker datePicker;
    private VBox tableDetailsBox;
    private Label tableDetailsLabel;
    private Button backButton;
    private VBox capacityBox;
    protected VBox dateBox;

    /**
     * Constructs a new TableOverview UI instance.
     *
     * @param uiSwitcher the UISwitcher instance for navigating between UI screens
     */
    public TableOverviewUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Table Overview");
        setTopText("Table Overview");
        initializeUI();
    }

    /**
     * Initializes the user interface components and layout.
     */
    private void initializeUI() {
        tableLayout = new GridPane();
        tableLayout.setHgap(18);
        tableLayout.setVgap(18);
        tableLayout.setPadding(new Insets(20));


        totalTablesLabel = new Label();
        totalTablesLabel.setTextFill(Color.WHITE);
        availableTablesLabel = new Label();
        availableTablesLabel.setTextFill(Color.WHITE);

        capacityBox = new VBox(10);
        capacityBox.setAlignment(Pos.CENTER_LEFT);

        occupancyPercentageLabel = new Label();
        occupancyPercentageLabel.setTextFill(Color.WHITE);
        occupancyPercentageLabel.setStyle("-fx-font-size: 16px;");

        // Create a date picker for selecting the date
        datePicker = new DatePicker();
        datePicker.setPromptText("Enter Date");

        datePicker.setOnAction(e -> updateTableAvailability());

        // Create a label for the selected date
        Label selectedDateLabel = new Label();

        dateBox = new VBox(10);
        dateBox.setAlignment(Pos.CENTER_LEFT);

        Label dateLabel = new Label("Select date: ");
        dateLabel.setTextFill(Color.WHITE);

        dateBox.getChildren().addAll(dateLabel, datePicker, selectedDateLabel);
        //dateBox.getChildren().addAll(new Label("Select Date:"), datePicker, selectedDateLabel);

        //datePicker.setStyle("-fx-text-fill: white;");

        tableLayout = new GridPane();
        tableLayout.setHgap(10);
        tableLayout.setVgap(10);
        tableLayout.setPadding(new Insets(10));

        createTableLayout(LocalDate.now());

        VBox extraInfoBox = new VBox(10);
        extraInfoBox.setAlignment(Pos.TOP_LEFT);
        extraInfoBox.setPadding(new Insets(10));

        Label occupiedLabel = new Label("Blue Table: Occupied");
        occupiedLabel.setTextFill(Color.WHITE);
        occupiedLabel.setStyle("-fx-font-size: 12px;");

        Label availableLabel = new Label("White Table: Available");
        availableLabel.setTextFill(Color.WHITE);
        availableLabel.setStyle("-fx-font-size: 12px;");

        Label availableTablesTitle = new Label("Available Tables:");
        availableTablesTitle.setTextFill(Color.WHITE);

        availableTablesLabel = new Label();
        availableTablesLabel.setTextFill(Color.WHITE);

        occupancyPercentageLabel = new Label();
        occupancyPercentageLabel.setTextFill(Color.WHITE);
        occupancyPercentageLabel.setStyle("-fx-font-size: 16px;");

        extraInfoBox.getChildren().addAll(
                occupiedLabel,
                availableLabel,
                availableTablesTitle,
                availableTablesLabel,
                occupancyPercentageLabel
        );

        HBox mainContent = new HBox(20);
        mainContent.setPadding(new Insets(20));

        VBox leftContent = new VBox(20);
        // leftContent.getChildren().addAll(tableLayout, capacityBox, occupancyPercentageLabel, dateBox);
        leftContent.getChildren().addAll(dateBox, tableLayout);


        mainContent.getChildren().addAll(leftContent, extraInfoBox);

        setMainContent(mainContent);
    }

    void updateTableAvailability() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            createTableLayout(selectedDate);
        }
    }

    /**
     * Creates the table layout by retrieving table information from the database
     * and displaying table buttons in a grid layout and displaying waiter's name with the assigned table. Check which table is occupied or available
     * and highlights them in red or blue
     */

    public void createTableLayout(LocalDate selectedDate) {
        tableLayout.getChildren().clear();

        try (Connection conn = AdminDatabaseConnector.getConnection()) {
            String query = "SELECT t.tablesID, t.tablesLayout, COALESCE(b.bookingStatus, 'Available') AS bookingStatus, " +
                    "GROUP_CONCAT(s.staffName SEPARATOR ', ') AS waiterNames " +
                    "FROM Tables t " +
                    "LEFT JOIN (" +
                    "    SELECT tablesID, MAX(bookingDate) AS maxDate " +
                    "    FROM Booking " +
                    "    WHERE bookingDate <= ? " +
                    "    GROUP BY tablesID" +
                    ") AS latest ON t.tablesID = latest.tablesID " +
                    "LEFT JOIN Booking b ON t.tablesID = b.tablesID AND b.bookingDate = latest.maxDate " +
                    "LEFT JOIN Tables_FOHStaff tf ON t.tablesID = tf.tableID " +
                    "LEFT JOIN StaffInfo s ON tf.staffInfoID = s.staffID " +
                    "GROUP BY t.tablesID";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setDate(1, Date.valueOf(selectedDate));

            ResultSet resultSet = statement.executeQuery();

            int row = 0;
            int col = 0;
            int totalTables = 0;
            int availableTables = 0;
            int occupiedTables = 0;

            while (resultSet.next()) {
                int tableId = resultSet.getInt("tablesID");
                String bookingStatus = resultSet.getString("bookingStatus");
                String waiterNames = resultSet.getString("waiterNames");

                String buttonText = "Table " + tableId + " (Layout: " + resultSet.getInt("tablesLayout") + ")\n";
                if (waiterNames != null) {
                    buttonText += "Waiters: " + waiterNames + "\n";
                } else {
                    buttonText += "No waiter assigned \n";
                }

                Button tableButton = new Button(buttonText);
                tableButton.setTextAlignment(TextAlignment.CENTER);
                tableButton.setWrapText(true);
                tableButton.setPrefSize(150, 120);
                tableButton.setStyle("-fx-background-color: #1A1A1A; -fx-text-fill: white;");

                // Add mouse event handlers to the table button
                tableButton.setOnMouseEntered(event -> {
                    tableButton.setStyle("-fx-background-color: #d3d3d3; -fx-text-fill: black;");
                });

                tableButton.setOnMouseExited(event -> {
                    if (bookingStatus.equals("Confirmed")) {
                        tableButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
                    } else {
                        tableButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                    }
                });

                if (bookingStatus.equals("Confirmed")) {
                    occupiedTables++;
                    tableButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
                } else {
                    availableTables++;
                    tableButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                }

                tableButton.setOnAction(e -> showTableDetails(tableId, selectedDate));

                tableLayout.add(tableButton, col, row);

                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }

                totalTables++;
            }

            totalTablesLabel.setText(String.valueOf(totalTables));
            availableTablesLabel.setText(String.valueOf(availableTables));

            double occupancyPercentage = (double) occupiedTables / totalTables * 100;
            String occupancyPercentageText = String.format("Occupancy: %.2f%%", occupancyPercentage);
            occupancyPercentageLabel.setText(occupancyPercentageText);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showTableDetails(int tableId, LocalDate selectedDate) {
        VBox mainContent = getMainContent();
        mainContent.getChildren().clear();

        tableDetailsBox = new VBox();
        tableDetailsBox.setSpacing(10);
        tableDetailsBox.setPadding(new Insets(10));

        tableDetailsLabel = new Label("Table Details - Table " + tableId);
        tableDetailsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        backButton = new Button("Back");
        backButton.setOnAction(e -> showMainUI(selectedDate));

        tableDetailsBox.getChildren().addAll(tableDetailsLabel);

        try (Connection conn = AdminDatabaseConnector.getConnection()) {
            // Retrieve dish details
            String dishQuery = "SELECT d.name AS dishName, d.price AS dishPrice, COUNT(sd.dishID) AS dishQuantity " +
                    "FROM Booking b " +
                    "JOIN Tables t ON b.tablesID = t.tablesID " +
                    "JOIN Sale_Table st ON t.tablesID = st.tablesID " +
                    "JOIN Sale s ON st.saleID = s.saleID AND b.bookingDate = s.date " +
                    "JOIN Sale_Dish sd ON s.saleID = sd.saleID " +
                    "JOIN Dish d ON sd.dishID = d.dishID " +
                    "WHERE b.tablesID = ? AND b.bookingDate = ? " +
                    "GROUP BY d.name, d.price";

            PreparedStatement dishStatement = conn.prepareStatement(dishQuery);
            dishStatement.setInt(1, tableId);
            dishStatement.setDate(2, Date.valueOf(selectedDate));

            ResultSet dishResultSet = dishStatement.executeQuery();

            // Create a table view for dishes
            TableView<DishDetails> dishTableView = new TableView<>();
            dishTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            dishTableView.setStyle("-fx-background-color: #1A1A1A;");

            TableColumn<DishDetails, String> dishNameColumn = new TableColumn<>("Dish Name");
            dishNameColumn.setCellValueFactory(data -> data.getValue().dishNameProperty());
            dishNameColumn.setStyle("-fx-text-fill: white;");

            TableColumn<DishDetails, Number> dishPriceColumn = new TableColumn<>("Price");
            dishPriceColumn.setCellValueFactory(data -> data.getValue().dishPriceProperty());
            dishPriceColumn.setStyle("-fx-text-fill: white;");

            TableColumn<DishDetails, Number> dishQuantityColumn = new TableColumn<>("Quantity Sold");
            dishQuantityColumn.setCellValueFactory(data -> data.getValue().dishQuantityProperty());
            dishQuantityColumn.setStyle("-fx-text-fill: white;");

            TableColumn<DishDetails, Number> dishTotalSaleColumn = new TableColumn<>("Total Sale For Item (£)");
            dishTotalSaleColumn.setCellValueFactory(cellData -> {
                DishDetails dishDetails = cellData.getValue();
                double totalSale = dishDetails.getDishPrice() * dishDetails.getDishQuantity();
                return new SimpleDoubleProperty(totalSale);
            });
            dishTotalSaleColumn.setStyle("-fx-text-fill: white;");

            dishTableView.getColumns().addAll(dishNameColumn, dishPriceColumn, dishQuantityColumn, dishTotalSaleColumn);

            dishTableView.setRowFactory(tv -> {
                TableRow<DishDetails> row = new TableRow<>();
                row.setStyle("-fx-background-color: #1A1A1A;");

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

            // Populate the dish table view
            while (dishResultSet.next()) {
                String dishName = dishResultSet.getString("dishName");
                double dishPrice = dishResultSet.getDouble("dishPrice");
                int dishQuantity = dishResultSet.getInt("dishQuantity");

                DishDetails dishDetails = new DishDetails(dishName, dishPrice, dishQuantity);
                dishTableView.getItems().add(dishDetails);
            }

            // Retrieve wine details
            String wineQuery = "SELECT w.wineName AS wineName, w.winePrice AS winePrice, COUNT(d.dishID) AS wineQuantity " +
                    "FROM Booking b " +
                    "JOIN Tables t ON b.tablesID = t.tablesID " +
                    "JOIN Sale_Table st ON t.tablesID = st.tablesID " +
                    "JOIN Sale s ON st.saleID = s.saleID AND b.bookingDate = s.date " +
                    "JOIN Sale_Dish sd ON s.saleID = sd.saleID " +
                    "JOIN Dish d ON sd.dishID = d.dishID " +
                    "JOIN Wine w ON d.wineID = w.wineID " +
                    "WHERE b.tablesID = ? AND b.bookingDate = ? " +
                    "GROUP BY w.wineName, w.winePrice";

            PreparedStatement wineStatement = conn.prepareStatement(wineQuery);
            wineStatement.setInt(1, tableId);
            wineStatement.setDate(2, Date.valueOf(selectedDate));

            ResultSet wineResultSet = wineStatement.executeQuery();

            // Create a table view for wines
            TableView<WineDetails> wineTableView = new TableView<>();
            wineTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            wineTableView.setStyle("-fx-background-color: #1A1A1A;");

            TableColumn<WineDetails, String> wineNameColumn = new TableColumn<>("Wine Name");
            wineNameColumn.setCellValueFactory(data -> data.getValue().wineNameProperty());
            wineNameColumn.setStyle("-fx-text-fill: white;");

            TableColumn<WineDetails, Number> winePriceColumn = new TableColumn<>("Price");
            winePriceColumn.setCellValueFactory(data -> data.getValue().winePriceProperty());
            winePriceColumn.setStyle("-fx-text-fill: white;");

            TableColumn<WineDetails, Number> wineQuantityColumn = new TableColumn<>("Quantity Sold");
            wineQuantityColumn.setCellValueFactory(data -> data.getValue().wineQuantityProperty());
            wineQuantityColumn.setStyle("-fx-text-fill: white;");

            TableColumn<WineDetails, Number> wineTotalSaleColumn = new TableColumn<>("Total Sale For Item (£)");
            wineTotalSaleColumn.setCellValueFactory(cellData -> {
                WineDetails wineDetails = cellData.getValue();
                double totalSale = wineDetails.getWinePrice() * wineDetails.getWineQuantity();
                return new SimpleDoubleProperty(totalSale);
            });
            wineTotalSaleColumn.setStyle("-fx-text-fill: white;");



            wineTableView.getColumns().addAll(wineNameColumn, winePriceColumn, wineQuantityColumn, wineTotalSaleColumn);

            wineTableView.setRowFactory(tv -> {
                TableRow<WineDetails> row = new TableRow<>();
                row.setStyle("-fx-background-color: #1A1A1A;");

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

            // Populate the wine table view
            while (wineResultSet.next()) {
                String wineName = wineResultSet.getString("wineName");
                double winePrice = wineResultSet.getDouble("winePrice");
                int wineQuantity = wineResultSet.getInt("wineQuantity");

                WineDetails wineDetails = new WineDetails(wineName, winePrice, wineQuantity);
                wineTableView.getItems().add(wineDetails);
            }

            tableDetailsBox.getChildren().addAll(dishTableView, wineTableView);

            // Calculate and display total sales for the table
            double totalSales = dishTableView.getItems().stream()
                    .mapToDouble(item -> item.getDishPrice() * item.getDishQuantity())
                    .sum() +
                    wineTableView.getItems().stream()
                            .mapToDouble(item -> item.getWinePrice() * item.getWineQuantity())
                            .sum();

            Label totalSalesLabel = new Label("Total Sales for Table " + tableId + ": £" + String.format("%.2f", totalSales));
            totalSalesLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
            tableDetailsBox.getChildren().add(totalSalesLabel);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableDetailsBox.getChildren().add(backButton);
        mainContent.getChildren().add(tableDetailsBox);
    }

    // Inner classes for dish and wine details
    private static class DishDetails {
        private final SimpleStringProperty dishName;
        private final SimpleDoubleProperty dishPrice;
        private final SimpleIntegerProperty dishQuantity;

        public DishDetails(String dishName, double dishPrice, int dishQuantity) {
            this.dishName = new SimpleStringProperty(dishName);
            this.dishPrice = new SimpleDoubleProperty(dishPrice);
            this.dishQuantity = new SimpleIntegerProperty(dishQuantity);
        }

        public String getDishName() {
            return dishName.get();
        }

        public SimpleStringProperty dishNameProperty() {
            return dishName;
        }

        public double getDishPrice() {
            return dishPrice.get();
        }

        public SimpleDoubleProperty dishPriceProperty() {
            return dishPrice;
        }

        public int getDishQuantity() {
            return dishQuantity.get();
        }

        public SimpleIntegerProperty dishQuantityProperty() {
            return dishQuantity;
        }
    }

    private static class WineDetails {
        private final SimpleStringProperty wineName;
        private final SimpleDoubleProperty winePrice;
        private final SimpleIntegerProperty wineQuantity;

        public WineDetails(String wineName, double winePrice, int wineQuantity) {
            this.wineName = new SimpleStringProperty(wineName);
            this.winePrice = new SimpleDoubleProperty(winePrice);
            this.wineQuantity = new SimpleIntegerProperty(wineQuantity);
        }

        public String getWineName() {
            return wineName.get();
        }

        public SimpleStringProperty wineNameProperty() {
            return wineName;
        }

        public double getWinePrice() {
            return winePrice.get();
        }

        public SimpleDoubleProperty winePriceProperty() {
            return winePrice;
        }

        public int getWineQuantity() {
            return wineQuantity.get();
        }

        public SimpleIntegerProperty wineQuantityProperty() {
            return wineQuantity;
        }
    }


    private void showMainUI(LocalDate selectedDate) {
        // Clear the main content
        VBox mainContent = getMainContent();
        mainContent.getChildren().clear();

        // Recreate the table layout
        createTableLayout(selectedDate);

        // Create the extra info box
        VBox extraInfoBox = new VBox(10);
        extraInfoBox.setAlignment(Pos.TOP_LEFT);
        extraInfoBox.setPadding(new Insets(10));

        Label occupiedLabel = new Label("Blue Table: Occupied");
        occupiedLabel.setTextFill(Color.WHITE);
        occupiedLabel.setStyle("-fx-font-size: 12px;");

        Label availableLabel = new Label("White Table: Available");
        availableLabel.setTextFill(Color.WHITE);
        availableLabel.setStyle("-fx-font-size: 12px;");

        Label availableTablesTitle = new Label("Available Tables:");
        availableTablesTitle.setTextFill(Color.WHITE);

        extraInfoBox.getChildren().addAll(
                occupiedLabel,
                availableLabel,
                availableTablesTitle,
                availableTablesLabel,
                occupancyPercentageLabel
        );

        // Create the main content layout
        HBox mainContentLayout = new HBox(20);
        mainContentLayout.setPadding(new Insets(20));

        VBox leftContent = new VBox(20);
        leftContent.getChildren().addAll(dateBox, tableLayout);

        mainContentLayout.getChildren().addAll(leftContent, extraInfoBox);

        mainContent.getChildren().add(mainContentLayout);
    }

}