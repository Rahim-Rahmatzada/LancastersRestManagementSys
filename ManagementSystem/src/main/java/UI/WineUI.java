/**
 * This class represents the user interface for managing wine data.
 * It provides functionalities to view, add, edit, and delete wine records.
 */
package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import DatabaseConnections.AdminDatabaseConnector;
import model.Wine;

import java.sql.*;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WineUI extends BaseUI {
    private TableView<Wine> wineTableView;

    /**
     * Constructor for the WineUI class.
     * @param uiSwitcher an instance of the UISwitcher class for managing UI transitions
     */
    public WineUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Wine");
        setTopText("Wine Overview");

        // Set the main content for the WineUI
        VBox wineMainContent = new VBox();
        wineMainContent.setPadding(new Insets(20));
        wineMainContent.setSpacing(10);

        // Create a TableView to display wine data
        wineTableView = createWineTableView();

        wineTableView.setStyle("-fx-background-color: #1A1A1A;");

        // Create the statistics box
        HBox statsBox = createStatsBox();


        HBox controlsBox = createControlsBox();

        wineMainContent.getChildren().addAll(wineTableView, controlsBox,statsBox);

        setMainContent(wineMainContent);
    }

    /**
     * Creates the TableView for displaying wine data.
     * @return the configured TableView object
     */
    private TableView<Wine> createWineTableView() {
        TableView<Wine> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #1A1A1A;");
        tableView.setEditable(true); // Enable editing

        // Make columns editable and handle cell editing
        TableColumn<Wine, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn()); // Make cell editable
        nameColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            wine.setName(event.getNewValue());
            updateWineInDatabase(wine); // Update database
        });

        TableColumn<Wine, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setCellFactory(TextFieldTableCell.forTableColumn()); // Make cell editable
        typeColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            wine.setType(event.getNewValue());
            updateWineInDatabase(wine);
        });

        TableColumn<Wine, Integer> vintageColumn = new TableColumn<>("Vintage");
        vintageColumn.setCellValueFactory(new PropertyValueFactory<>("vintage"));
        vintageColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter())); // Make cell editable
        vintageColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            wine.setVintage(event.getNewValue());
            updateWineInDatabase(wine); // Update database
        });

        TableColumn<Wine, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter())); // Make cell editable
        quantityColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            wine.setQuantity(event.getNewValue());
            updateWineInDatabase(wine); // Update database
        });

        TableColumn<Wine, Double> priceColumn = new TableColumn<>("Price (£)");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter())); // Make cell editable
        priceColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            wine.setPrice(event.getNewValue());
            updateWineInDatabase(wine); // Update database
        });


        nameColumn.setStyle("-fx-text-fill: white;");
        typeColumn.setStyle("-fx-text-fill: white;");
        vintageColumn.setStyle("-fx-text-fill: white;");
        quantityColumn.setStyle("-fx-text-fill: white;");
        priceColumn.setStyle("-fx-text-fill: white;");



        // Add columns to the table view
        tableView.getColumns().addAll(nameColumn, typeColumn, vintageColumn, quantityColumn, priceColumn);

        // Load wine data from the database
        ObservableList<Wine> wineData = getWineDataFromDatabase();

        // Set the data to the table view
        tableView.setItems(wineData);

        // Remove the empty column
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set the cell factory to style table cells
        tableView.setRowFactory(tv -> {
            TableRow<Wine> row = new TableRow<>();
            row.setStyle("-fx-background-color: #1A1A1A;");

            // Change the highlight color of the selected cell to red
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
     * Creates the box containing controls for adding wines.
     * @return the configured HBox object containing add wine controls
     */
    private HBox createControlsBox() {
        HBox controlsBox = new HBox();
        controlsBox.setSpacing(20);
        controlsBox.setAlignment(Pos.CENTER_LEFT);
        controlsBox.setPadding(new Insets(0, 0, 0, 50));

        controlsBox.getChildren().addAll(
                createAddWineControls()
        );

        return controlsBox;
    }

    /**
     * Creates the box containing controls for adding a new wine.
     * @return the configured HBox object containing add wine controls
     */
    private HBox createAddWineControls() {
        HBox addWineBox = new HBox();
        addWineBox.setSpacing(10);

        TextField wineNameField = new TextField();
        wineNameField.setPromptText("Enter Wine Name");

        Button addWineButton = new Button("Add Wine");
        addWineButton.setOnAction(event -> {
            String wineName = wineNameField.getText().trim();
            if (!wineName.isEmpty()) {
                addNewWine(wineName);
                wineNameField.clear();
            } else {
                showAlert("Invalid Wine", "Please enter a valid wine name.");
            }
        });

        addWineBox.getChildren().addAll(wineNameField, addWineButton);

        return addWineBox;
    }

    /**
     * Adds a new wine to the database.
     * @param wineName the name of the wine to add
     */
    private void addNewWine(String wineName) {
        try (Connection conn = AdminDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Wine (wineID, wineName, wineType, wineVintage, wineQuantity, winePrice) " +
                             "VALUES (?, ?, ?, ?, ?, ?)")) {

            int wineID;
            do {
                wineID = generateRandomWineID();
            } while (isWineIDExists(wineID));

            stmt.setInt(1, wineID);
            stmt.setString(2, wineName);
            stmt.setNull(3, Types.VARCHAR);
            stmt.setNull(4, Types.INTEGER);
            stmt.setNull(5, Types.INTEGER);
            stmt.setNull(6, Types.DOUBLE);

            stmt.executeUpdate();

            // Refresh the table view
            reloadWineTableView();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add new wine.");
        }
    }

    /**
     * Generates a random wine ID.
     * @return a randomly generated wine ID
     */
    private int generateRandomWineID() {
        return (int) (Math.random() * 900) + 100;
    }

    /**
     * Checks if a wine ID already exists in the database.
     * @param wineID the wine ID to check
     * @return true if the wine ID exists, false otherwise
     */
    private boolean isWineIDExists(int wineID) {
        try (Connection conn = AdminDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM Wine WHERE wineID = ?")) {

            stmt.setInt(1, wineID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves wine data from the database.
     * @return an ObservableList containing wine data
     */
    private ObservableList<Wine> getWineDataFromDatabase() {
        ObservableList<Wine> wineList = FXCollections.observableArrayList();

        try (Connection conn = AdminDatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Wine")) {

            while (rs.next()) {
                int wineID = rs.getInt("wineID");
                String name = rs.getString("wineName");
                String type = rs.getString("wineType");
                int vintage = rs.getInt("wineVintage");
                int quantity = rs.getInt("wineQuantity");
                double price = rs.getDouble("winePrice");

                Wine wine = new Wine(wineID, name, type, vintage, quantity, price);
                wineList.add(wine);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wineList;
    }

    /**
     * Reloads the wine table view with updated data from the database.
     */
    private void reloadWineTableView() {
        // Reload the data in the table view
        ObservableList<Wine> wineData = getWineDataFromDatabase();
        wineTableView.setItems(wineData);
    }

    /**
     * Displays an error alert with the given title and content.
     * @param title the title of the alert
     * @param content the content of the alert
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Updates the specified wine in the database.
     * @param wine the wine object to update
     */
    private void updateWineInDatabase(Wine wine) {
        try (Connection conn = AdminDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE Wine SET wineName = ?, wineType = ?, wineVintage = ?, wineQuantity = ?, winePrice = ? WHERE wineID = ?")) {

            stmt.setString(1, wine.getName());
            stmt.setString(2, wine.getType());
            stmt.setInt(3, wine.getVintage());
            stmt.setInt(4, wine.getQuantity());
            stmt.setDouble(5, wine.getPrice());
            stmt.setInt(6, wine.getWineID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update wine.");
        }
    }

    /**
     * Creates the box containing statistics about wines.
     * @return the configured HBox object containing wine statistics
     */
    private HBox createStatsBox() {
        HBox statsBox = new HBox();
        statsBox.setSpacing(50);
        statsBox.setAlignment(Pos.CENTER);

        statsBox.getChildren().addAll(
                createMostExpensiveWineBox(),
                createMostPopularWineBox(),
                createHighestQuantityWineBox()
        );

        return statsBox;
    }

    /**
     * Creates a VBox containing information about the most expensive wine.
     * @return the configured VBox object containing most expensive wine information
     */
    private VBox createMostExpensiveWineBox() {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 10;");
        box.setPadding(new Insets(10));
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);

        Text titleText = new Text("Most Expensive Wine");
        titleText.setFont(Font.font(16));
        titleText.setFill(Color.BLACK);

        Text mostExpensiveText = new Text(getMostExpensiveWine());
        mostExpensiveText.setFont(Font.font(12));
        mostExpensiveText.setFill(Color.BLACK);
        mostExpensiveText.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(titleText, mostExpensiveText);

        box.setMinWidth(250);
        box.setMaxWidth(250);

        return box;
    }

    /**
     * Creates a VBox containing information about the most popular wine.
     * @return the configured VBox object containing most popular wine information
     */
    private VBox createMostPopularWineBox() {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 10;");
        box.setPadding(new Insets(10));
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);

        Text titleText = new Text("Most Popular Wine");
        titleText.setFont(Font.font(16));
        titleText.setFill(Color.BLACK);

        Text mostPopularText = new Text(getMostPopularWine());
        mostPopularText.setFont(Font.font(12));
        mostPopularText.setFill(Color.BLACK);
        mostPopularText.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(titleText, mostPopularText);

        box.setMinWidth(250);
        box.setMaxWidth(250);

        return box;
    }

    /**
     * Creates a VBox containing information about the wine with the highest quantity.
     * @return the configured VBox object containing highest quantity wine information
     */
    private VBox createHighestQuantityWineBox() {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 10;");
        box.setPadding(new Insets(10));
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);

        Text titleText = new Text("Wine With Highest Quantity");
        titleText.setFont(Font.font(16));
        titleText.setFill(Color.BLACK);

        Text highestQuantityText = new Text(getWineWithHighestQuantity());
        highestQuantityText.setFont(Font.font(12));
        highestQuantityText.setFill(Color.BLACK);
        highestQuantityText.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(titleText, highestQuantityText);

        box.setMinWidth(250);
        box.setMaxWidth(250);

        return box;
    }

    /**
     * Retrieves information about the most expensive wine from the database.
     * @return a string containing information about the most expensive wine
     */
    private String getMostExpensiveWine() {
        String mostExpensiveWine = "";

        try (Connection conn = AdminDatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT wineName, winePrice FROM Wine ORDER BY winePrice DESC LIMIT 1")) {

            if (rs.next()) {
                mostExpensiveWine = rs.getString("wineName") + ": £" + rs.getDouble("winePrice");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mostExpensiveWine;
    }

    /**
     * Retrieves information about the most popular wine from the database.
     * @return a string containing information about the most popular wine
     */
    private String getMostPopularWine() {
        String mostPopularWine = "";

        try (Connection conn = AdminDatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT w.wineName, COUNT(sd.dishID) AS totalSales " +
                             "FROM Wine w " +
                             "JOIN Dish d ON w.wineID = d.wineID " +
                             "JOIN Menu_Dish md ON d.dishID = md.dishID " +
                             "JOIN Sale_Dish sd ON d.dishID = sd.dishID " +
                             "GROUP BY w.wineName " +
                             "ORDER BY totalSales DESC " +
                             "LIMIT 1")) {

            if (rs.next()) {
                mostPopularWine = rs.getString("wineName") + ": " + rs.getInt("totalSales") + " sales";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mostPopularWine;
    }

    /**
     * Retrieves information about the wine with the highest quantity from the database.
     * @return a string containing information about the wine with the highest quantity
     */
    private String getWineWithHighestQuantity() {
        String wineWithHighestQuantity = "";

        try (Connection conn = AdminDatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT wineName, wineQuantity FROM Wine ORDER BY wineQuantity DESC LIMIT 1")) {

            if (rs.next()) {
                wineWithHighestQuantity = rs.getString("wineName") + ": " + rs.getInt("wineQuantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wineWithHighestQuantity;
    }


}
