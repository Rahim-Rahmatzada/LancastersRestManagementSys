package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.DatabaseConnector;
import java.sql.*;
import java.time.LocalDate;


public class RestaurantCapacityUI extends BaseUI {
    private GridPane tableLayout;
    private Label totalTablesLabel;
    private Label availableTablesLabel;

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
        availableTablesLabel = new Label();

        VBox capacityBox = new VBox(10);
        capacityBox.setAlignment(Pos.CENTER_LEFT);
        capacityBox.getChildren().addAll(
                new Label("Total Tables:"), totalTablesLabel,
                new Label("Available Tables:"), availableTablesLabel
        );

        createTableLayout();

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.getChildren().addAll(tableLayout, capacityBox);

        setMainContent(mainContent);
    }

    /**
     * Creates the table layout by retrieving table information from the database
     * and displaying table buttons in a grid layout.
     */

    private void createTableLayout() {
        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "SELECT tablesID, tablesLayout FROM Tables";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            int row = 0;
            int col = 0;
            int totalTables = 0;
            int availableTables = 0;

            while (resultSet.next()) {
                int tableId = resultSet.getInt("tablesID");
                int capacity = resultSet.getInt("tablesLayout");

                Button tableButton = new Button("Table " + tableId + " (" + capacity + ")");
                tableButton.setPrefSize(120, 60);


                int tableThreshold = calculateTableThreshold();
                if (availableTables >= tableThreshold) {
                    tableButton.setDisable(true);
                } else {
                    availableTables++;
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