package UI;

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



public class RestaurantCapacityUI extends BaseUI {
    private GridPane tableLayout;
    private Label totalTablesLabel;
    private Label availableTablesLabel;
    private Label dateTimeLabel;
    private Label occupancyPercentageLabel;

    /**
     * Constructs a new RestaurantCapacityUI instance.
     *
     * @param uiSwitcher the UISwitcher instance for navigating between UI screens
     */
    public RestaurantCapacityUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        setTopText("Restaurant Capacity Management");
        initializeUI();
    }

    /**
     * Initializes the user interface components and layout.
     */
    private void initializeUI() {
        tableLayout = new GridPane();
        tableLayout.setHgap(10);
        tableLayout.setVgap(10);
        tableLayout.setPadding(new Insets(10));

        totalTablesLabel = new Label();
        totalTablesLabel.setTextFill(Color.WHITE);
        availableTablesLabel = new Label();
        availableTablesLabel.setTextFill(Color.WHITE);

        VBox capacityBox = new VBox(10);
        capacityBox.setAlignment(Pos.CENTER_LEFT);

        Label totalTablesTitle = new Label("Total Tables:");
        totalTablesTitle.setTextFill(Color.WHITE);
        Label availableTablesTitle = new Label("Available Tables:");
        availableTablesTitle.setTextFill(Color.WHITE);

        capacityBox.getChildren().addAll(
                totalTablesTitle, totalTablesLabel,
                availableTablesTitle, availableTablesLabel
        );

        occupancyPercentageLabel = new Label();
        occupancyPercentageLabel.setTextFill(Color.WHITE);
        occupancyPercentageLabel.setStyle("-fx-font-size: 16px;");

        createTableLayout();

        // Create labels to explain the table colors
        Label occupiedLabel = new Label("Red Table: Occupied");
        occupiedLabel.setTextFill(Color.WHITE);
        occupiedLabel.setStyle("-fx-font-size: 12px;");

        Label availableLabel = new Label("White Table: Available");
        availableLabel.setTextFill(Color.WHITE);
        availableLabel.setStyle("-fx-font-size: 12px;");

        VBox legendBox = new VBox(5);
        legendBox.getChildren().addAll(occupiedLabel, availableLabel);
        legendBox.setAlignment(Pos.TOP_RIGHT);
        legendBox.setPadding(new Insets(10));

        HBox mainContent = new HBox(20);
        mainContent.setPadding(new Insets(20));

        VBox leftContent = new VBox(20);
        leftContent.getChildren().addAll(tableLayout, capacityBox, occupancyPercentageLabel);

        mainContent.getChildren().addAll(leftContent, legendBox);

        setMainContent(mainContent);
    }

    /**
     * Updates the current date and time label.
     */
//    private void updateDateTime() {
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String dateTimeString = now.format(formatter);
//        //dateTimeLabel.setText("Current Date and Time: " + dateTimeString);
//    }

    /**
     * Creates the table layout by retrieving table information from the database
     * and displaying table buttons in a grid layout and displaying waiter's name with the assigned table. Check which table is occupied or available
     * and highlights them in red or blue
     */

    private void createTableLayout() {
        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "SELECT t.tablesID, t.tablesLayout, t.status, s.staffName " +
                    "FROM Tables t " +
                    "LEFT JOIN Tables_FOHStaff tf ON t.tablesID = tf.tableID " +
                    "LEFT JOIN StaffInfo s ON tf.staffInfoID = s.staffID";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            int row = 0;
            int col = 0;
            int totalTables = 0;
            int availableTables = 0;
            int occupiedTables = 0;

            while (resultSet.next()) {
                int tableId = resultSet.getInt("tablesID");
                int capacity = resultSet.getInt("tablesLayout");
                String status = resultSet.getString("status");
                String waiterName = resultSet.getString("staffName");

                String buttonText = "Table " + tableId + " (" + capacity + ")\n";
                if (waiterName != null) {
                    buttonText += "Waiter: " + waiterName;
                } else {
                    buttonText += "No waiter assigned";
                }

                Button tableButton = new Button(buttonText);
                tableButton.setTextAlignment(TextAlignment.CENTER);
                tableButton.setWrapText(true);
                tableButton.setPrefSize(150, 80);

                if (status.equals("occupied")) {
                    occupiedTables++;
                    tableButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;"); // Set occupied table color to red
                } else {
                    availableTables++;
                    tableButton.setStyle("-fx-background-color: white; -fx-text-fill: black;"); // Set available table color to white
                }

                int tableThreshold = calculateTableThreshold();
                if (availableTables > tableThreshold) {
                    tableButton.setDisable(true);
                }

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
     * Calculates the table threshold based on the number of available waiters
     * retrieved from the StaffInfo and StaffSchedule tables in the database.
     *
     * @return the table threshold
     */
    private int calculateTableThreshold() {
        int tableThreshold = 0;
        try (Connection conn = DatabaseConnector.getConnection()) {
            LocalDate currentDate = LocalDate.now();
            String query = "SELECT COUNT(*) FROM StaffInfo si " +
                    "JOIN StaffSchedule ss ON si.staffScheduleID = ss.scheduleID " +
                    "WHERE si.staffRole = 'Waiter' AND ss.startDate <= ? AND ss.endDate >= ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setDate(1, Date.valueOf(currentDate));
            statement.setDate(2, Date.valueOf(currentDate));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int numWaiters = resultSet.getInt(1);
                tableThreshold = numWaiters * 3; // Assuming each waiter can handle 4 tables
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableThreshold;
    }
}

//    when user clicks on a table it should show what dishes+wines the table is having butttt i looked
//    at database don't think that's possible unless u make some changes so ignore this for now