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
import model.DatabaseConnector;
import model.Wine;

import java.sql.*;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WineUI extends BaseUI {
    private TableView<Wine> wineTableView;

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


        HBox controlsBox = createControlsBox();

        wineMainContent.getChildren().addAll(wineTableView, controlsBox);

        setMainContent(wineMainContent);
    }

    private TableView<Wine> createWineTableView() {
        TableView<Wine> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #1A1A1A;");

        // Create table columns
        TableColumn<Wine, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setStyle("-fx-text-fill: white;");

        TableColumn<Wine, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setStyle("-fx-text-fill: white;");

        TableColumn<Wine, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setStyle("-fx-text-fill: white;");

        TableColumn<Wine, Integer> vintageColumn = new TableColumn<>("Vintage");
        vintageColumn.setCellValueFactory(new PropertyValueFactory<>("vintage"));
        vintageColumn.setStyle("-fx-text-fill: white;");

        TableColumn<Wine, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setStyle("-fx-text-fill: white;");

        // Add columns to the table view
        tableView.getColumns().addAll(nameColumn, typeColumn, quantityColumn, vintageColumn, priceColumn);

        // Load wine data from the database
        ObservableList<Wine> wineData = getWineDataFromDatabase();

        // Set the data to the table view
        tableView.setItems(wineData);

        // Remove the empty column
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        row.setStyle("-fx-background-color: #1A1A1A;");

        return tableView;
    }

    private HBox createControlsBox() {
        HBox controlsBox = new HBox();
        controlsBox.setSpacing(20);
        controlsBox.setAlignment(Pos.CENTER_LEFT);
        controlsBox.setPadding(new Insets(0, 0, 0, 200));

        controlsBox.getChildren().addAll(
                createAddWineControls(),
                createDeleteWineControls()
        );

        return controlsBox;
    }

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

    private void addNewWine(String wineName) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Wine (wineID, wineName, wineType, wineQuantity, wineVintage, winePrice) " +
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

    private int generateRandomWineID() {
        return (int) (Math.random() * 900) + 100;
    }

    private boolean isWineIDExists(int wineID) {
        try (Connection conn = DatabaseConnector.getConnection();
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

    private HBox createDeleteWineControls() {
        HBox deleteWineBox = new HBox();
        deleteWineBox.setSpacing(10);

        TextField wineNameField = new TextField();
        wineNameField.setPromptText("Enter Wine Name");

        Button deleteWineButton = new Button("Delete Wine");
        deleteWineButton.setOnAction(event -> {
            String wineName = wineNameField.getText().trim();
            if (!wineName.isEmpty()) {
                deleteWine(wineName);
                wineNameField.clear();
            } else {
                showAlert("Invalid Wine", "Please enter a valid wine name.");
            }
        });

        deleteWineBox.getChildren().addAll(wineNameField, deleteWineButton);

        return deleteWineBox;
    }

    private void deleteWine(String wineName) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM Wine WHERE wineName = ?")) {

            stmt.setString(1, wineName);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Refresh the table view
                reloadWineTableView();
            } else {
                showAlert("Wine Not Found", "No wine named: " + wineName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete wine.");
        }
    }

    private ObservableList<Wine> getWineDataFromDatabase() {
        ObservableList<Wine> wineList = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Wine")) {

            while (rs.next()) {
                int wineID = rs.getInt("wineID");
                String name = rs.getString("wineName");
                String type = rs.getString("wineType");
                int quantity = rs.getInt("wineQuantity");
                int vintage = rs.getInt("wineVintage");
                double price = rs.getDouble("winePrice");

                Wine wine = new Wine(wineID, name, type, quantity, vintage, price);
                wineList.add(wine);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wineList;
    }

    private void reloadWineTableView() {
        // Reload the data in the table view
        ObservableList<Wine> wineData = getWineDataFromDatabase();
        wineTableView.setItems(wineData);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}