package UI;

import DatabaseConnections.AdminDatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.DoubleStringConverter;
import model.Dish;
import model.Ingredient;

import java.sql.*;

public class DishUI extends BaseUI {

    public DishUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Dish");
        setTopText("Dish Overview");
        initializeUI();
    }
    private void initializeUI() {
        // Create TableView for dish details
        TableView<Dish> dishTableView = new TableView<>();
        dishTableView.setStyle("-fx-background-color: #1A1A1A;");
        dishTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Enable cell selection and editing
        dishTableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
        dishTableView.setEditable(true);

        TableColumn<Dish, Integer> dishIDColumn = new TableColumn<>("Dish ID");
        dishIDColumn.setCellValueFactory(new PropertyValueFactory<>("dishID"));

        TableColumn<Dish, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Dish, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceColumn.setOnEditCommit(event -> {
            Dish dish = event.getRowValue();
            dish.setPrice(event.getNewValue());
            updateDishInDatabase(dish);
        });

        TableColumn<Dish, String> descriptionColumn = new TableColumn<>("Dish Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("dishDescription"));
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setOnEditCommit(event -> {
            Dish dish = event.getRowValue();
            dish.setDescription(event.getNewValue());
            updateDishInDatabase(dish);
        });

        TableColumn<Dish, String> allergyInfoColumn = new TableColumn<>("Allergy Info");
        allergyInfoColumn.setCellValueFactory(new PropertyValueFactory<>("allergyInfo"));

        dishIDColumn.setStyle("-fx-text-fill: white;");
        nameColumn.setStyle("-fx-text-fill: white;");
        priceColumn.setStyle("-fx-text-fill: white;");
        descriptionColumn.setStyle("-fx-text-fill: white;");
        allergyInfoColumn.setStyle("-fx-text-fill: white;");

        // Add columns to TableView
        dishTableView.getColumns().addAll(dishIDColumn, nameColumn, priceColumn, descriptionColumn, allergyInfoColumn);

        // Load dish data from the database
        ObservableList<Dish> dishData = getDishDataFromDatabase();

        // Set the data to the table view
        dishTableView.setItems(dishData);

        // Set the cell factory to style table cells
        dishTableView.setRowFactory(tv -> {
            TableRow<Dish> row = new TableRow<>();
            row.setStyle("-fx-background-color: #1A1A1A;");

            // Change the highlight color of the selected cell
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


        // Create TableView for ingredients
        TableView<Ingredient> ingredientTableView = new TableView<>();
        ingredientTableView.setStyle("-fx-background-color: #1A1A1A;");
        ingredientTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Ingredient, String> ingredientNameColumn = new TableColumn<>("Ingredient Name");
        ingredientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ingredientNameColumn.setStyle("-fx-text-fill: white;");

        TableColumn<Ingredient, Integer> quantityColumn = new TableColumn<>("Quantity Of Ingredient Used For Dish");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityUsed"));
        quantityColumn.setStyle("-fx-text-fill: white;");

        ingredientTableView.getColumns().addAll(ingredientNameColumn, quantityColumn);

        // Set the TableView to only show when a dish is selected
        ingredientTableView.managedProperty().bind(ingredientTableView.visibleProperty());
        ingredientTableView.setVisible(false);

        // Add TableView to layout
        VBox mainContent = (VBox) getMainContent();
        mainContent.getChildren().addAll(dishTableView, ingredientTableView);

        // Add event handler to show ingredients when a dish is selected
        dishTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ObservableList<Ingredient> ingredients = getIngredientsForDishFromDatabase(newSelection.getDishID());
                ingredientTableView.setItems(ingredients);
                ingredientTableView.setVisible(true);
                ingredientTableView.setStyle("-fx-background-color: #1A1A1A;");
            } else {
                ingredientTableView.setVisible(false);
                ingredientTableView.setStyle("-fx-background-color: #1A1A1A;");
            }
        });

        // Set the cell factory to style table cells
        ingredientTableView.setRowFactory(tv -> {
            TableRow<Ingredient> row = new TableRow<>();
            row.setStyle("-fx-background-color: #1A1A1A;");

            // Change the highlight color of the selected cell
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

    }
    private void updateDishInDatabase(Dish dish) {
        try (Connection conn = AdminDatabaseConnector.getConnection()) {
            String query = "UPDATE dishes SET name = ?, description = ?, price = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setString(1, dish.getName());
            pstmt.setString(2, dish.getDescription());
            pstmt.setDouble(3, dish.getPrice());
            pstmt.setInt(4, dish.getId());

            int updatedRows = pstmt.executeUpdate();

            if (updatedRows > 0) {
                System.out.println("Dish updated successfully");
            } else {
                System.out.println("Failed to update dish");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private ObservableList<Dish> getDishDataFromDatabase() {
        ObservableList<Dish> dishList = FXCollections.observableArrayList();

        try (Connection conn = AdminDatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT dishID, name, price, dishDescription, allergyInfo FROM Dish")) {

            while (rs.next()) {
                int dishID = rs.getInt("dishID");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String description = rs.getString("dishDescription");
                String allergyInfo = rs.getString("allergyInfo");

                Dish dish = new Dish(dishID, name, price, description, allergyInfo);
                dishList.add(dish);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dishList;
    }
    private ObservableList<Ingredient> getIngredientsForDishFromDatabase(int dishID) {
        ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();

        try (Connection conn = AdminDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT i.ingredientID, i.ingredientName, di.ingredientQuantityUsed " +
                             "FROM Ingredient i " +
                             "JOIN Dish_Ingredient di ON i.ingredientID = di.ingredientID " +
                             "WHERE di.dishID = ?")) {

            stmt.setInt(1, dishID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int ingredientID = rs.getInt("ingredientID");
                    String name = rs.getString("ingredientName");
                    int quantityUsed = rs.getInt("ingredientQuantityUsed");  // Fetch the ingredientQuantityUsed from the database
                    Ingredient ingredient = new Ingredient(ingredientID, name, quantityUsed);
                    ingredients.add(ingredient);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredients;
    }


}
