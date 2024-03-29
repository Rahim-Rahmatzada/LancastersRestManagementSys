package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.sql.*;
import model.DatabaseConnector;

public class RestaurantCapacityUI extends BaseUI{
    private GridPane restaurantLayout;
    private TextField maxCapacityField;
    private Label capacityUtilizationLabel;

    public RestaurantCapacityUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        setTopText("Restaurant Capacity Management");
        initializeUI();
    }

    private void initializeUI() {
        // Create a grid layout for the restaurant tables
        restaurantLayout = new GridPane();
        restaurantLayout.setHgap(10);
        restaurantLayout.setVgap(10);
        restaurantLayout.setPadding(new Insets(10));

        // Create a text field for entering the maximum capacity
        maxCapacityField = new TextField();
        maxCapacityField.setPromptText("Enter maximum capacity");

        // Create a button to update the maximum capacity
        Button updateCapacityButton = new Button("Update Capacity");
        updateCapacityButton.setOnAction(e -> updateCapacity());

        // Create a label to display the capacity utilization
        capacityUtilizationLabel = new Label();

        // Create a vertical layout box for the capacity-related components
        VBox capacityBox = new VBox(10);
        capacityBox.setAlignment(Pos.CENTER_LEFT);
        capacityBox.getChildren().addAll(new Label("Maximum Capacity:"), maxCapacityField, updateCapacityButton, capacityUtilizationLabel);

        // Populate the restaurant layout with table buttons
        createRestaurantLayout();

        // Create a vertical layout box for the main content
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.getChildren().addAll(restaurantLayout, capacityBox);

    }

    /**
     * Creates the restaurant layout by retrieving table information from the database
     * and adding table buttons to the grid layout.
     */
    private void createRestaurantLayout() {
        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "SELECT * FROM Tables";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            int row = 0;
            int col = 0;

            while (resultSet.next()) {
                int tableId = resultSet.getInt("tablesID");
                int capacity = resultSet.getInt("tablesLayout");

                // Create a button for each table with its ID and capacity
                Button tableButton = new Button("Table " + tableId + " (" + capacity + ")");
                tableButton.setPrefSize(120, 60);
                tableButton.setOnAction(e -> updateTableStatus(tableId));

                restaurantLayout.add(tableButton, col, row);

                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTableStatus(int tableId) {
        // Implement the logic to update the table status in the UI and database
        // You can use a dialog or prompt to get the new status from the user
        // Update the corresponding button style based on the new status
        // Update the table status in the database using an SQL UPDATE statement
        // Refresh the capacity utilization display
    }

    /**
     * Updates the maximum capacity based on the value entered in the text field.
     */
    private void updateCapacity() {
        int maxCapacity = Integer.parseInt(maxCapacityField.getText());
        // Update the maximum capacity in the database or store it in memory
        refreshCapacityUtilization();
    }

    /**
     * Refreshes the capacity utilization display based on the current occupancy and staff availability.
     */
    private void refreshCapacityUtilization() {
        int occupiedTables = getOccupiedTablesCount();
        int maxCapacity = getMaxCapacity();
        int tableThreshold = calculateTableThreshold();

        double utilization = (double) occupiedTables / maxCapacity * 100;

        String utilizationText = String.format("Capacity Utilization: %.2f%%", utilization);
        if (occupiedTables >= tableThreshold) {
            utilizationText += " (Threshold Reached)";
        }

        capacityUtilizationLabel.setText(utilizationText);
    }

    private int getOccupiedTablesCount() {
        int occupiedTables = 0;
        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "SELECT COUNT(*) FROM Tables WHERE status = 'occupied'"; //Think need to add this to database?
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                occupiedTables = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return occupiedTables;
    }

    private int getMaxCapacity() {
        // Retrieve the maximum capacity from the database or stored value, will need to add
        // For simplicity, let's assume it's stored in the TextField
        return Integer.parseInt(maxCapacityField.getText());
    }


    /**
     * Calculates the table threshold based on the number of available waiters.
     *
     * @return the table threshold
     */
    private int calculateTableThreshold() {
        int tableThreshold = 0;
        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "SELECT COUNT(*) FROM StaffInfo WHERE staffRole = 'Waiter'";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                int numWaiters = resultSet.getInt(1);
                tableThreshold = numWaiters * 3; // Assuming each waiter can handle 3 tables
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableThreshold;
    }
}