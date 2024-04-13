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

    private Text topWineText;
    private Text mostExpensiveWinesText;
    private Text highestQuantityWineText;
    private TableView<Wine> wineTableView;

    public WineUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Wine");
        setTopText("Wine Overview");

        // Set the main content for the WineUI.
        VBox wineMainContent = new VBox();
        wineMainContent.setPadding(new Insets(20));
        wineMainContent.setSpacing(10);

        wineTableView = createWineTableView();

        VBox topWinesBox = createTopWinesBox();
        VBox mostExpensiveWinesBox = createMostExpensiveWinesBox();
        VBox highestQuantityWinesBox = createHighestQuantityWinesBox();

        HBox insightsBox = new HBox();
        insightsBox.setSpacing(50);
        insightsBox.setAlignment(Pos.CENTER);
        insightsBox.getChildren().addAll(topWinesBox, mostExpensiveWinesBox, highestQuantityWinesBox);

        HBox controlsBox = createControlsBox();

        // Create a TableView to display wine data

        wineMainContent.getChildren().addAll(wineTableView, controlsBox, insightsBox);

        setMainContent(wineMainContent);

        showTopWines();
        showMostExpensiveWines();
        showHighestQuantityWines();

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
            updateWineValue(wine, "wineID", newValue);
        });


        TableColumn<Wine, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            String newValue = event.getNewValue();
            updateWineValue(wine, "wineName", newValue);
        });

        TableColumn<Wine, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        typeColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            String newValue = event.getNewValue();
            updateWineValue(wine, "wineType", newValue);
        });

        TableColumn<Wine, Integer> vintageColumn = new TableColumn<>("Vintage");
        vintageColumn.setCellValueFactory(new PropertyValueFactory<>("vintage"));
        vintageColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        vintageColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            int newValue = event.getNewValue();
            updateWineValue(wine, "wineVintage", newValue);
        });

        TableColumn<Wine, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            int newValue = event.getNewValue();
            updateWineValue(wine, "wineQuantity", newValue);
        });

        TableColumn<Wine, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceColumn.setOnEditCommit(event -> {
            Wine wine = event.getRowValue();
            double newValue = event.getNewValue();
            updateWineValue(wine, "winePrice", newValue);
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

                    Wine wine = getTableView().getItems().get(getIndex());
                    if (wine.getQuantity() < 3) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("-fx-text-fill: white;");
                    }
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
                     "DELETE FROM Wine WHERE wineName = ?")) {

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

    private void addNewWine(String name) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Wine (wineID, wineName, wineType, wineVintage, wineQuantity, winePrice) " +
                             "VALUES (?, ?, ?, ?, ?, ?)")) {

            int newID = getMaxID() + 1;

            stmt.setInt(1, newID);
            stmt.setString(2, name);
            stmt.setString(3, "Red"); // Set initial type to Red
            stmt.setInt(4, 1900); // Set initial vinatge to 1900
            stmt.setInt(5, 0); // Set initial quantity to 0
            stmt.setDouble(6, 0.0); // Set initial prize to 0

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
            } else if (newValue instanceof Double) {
                stmt.setDouble(1, (Double) newValue);
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

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT wineID, wineName, wineType, wineVintage, wineQuantity, winePrice FROM Wine")) {

            while (rs.next()) {
                int ID = rs.getInt("wineID");
                String Name = rs.getString("wineName");
                String Type = rs.getString("wineType");
                int Vintage = rs.getInt("wineVintage");
                int Quantity = rs.getInt("wineQuantity");
                double Price = rs.getDouble("winePrice");


                // Create Wine object and add to the list
                Wine wine = new Wine(ID, Name, Type, Vintage, Quantity, Price);
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

    private void showTopWines() {
        // Execute the SQL query to get the top 5 most used ingredients
        String query =
                "SELECT I.wineName, COUNT(*) as totalUsage " +
                        "FROM Sale_Dish SD" +
                        "JOIN Dish DI ON SD.dishID = DI.dishID" +
                        "JOIN Wine I ON DI.wineID = I.wineID" +
                        "GROUP BY I.wineID" +
                        "ORDER BY totalUsage DESC" +
                        "LIMIT 4;";

        StringBuilder topWines = new StringBuilder();
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String wineName = rs.getString("wineName");
                int totalUsage = rs.getInt("totalUsage");
                topWines.append(wineName).append(": ").append(totalUsage).append("\n");
            }

            // Display the top ingredients in the Text
            topWineText.setText(topWines.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showMostExpensiveWines() {
        String query = "SELECT wineName, winePrice " +
                "FROM Wine " +
                "ORDER BY winePrice DESC " +
                "LIMIT 4;";

        StringBuilder mostExpensiveWines = new StringBuilder();
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String wineName = rs.getString("wineName");
                double cost = rs.getDouble("winePrice");
                mostExpensiveWines.append(wineName).append(": £").append(cost).append("\n");
            }

            mostExpensiveWinesText.setText(mostExpensiveWines.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showHighestQuantityWines() {
        String query = "SELECT wineName, wineQuantity" +
                "FROM Wine " +
                "ORDER BY wineQuantity DESC " +
                "LIMIT 4;";

        StringBuilder highestQuantityWines = new StringBuilder();
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String ingredientName = rs.getString("wineName");
                int quantity = rs.getInt("wineQuantity");
                highestQuantityWines.append(ingredientName).append(": ").append(quantity).append("\n");
            }

            highestQuantityWineText.setText(highestQuantityWines.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createMostExpensiveWinesBox() {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 10;");
        box.setPadding(new Insets(10));
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);

        Text titleText = new Text("Most Expensive Wines");
        titleText.setFont(Font.font(16));
        titleText.setFill(Color.BLACK);

        mostExpensiveWinesText = new Text();
        mostExpensiveWinesText.setFont(Font.font(12));
        mostExpensiveWinesText.setFill(Color.BLACK);
        mostExpensiveWinesText.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(titleText, mostExpensiveWinesText);

        box.setMinWidth(250);
        box.setMaxWidth(250);

        return box;
    }

    private VBox createHighestQuantityWinesBox() {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 10;");
        box.setPadding(new Insets(10));
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);

        Text titleText = new Text("Highest Quantity Wines");
        titleText.setFont(Font.font(16));
        titleText.setFill(Color.BLACK);

        highestQuantityWineText = new Text();
        highestQuantityWineText.setFont(Font.font(12));
        highestQuantityWineText.setFill(Color.BLACK);
        highestQuantityWineText.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(titleText, highestQuantityWineText);

        box.setMinWidth(250);
        box.setMaxWidth(250);

        return box;
    }

    private VBox createTopWinesBox() {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 10;");
        box.setPadding(new Insets(10));
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);

        Text titleText = new Text("Most Used Wines");
        titleText.setFont(Font.font(16));
        titleText.setFill(Color.BLACK);

        topWineText = new Text();
        topWineText.setFont(Font.font(12));
        topWineText.setFill(Color.BLACK);
        topWineText.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(titleText, topWineText);

        // Set the position and size of the top ingredients box

        box.setMinWidth(250);
        box.setMaxWidth(250);

        return box;
    }

}