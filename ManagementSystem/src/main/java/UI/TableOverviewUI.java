package UI;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import model.DatabaseConnector;
import java.sql.*;
import java.time.LocalDate;



public class TableOverviewUI extends BaseUI {
    private GridPane tableLayout;
    private Label totalTablesLabel;
    private Label availableTablesLabel;
    private Label occupancyPercentageLabel;
    private DatePicker datePicker;
    private VBox tableDetailsBox;
    private Label tableDetailsLabel;
    private Button backButton;
    private VBox capacityBox;
    private VBox dateBox;

    /**
     * Constructs a new TableOverview UI instance.
     *
     * @param uiSwitcher the UISwitcher instance for navigating between UI screens
     */
    public TableOverviewUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
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

       // Label totalTablesTitle = new Label("Total Tables:");
       // totalTablesTitle.setTextFill(Color.WHITE);
        Label availableTablesTitle = new Label("Available Tables:");
        availableTablesTitle.setTextFill(Color.WHITE);

        capacityBox.getChildren().addAll(
               // totalTablesTitle, totalTablesLabel,
                availableTablesTitle, availableTablesLabel
        );

        occupancyPercentageLabel = new Label();
        occupancyPercentageLabel.setTextFill(Color.WHITE);
        occupancyPercentageLabel.setStyle("-fx-font-size: 16px;");

        // Create labels to explain the table colors
        Label occupiedLabel = new Label("Blue Table: Occupied");
        occupiedLabel.setTextFill(Color.WHITE);
        occupiedLabel.setStyle("-fx-font-size: 12px;");

        Label availableLabel = new Label("White Table: Available");
        availableLabel.setTextFill(Color.WHITE);
        availableLabel.setStyle("-fx-font-size: 12px;");

        VBox legendBox = new VBox(5);
        legendBox.getChildren().addAll(occupiedLabel, availableLabel);
        legendBox.setAlignment(Pos.TOP_RIGHT);
        legendBox.setPadding(new Insets(10));

        // Create a date picker for selecting the date
        datePicker = new DatePicker();
        datePicker.setOnAction(e -> updateTableAvailability());

        // Create a label for the selected date
        Label selectedDateLabel = new Label();
        selectedDateLabel.setTextFill(Color.WHITE);
        selectedDateLabel.textProperty().bind(datePicker.valueProperty().asString());

        dateBox = new VBox(10);
        dateBox.setAlignment(Pos.CENTER_LEFT);
        dateBox.getChildren().addAll(new Label("Select Date:"), datePicker, selectedDateLabel);

        HBox mainContent = new HBox(20);
        mainContent.setPadding(new Insets(20));

        VBox leftContent = new VBox(20);
        leftContent.getChildren().addAll(tableLayout, capacityBox, occupancyPercentageLabel, dateBox);

        mainContent.getChildren().addAll(leftContent, legendBox);

        setMainContent(mainContent);
    }

    private void updateTableAvailability() {
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

    private void createTableLayout(LocalDate selectedDate) {
        tableLayout.getChildren().clear();

        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "SELECT t.tablesID, t.tablesLayout, COALESCE(b.bookingStatus, 'Available') AS bookingStatus, s.staffName " +
                    "FROM Tables t " +
                    "LEFT JOIN (" +
                    "    SELECT tablesID, MAX(bookingDate) AS maxDate " +
                    "    FROM Booking " +
                    "    WHERE bookingDate <= ? " +
                    "    GROUP BY tablesID" +
                    ") AS latest ON t.tablesID = latest.tablesID " +
                    "LEFT JOIN Booking b ON t.tablesID = b.tablesID AND b.bookingDate = latest.maxDate " +
                    "LEFT JOIN Tables_FOHStaff tf ON t.tablesID = tf.tableID " +
                    "LEFT JOIN StaffInfo s ON tf.staffInfoID = s.staffID";

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
                //int capacity = resultSet.getInt("tablesLayout");
                String bookingStatus = resultSet.getString("bookingStatus");
                String waiterName = resultSet.getString("staffName");

                //String buttonText = "Table " + tableId + " (" + capacity + ")\n";
                String buttonText = "Table " + tableId + " \n";
                if (waiterName != null) {
                    buttonText += "Waiter: " + waiterName + "\n";
                } else {
                    buttonText += "No waiter assigned \n";
                }


                Button tableButton = new Button(buttonText);
                tableButton.setTextAlignment(TextAlignment.CENTER);
                tableButton.setWrapText(true);
                tableButton.setPrefSize(150, 120);

                if (bookingStatus.equals("Confirmed")) {
                    occupiedTables++;
                    tableButton.setStyle("-fx-background-color: #4CB5F5; -fx-text-fill: white;");
                } else {
                    availableTables++;
                    tableButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                }

//                int tableThreshold = calculateTableThreshold();
//                if (availableTables > tableThreshold) {
//                    tableButton.setDisable(true);
//                }

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

    /**
     * Displays the dishes, wines, their prices, and table layout for a selected table on a specific date.
     *
     * @param tableId      the ID of the selected table
     * @param selectedDate the selected date
     */
    private void showTableDetails(int tableId, LocalDate selectedDate) {

        VBox mainContent = (VBox) getMainContent();
        mainContent.getChildren().clear();

        tableDetailsBox = new VBox();
        tableDetailsBox.setSpacing(10);
        tableDetailsBox.setPadding(new Insets(10));

        tableDetailsLabel = new Label("Table Details - Table " + tableId);
        tableDetailsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        backButton = new Button("Back");
        backButton.setOnAction(e -> showMainUI(selectedDate));

        tableDetailsBox.getChildren().addAll(tableDetailsLabel);

        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "SELECT d.name AS dishName, d.price AS dishPrice, w.name AS wineName, w.winePrice AS winePrice, " +
                    "t.tablesLayout AS tableLayout, COUNT(b.bookingID) AS numPeople " +
                    "FROM Sale s " +
                    "JOIN Sale_Dish_Wine sdw ON s.saleID = sdw.saleID " +
                    "JOIN Dish d ON sdw.dishID = d.dishID " +
                    "JOIN Wine w ON sdw.wineID = w.wineID " +
                    "JOIN Booking b ON s.saleDate = b.bookingDate AND b.tablesID = ? " +
                    "JOIN Tables t ON b.tablesID = t.tablesID " +
                    "WHERE s.saleDate = ? " +
                    "GROUP BY d.name, d.price, w.name, w.winePrice, t.tablesLayout";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, tableId);
            statement.setDate(2, Date.valueOf(selectedDate));

            ResultSet resultSet = statement.executeQuery();

            // Create a dialog box to display the dishes, wines, prices, and table layout
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Table Details");
            dialog.setHeaderText("Dishes, Wines, and Table Layout for Table " + tableId + " on " + selectedDate);

            // Create a table view to display the data
            TableView<DishWine.DishWinePrice> tableView = new TableView<>();
            TableColumn<DishWine.DishWinePrice, String> dishColumn = new TableColumn<>("Dish");
            dishColumn.setCellValueFactory(data -> data.getValue().dishNamePriceProperty());
            TableColumn<DishWine.DishWinePrice, String> wineColumn = new TableColumn<>("Wine");
            wineColumn.setCellValueFactory(data -> data.getValue().wineNamePriceProperty());
            TableColumn<DishWine.DishWinePrice, Number> totalColumn = new TableColumn<>("Total Price");
            totalColumn.setCellValueFactory(data -> data.getValue().totalPriceProperty());
            tableView.getColumns().addAll(dishColumn, wineColumn, totalColumn);


            int tableLayout = 0;
           // int numPeople = 0;

            // Populate the table view with data from the result set
            while (resultSet.next()) {
                String dishName = resultSet.getString("dishName");
                double dishPrice = resultSet.getDouble("dishPrice");
                String wineName = resultSet.getString("wineName");
                double winePrice = resultSet.getDouble("winePrice");
                tableView.getItems().add(new DishWine.DishWinePrice(dishName, dishPrice, wineName, winePrice));

                tableLayout = resultSet.getInt("tableLayout");
               // numPeople = resultSet.getInt("numPeople");
            }

//            // Create labels to display the table layout and number of people
            Label tableLayoutLabel = new Label("Table Layout: " + tableLayout);
            //Label numPeopleLabel = new Label("Number of People: " + numPeople);
//
//            // Create a vertical box to hold the table view and labels
//            VBox dialogContent = new VBox(10);
//           // dialogContent.getChildren().addAll(tableView, tableLayoutLabel, numPeopleLabel);
//            dialogContent.getChildren().addAll(tableView, tableLayoutLabel);
//
//            // Set the vertical box as the dialog content
//            dialog.getDialogPane().setContent(dialogContent);
//
//            // Add a close button to the dialog
//            ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
//            dialog.getDialogPane().getButtonTypes().add(closeButtonType);
//
//            // Show the dialog and wait for it to be closed
//            dialog.showAndWait();

            tableDetailsBox.getChildren().add(tableView);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableDetailsBox.getChildren().add(backButton);

        mainContent.getChildren().add(tableDetailsBox);
    }

    private void showMainUI(LocalDate selectedDate) {
        // Clear the main content
        VBox mainContent = (VBox) getMainContent();
        mainContent.getChildren().clear();

        // Recreate the main UI
        VBox leftContent = new VBox(20);
        leftContent.getChildren().addAll(tableLayout, capacityBox, dateBox);

        Label occupiedLabel = new Label("Blue Table: Occupied");
        occupiedLabel.setTextFill(Color.WHITE);
        occupiedLabel.setStyle("-fx-font-size: 12px;");

        Label availableLabel = new Label("White Table: Available");
        availableLabel.setTextFill(Color.WHITE);
        availableLabel.setStyle("-fx-font-size: 12px;");

        HBox legendBox = new HBox(10);
        legendBox.getChildren().addAll(occupiedLabel, availableLabel);

        VBox rightContent = new VBox(20);
        rightContent.getChildren().addAll(legendBox);

        HBox mainBox = new HBox(20);
        mainBox.getChildren().addAll(leftContent, rightContent);

        mainContent.getChildren().add(mainBox);


        // Refresh the table layout
        createTableLayout(selectedDate);
    }

//    /**
//     * Calculates the table threshold based on the number of available waiters
//     * retrieved from the StaffInfo and StaffSchedule tables in the database.
//     *
//     * @return the table threshold
//     */
//    private int calculateTableThreshold() {
//        int tableThreshold = 0;
//        try (Connection conn = DatabaseConnector.getConnection()) {
//            LocalDate currentDate = LocalDate.now();
//            String query = "SELECT COUNT(*) FROM StaffInfo si " +
//                    "JOIN StaffSchedule ss ON si.staffScheduleID = ss.scheduleID " +
//                    "WHERE si.staffRole = 'Waiter' AND ss.startDate <= ? AND ss.endDate >= ?";
//            PreparedStatement statement = conn.prepareStatement(query);
//            statement.setDate(1, Date.valueOf(currentDate));
//            statement.setDate(2, Date.valueOf(currentDate));
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                int numWaiters = resultSet.getInt(1);
//                tableThreshold = numWaiters * 4; // Assuming each waiter can handle 4 tables
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return tableThreshold;
//    }


    /**
     * Inner class representing a dish-wine pair.
     */
    private static class DishWine {
        private final SimpleStringProperty dishName;
        private final SimpleStringProperty wineName;

        /**
         * Constructs a DishWine object with the given dish name and wine name.
         *
         * @param dishName the name of the dish
         * @param wineName the name of the wine
         */
        private DishWine(String dishName, String wineName) {
            this.dishName = new SimpleStringProperty(dishName);
            this.wineName = new SimpleStringProperty(wineName);
        }

        /**
         * Inner class representing a dish-wine pair with prices.
         */
        private static class DishWinePrice {
            private final SimpleStringProperty dishNamePrice;
            private final SimpleStringProperty wineNamePrice;
            private final SimpleDoubleProperty totalPrice;

            /**
             * Constructs a DishWinePrice object with the given dish name, dish price, wine name, and wine price.
             *
             * @param dishName  the name of the dish
             * @param dishPrice the price of the dish
             * @param wineName  the name of the wine
             * @param winePrice the price of the wine
             */
            private DishWinePrice(String dishName, double dishPrice, String wineName, double winePrice) {
                this.dishNamePrice = new SimpleStringProperty(dishName + " (" + dishPrice + ")");
                this.wineNamePrice = new SimpleStringProperty(wineName + " (" + winePrice + ")");
                this.totalPrice = new SimpleDoubleProperty(dishPrice + winePrice);
            }

            /**
             * Returns the SimpleStringProperty representing the dish name and price.
             *
             * @return the SimpleStringProperty for the dish name and price
             */
            public SimpleStringProperty dishNamePriceProperty() {
                return dishNamePrice;
            }

            /**
             * Returns the SimpleStringProperty representing the wine name and price.
             *
             * @return the SimpleStringProperty for the wine name and price
             */
            public SimpleStringProperty wineNamePriceProperty() {
                return wineNamePrice;
            }

            /**
             * Returns the SimpleDoubleProperty representing the total price of the dish and wine.
             *
             * @return the SimpleDoubleProperty for the total price
             */
            public SimpleDoubleProperty totalPriceProperty() {
                return totalPrice;
            }
        }
    }
}