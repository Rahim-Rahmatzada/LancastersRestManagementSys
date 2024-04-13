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
import model.AdminDatabaseConnector;
import model.Ingredient;

import java.sql.*;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class InventoryUI extends BaseUI {


    private Text topIngredientsText;
    private Text mostExpensiveIngredientsText;
    private Text highestQuantityIngredientsText;

    private TableView<Ingredient> ingredientTableView; // Store a reference to the TableView


    public InventoryUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Inventory");
        setTopText("Inventory Overview");



        // Set the main content for the InventoryUI.
        VBox inventoryMainContent = new VBox();
        inventoryMainContent.setPadding(new Insets(20));
        inventoryMainContent.setSpacing(10);

        // Create a TableView to display ingredient data
        ingredientTableView = createIngredientTableView(); // Store the reference to the TableView



        VBox topIngredientsBox = createTopIngredientsBox();
        VBox mostExpensiveIngredientsBox = createMostExpensiveIngredientsBox();
        VBox highestQuantityIngredientsBox = createHighestQuantityIngredientsBox();

        HBox insightsBox = new HBox();
        insightsBox.setSpacing(50);
        insightsBox.setAlignment(Pos.CENTER);
        insightsBox.getChildren().addAll(topIngredientsBox, mostExpensiveIngredientsBox, highestQuantityIngredientsBox);

        HBox controlsBox = createControlsBox();

        inventoryMainContent.getChildren().addAll(ingredientTableView, controlsBox, insightsBox);

        setMainContent(inventoryMainContent);

        // Populate top ingredients
        showTopIngredients();
        showMostExpensiveIngredients();
        showHighestQuantityIngredients();
    }

    private TableView<Ingredient> createIngredientTableView() {
        TableView<Ingredient> tableView = new TableView<>();

        tableView.setStyle("-fx-background-color: #1A1A1A;");


        // Create table columns
        TableColumn<Ingredient, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Ingredient ingredient = event.getRowValue();
            String newValue = event.getNewValue();
            updateIngredientValue(ingredient, "ingredientName", newValue);
        });

        TableColumn<Ingredient, Double> costColumn = new TableColumn<>("Price per unit (£)");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        costColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        costColumn.setOnEditCommit(event -> {
            Ingredient ingredient = event.getRowValue();
            double newValue = event.getNewValue();
            updateIngredientValue(ingredient, "ingredientCost", newValue);
        });

        TableColumn<Ingredient, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            Ingredient ingredient = event.getRowValue();
            int newValue = event.getNewValue();
            updateIngredientValue(ingredient, "ingredientQuantity", newValue);
        });

        TableColumn<Ingredient, Integer> thresholdColumn = new TableColumn<>("Low Stock Threshold");
        thresholdColumn.setCellValueFactory(new PropertyValueFactory<>("threshold"));
        thresholdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        thresholdColumn.setOnEditCommit(event -> {
            Ingredient ingredient = event.getRowValue();
            int newValue = event.getNewValue();
            updateIngredientValue(ingredient, "ingredientThreshold", newValue);
        });
        thresholdColumn.setStyle("-fx-text-fill: white;");


        // Set the style for table columns
        nameColumn.setStyle("-fx-text-fill: white;");
        costColumn.setStyle("-fx-text-fill: white;");
        quantityColumn.setStyle("-fx-text-fill: white;");

        // Add columns to the table view
        tableView.getColumns().addAll(nameColumn, costColumn, quantityColumn, thresholdColumn);

        // Load ingredient data from the database
        ObservableList<Ingredient> ingredientData = getIngredientDataFromDatabase();

        // Set the data to the table view
        tableView.setItems(ingredientData);



        nameColumn.setCellFactory(column -> new TableCell<Ingredient, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);

                    Ingredient ingredient = getTableView().getItems().get(getIndex());
                    if (ingredient.getQuantity() < ingredient.getThreshold()) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("-fx-text-fill: white;");
                    }
                }
            }
        });

        // Set the cell factory to style table cells
        tableView.setRowFactory(tv -> {
            TableRow<Ingredient> row = new TableRow<>();
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
        tableView.setPlaceholder(new Label("No ingredients found."));

        // Remove the empty column
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Enable cell selection and editing
        tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
        tableView.setEditable(true);

        return tableView;
    }

    private HBox createDeleteIngredientControls() {
        HBox deleteIngredientBox = new HBox();
        deleteIngredientBox.setSpacing(10);

        TextField ingredientNameField = new TextField();
        ingredientNameField.setPromptText("Enter Ingredient Name");

        Button deleteIngredientButton = new Button("Delete Ingredient");
        deleteIngredientButton.setOnAction(event -> {
            String ingredientName = ingredientNameField.getText().trim();
            if (!ingredientName.isEmpty()) {
                deleteIngredient(ingredientName);
                ingredientNameField.clear();
            } else {
                showAlert("Invalid Ingredient", "Please enter a valid ingredient name.");
            }
        });

        deleteIngredientBox.getChildren().addAll(ingredientNameField, deleteIngredientButton);

        return deleteIngredientBox;
    }

    private void deleteIngredient(String ingredientName) {
        try (Connection conn = AdminDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM Ingredient WHERE ingredientName = ?")) {

            stmt.setString(1, ingredientName);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Refresh the table view
                reloadIngredientTableView();
            } else {
                showAlert("Ingredient Not Found", "No ingredient named: " + ingredientName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete ingredient.");
        }
    }

    private HBox createControlsBox() {
        HBox controlsBox = new HBox();
        controlsBox.setSpacing(20);
        controlsBox.setAlignment(Pos.CENTER_LEFT);
        controlsBox.setPadding(new Insets(0, 0, 0, 200)); // Add left padding to shift the controls to the right

        controlsBox.getChildren().addAll(
                createAddIngredientControls(),
                createDeleteIngredientControls()
        );

        return controlsBox;
    }

    private HBox createAddIngredientControls() {
        HBox addIngredientBox = new HBox();
        addIngredientBox.setSpacing(10);

        TextField ingredientNameField = new TextField();
        ingredientNameField.setPromptText("Enter Ingredient Name");

        Button addIngredientButton = new Button("Add Ingredient");
        addIngredientButton.setOnAction(event -> {
            String ingredientName = ingredientNameField.getText().trim();
            if (!ingredientName.isEmpty()) {
                addNewIngredient(ingredientName);
                ingredientNameField.clear();
            } else {
                showAlert("Invalid Ingredient", "Please enter a valid ingredient name.");
            }
        });

        addIngredientBox.getChildren().addAll(ingredientNameField, addIngredientButton);

        return addIngredientBox;
    }

    private void addNewIngredient(String ingredientName) {
        try (Connection conn = AdminDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Ingredient (ingredientID, ingredientName, ingredientCost, ingredientQuantity, ingredientThreshold) " +
                             "VALUES (?, ?, ?, ?, ?)")) {

            // ...
            stmt.setInt(5, 0); // Set initial threshold to 0

            // ...
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add new ingredient.");
        }
    }

    private int getMaxIngredientID() {
        int maxID = 0;
        try (Connection conn = AdminDatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(ingredientID) FROM Ingredient")) {

            if (rs.next()) {
                maxID = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxID;
    }

    private void updateIngredientValue(Ingredient ingredient, String column, Object newValue) {
        try (Connection conn = AdminDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE Ingredient SET " + column + " = ? WHERE ingredientID = ?")) {

            if (newValue instanceof String) {
                stmt.setString(1, (String) newValue);
            } else if (newValue instanceof Double) {
                stmt.setDouble(1, (Double) newValue);
            } else if (newValue instanceof Integer) {
                stmt.setInt(1, (Integer) newValue);
            }

            stmt.setInt(2, ingredient.getIngredientID());
            stmt.executeUpdate();

            // Refresh the table view
            reloadIngredientTableView();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update ingredient value.");
        } catch (Exception e) {
            showAlert("Error", "Invalid value entered.");
        }
    }

    public ObservableList<Ingredient> getIngredientDataFromDatabase() {
        ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

        try (Connection conn = AdminDatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ingredientID, ingredientName, ingredientCost, ingredientQuantity, ingredientThreshold FROM Ingredient")) {

            while (rs.next()) {
                int ingredientID = rs.getInt("ingredientID");
                String name = rs.getString("ingredientName");
                double cost = rs.getDouble("ingredientCost");
                int quantity = rs.getInt("ingredientQuantity");
                int threshold = rs.getInt("ingredientThreshold");

                Ingredient ingredient = new Ingredient(ingredientID, name, cost, quantity, threshold);
                ingredientList.add(ingredient);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredientList;
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void reloadIngredientTableView() {
        // Reload the data in the table view based on the updated threshold
        ObservableList<Ingredient> ingredientData = getIngredientDataFromDatabase();
        ingredientTableView.setItems(ingredientData);
    }

    private void showTopIngredients() {
        // Execute the SQL query to get the top 5 most used ingredients
        String query = "SELECT I.ingredientName, COUNT(*) as totalUsage " +
                "FROM Sale_Dish SD " +
                "JOIN Dish_Ingredient DI ON SD.dishID = DI.dishID " +
                "JOIN Ingredient I ON DI.ingredientID = I.ingredientID " +
                "GROUP BY I.ingredientID " +
                "ORDER BY totalUsage DESC " +
                "LIMIT 3;";

        StringBuilder topIngredients = new StringBuilder();
        try (Connection conn = AdminDatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String ingredientName = rs.getString("ingredientName");
                int totalUsage = rs.getInt("totalUsage");
                topIngredients.append(ingredientName).append(": ").append(totalUsage).append("\n");
            }

            // Display the top ingredients in the Text
            topIngredientsText.setText(topIngredients.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showMostExpensiveIngredients() {
        String query = "SELECT ingredientName, ingredientCost " +
                "FROM Ingredient " +
                "ORDER BY ingredientCost DESC " +
                "LIMIT 3;";

        StringBuilder mostExpensiveIngredients = new StringBuilder();
        try (Connection conn = AdminDatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String ingredientName = rs.getString("ingredientName");
                double cost = rs.getDouble("ingredientCost");
                mostExpensiveIngredients.append(ingredientName).append(": £").append(cost).append("\n");
            }

            mostExpensiveIngredientsText.setText(mostExpensiveIngredients.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showHighestQuantityIngredients() {
        String query = "SELECT ingredientName, ingredientQuantity " +
                "FROM Ingredient " +
                "ORDER BY ingredientQuantity DESC " +
                "LIMIT 3;";

        StringBuilder highestQuantityIngredients = new StringBuilder();
        try (Connection conn = AdminDatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String ingredientName = rs.getString("ingredientName");
                int quantity = rs.getInt("ingredientQuantity");
                highestQuantityIngredients.append(ingredientName).append(": ").append(quantity).append("\n");
            }

            highestQuantityIngredientsText.setText(highestQuantityIngredients.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createMostExpensiveIngredientsBox() {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 10;");
        box.setPadding(new Insets(10));
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);

        Text titleText = new Text("Most Expensive Ingredients");
        titleText.setFont(Font.font(16));
        titleText.setFill(Color.BLACK);

        mostExpensiveIngredientsText = new Text();
        mostExpensiveIngredientsText.setFont(Font.font(12));
        mostExpensiveIngredientsText.setFill(Color.BLACK);
        mostExpensiveIngredientsText.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(titleText, mostExpensiveIngredientsText);

        box.setMinWidth(250);
        box.setMaxWidth(250);

        return box;
    }

    private VBox createHighestQuantityIngredientsBox() {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 10;");
        box.setPadding(new Insets(10));
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);

        Text titleText = new Text("Highest Quantity Ingredients");
        titleText.setFont(Font.font(16));
        titleText.setFill(Color.BLACK);

        highestQuantityIngredientsText = new Text();
        highestQuantityIngredientsText.setFont(Font.font(12));
        highestQuantityIngredientsText.setFill(Color.BLACK);
        highestQuantityIngredientsText.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(titleText, highestQuantityIngredientsText);

        box.setMinWidth(250);
        box.setMaxWidth(250);

        return box;
    }

    private VBox createTopIngredientsBox() {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 10;");
        box.setPadding(new Insets(10));
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);

        Text titleText = new Text("Most Used Ingredients");
        titleText.setFont(Font.font(16));
        titleText.setFill(Color.BLACK);

        topIngredientsText = new Text();
        topIngredientsText.setFont(Font.font(12));
        topIngredientsText.setFill(Color.BLACK);
        topIngredientsText.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(titleText, topIngredientsText);

        // Set the position and size of the top ingredients box

        box.setMinWidth(250);
        box.setMaxWidth(250);

        return box;
    }



}

//set threshold
//low stock items
//most used ingredient