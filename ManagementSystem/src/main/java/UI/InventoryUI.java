package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.DatabaseConnector;
import model.Ingredient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InventoryUI extends BaseUI {

    private int threshold = 100;

    private TableView<Ingredient> ingredientTableView; // Store a reference to the TableView


    public InventoryUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Inventory");
        setTopText("Inventory Overviewwwwwwww");



        // Set the main content for the InventoryUI.
        VBox inventoryMainContent = new VBox();
        inventoryMainContent.setPadding(new Insets(20));
        inventoryMainContent.setSpacing(10);

        // Create a TableView to display ingredient data
        ingredientTableView = createIngredientTableView(); // Store the reference to the TableView

        // Add the TableView to the main content
        inventoryMainContent.getChildren().addAll(ingredientTableView, createThresholdControls());

        setMainContent(inventoryMainContent);
    }

    private TableView<Ingredient> createIngredientTableView() {
        TableView<Ingredient> tableView = new TableView<>();

        tableView.setStyle("-fx-background-color: #1A1A1A;");


        // Create table columns
        TableColumn<Ingredient, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(50); // Set the preferred width of the name column


        TableColumn<Ingredient, Double> costColumn = new TableColumn<>("Cost");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        costColumn.setPrefWidth(20); // Set the preferred width of the cost column


        TableColumn<Ingredient, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setPrefWidth(20); // Set the preferred width of the quantity column


        // Set the style for table columns
        nameColumn.setStyle("-fx-text-fill: white;");
        costColumn.setStyle("-fx-text-fill: white;");
        quantityColumn.setStyle("-fx-text-fill: white;");

        // Add columns to the table view
        tableView.getColumns().addAll(nameColumn, costColumn, quantityColumn);

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
                        if (ingredient.getQuantity() < threshold) {
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

        return tableView;
    }

    private ObservableList<Ingredient> getIngredientDataFromDatabase() {
        ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ingredientID, ingredientName, ingredientCost, ingredientQuantity FROM Ingredient")) {

            while (rs.next()) {
                int ingredientID = rs.getInt("ingredientID");
                String name = rs.getString("ingredientName");
                double cost = rs.getDouble("ingredientCost");
                int quantity = rs.getInt("ingredientQuantity");

                Ingredient ingredient = new Ingredient(ingredientID, name, cost, quantity);
                ingredientList.add(ingredient);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredientList;
    }

    private HBox createThresholdControls() {
        HBox thresholdBox = new HBox();
        thresholdBox.setSpacing(10);

        TextField thresholdField = new TextField();
        thresholdField.setPromptText("Enter Threshold");
        thresholdField.setText(String.valueOf(threshold));

        Button setThresholdButton = new Button("Set Threshold");
        setThresholdButton.setOnAction(event -> {
            try {
                threshold = Integer.parseInt(thresholdField.getText());
                reloadIngredientTableView();
            } catch (NumberFormatException e) {
                System.out.println("Invalid threshold value.");
            }
        });

        Button showTopIngredientsButton = new Button("Show Top Ingredients");
        showTopIngredientsButton.setOnAction(event -> showTopIngredients());

        thresholdBox.getChildren().addAll(thresholdField, setThresholdButton, showTopIngredientsButton);

        return thresholdBox;
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
                "LIMIT 5;";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            VBox topIngredientsBox = new VBox();
            while (rs.next()) {
                String ingredientName = rs.getString("ingredientName");
                int totalUsage = rs.getInt("totalUsage");
                Label label = new Label(ingredientName + ": " + totalUsage);
                topIngredientsBox.getChildren().add(label);
            }

            // Display the top ingredients below the grid
            VBox mainContent = (VBox) getMainContent();
            mainContent.getChildren().add(topIngredientsBox);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}

//set threshold
//low stock items
//most used ingredient