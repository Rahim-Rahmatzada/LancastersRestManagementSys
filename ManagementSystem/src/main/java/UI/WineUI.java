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

        // Set the main content for the WineUI.
        VBox wineMainContent = new VBox();
        wineMainContent.setPadding(new Insets(20));
        wineMainContent.setSpacing(10);

        HBox controlsBox = createControlsBox();

        // Create a TableView to display wine data
        wineTableView = createWineTableView();
        wineMainContent.getChildren().addAll(wineTableView, controlsBox);

        setMainContent(wineMainContent);

    }

    private TableView<Wine> createWineTableView() {
        TableView<Wine> tableView = new TableView<>();

        tableView.setStyle("-fx-background-color: #1A1A1A;");

        // Create table columns

        TableColumn<Wine, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        idColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            String newValue = event.getNewValue();
            updateWineValue(wine, "ID", newValue);
        });


        TableColumn<Wine, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            String newValue = event.getNewValue();
            updateWineValue(wine, "name", newValue);
        });

        TableColumn<Wine, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        typeColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            String newValue = event.getNewValue();
            updateWineValue(wine, "type", newValue);
        });

        TableColumn<Wine, Integer> vintageColumn = new TableColumn<>("Vintage");
        vintageColumn.setCellValueFactory(new PropertyValueFactory<>("vintage"));
        vintageColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        vintageColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            int newValue = event.getNewValue();
            updateWineValue(wine, "vintage", newValue);
        });

        TableColumn<Wine, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            int newValue = event.getNewValue();
            updateWineValue(wine, "quantity", newValue);
        });

        TableColumn<Wine, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            double newValue = event.getNewValue();
            updateWineValue(wine, "price", newValue);
        });


        // Set the style for table columns
        nameColumn.setStyle("-fx-text-fill: white;");
        typeColumn.setStyle("-fx-text-fill: white;");
        vintageColumn.setStyle("-fx-text-fill: white;");
        quantityColumn.setStyle("-fx-text-fill: white;");
        priceColumn.setStyle("-fx-text-fill: white;");
        idColumn.setStyle("-fx-text-fill: white;");



        // Add columns to the table view
        tableView.getColumns().addAll(idColumn, nameColumn, typeColumn, vintageColumn, quantityColumn, priceColumn); // Add other columns as needed

        // Load wine data from the database
        ObservableList<Wine> wineData = getWineDataFromDatabase();

        // Set the data to the table view
        tableView.setItems(wineData);



        nameColumn.setCellFactory(column -> new TableCell<Wine, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                }
            }
        });

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

        // Set the empty table message
        tableView.setPlaceholder(new Label("No wine found."));

        // Remove the empty column
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Enable cell selection and editing
        tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
        tableView.setEditable(true);

        return tableView;
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

    private void deleteWine(String name) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM Wine WHERE name = ?")) {

            stmt.setString(1, name);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Refresh the table view
                reloadWineTableView();
            } else {
                showAlert("Wine Not Found", "No wine named: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete wine.");
        }
    }

    private HBox createControlsBox() {
        HBox controlsBox = new HBox();
        controlsBox.setSpacing(20);
        controlsBox.setAlignment(Pos.CENTER_LEFT);

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

    private void addNewWine(String name) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Wine (wineID, name, type, vintage, quantity, winePrice) " +
                             "VALUES (?, ?, ?, ?, ?, ?)")) {

            int newID = getMaxID() + 1;

            stmt.setInt(1, newID);
            stmt.setString(2, name);
            stmt.setString(3, "Red"); // Set initial type to Red
            stmt.setInt(4, 1900); // Set initial vinatge to 1900
            stmt.setInt(5, 0); // Set initial quantity to 0
            stmt.setDouble(6, 0.0); // Set initial prize to 0


            stmt.executeUpdate();

            // Refresh the table view
            reloadWineTableView();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add new wine.");
        }
    }

    private int getMaxID() {
        int maxID = 0;
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(wineID) FROM Wine")) {

            if (rs.next()) {
                maxID = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxID;
    }

    private void updateWineValue(Wine wine, String column, Object newValue) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE Wine SET " + column + " = ? WHERE wineID = ?")) {

            if (newValue instanceof String) {
                stmt.setString(1, (String) newValue);
            } else if (newValue instanceof Integer) {
                stmt.setInt(1, (Integer) newValue);
            }

            stmt.setInt(2, wine.getWineID());
            stmt.executeUpdate();

            // Refresh the table view
            reloadWineTableView();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update wine value.");
        } catch (Exception e) {
            showAlert("Error", "Invalid value entered.");
        }
    }

    private ObservableList<Wine> getWineDataFromDatabase() {
        ObservableList<Wine> wineList = FXCollections.observableArrayList();

        // Query to retrieve wine data from the database
        String query = "SELECT wineID, name, type, vintage, quantity, winePrice FROM Wine";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("wineID");
                String name = rs.getString("name");
                String type = rs.getString("type");
                int vintage = rs.getInt("vintage");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("winePrice");


                // Create Wine object and add to the list
                Wine wine = new Wine(id, name, type, vintage, quantity, price);
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
