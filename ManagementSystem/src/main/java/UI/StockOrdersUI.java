package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.DatabaseConnector;
import model.Ingredient;
import model.Order;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
// TO DO: show ingredients of low stock as red in place order UI


public class StockOrdersUI extends BaseUI {

    private DatePicker orderDatePicker;
    private TableView<Order> orderTableView;
    private ComboBox<String> orderStatusComboBox;
    private boolean isPlacingOrder = false;


    public StockOrdersUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Stock Orders");
        setTopText("Stock Orders Overview");

        // Set the main content for the StockOrdersUI.
        VBox ordersMainContent = new VBox();
        ordersMainContent.setSpacing(20);
        ordersMainContent.setPadding(new Insets(20));

        if (isPlacingOrder) {
            initializeOrderPlacementUI(ordersMainContent);
        } else {
            initializeOrderViewingUI(ordersMainContent);
        }

        setMainContent(ordersMainContent);
    }

    private void initializeOrderViewingUI(VBox ordersMainContent) {
        // Create a DatePicker for selecting the order date
        orderDatePicker = new DatePicker();
        orderDatePicker.setPromptText("Date For Order");

        // Create a button to get the order
        Button getOrderButton = new Button("Get Order");
        getOrderButton.setOnAction(e -> getOrder());

        // Create a button to go to the order placement page
        Button goToPlaceOrderButton = new Button("Go to Place Order");
        goToPlaceOrderButton.setOnAction(e -> switchToOrderPlacementUI());

        // Create an HBox to hold the DatePicker, "Get Order" button, and "Go to Place Order" button
        HBox dateSelectionBox = new HBox(orderDatePicker, getOrderButton,goToPlaceOrderButton);
        dateSelectionBox.setSpacing(10);
        dateSelectionBox.setAlignment(Pos.CENTER_LEFT);

        HBox.setHgrow(dateSelectionBox, Priority.ALWAYS);

        HBox topBox = new HBox(dateSelectionBox);
        topBox.setSpacing(20);
        topBox.setAlignment(Pos.CENTER_LEFT);

        // Create a TableView to display the order information
        orderTableView = createTableView();

        // Create a combo box for selecting the order status
        orderStatusComboBox = new ComboBox<>();
        orderStatusComboBox.getItems().addAll("Pending", "Received");
        orderStatusComboBox.setPromptText("Select Order Status");

        orderStatusComboBox.setOnAction(e -> {
            if (orderDatePicker.getValue() == null) {
                showAlert("No Date Selected", "Please select a date before selecting the order status.");
                orderStatusComboBox.getSelectionModel().clearSelection();
            }
        });

        // Create a button to update the order status
        Button updateStatusButton = new Button("Update Status");
        updateStatusButton.setOnAction(e -> updateOrderStatus());

        // Create an HBox to hold the order status combo box and button
        HBox orderStatusBox = new HBox(orderStatusComboBox, updateStatusButton);
        orderStatusBox.setSpacing(10);
        orderStatusBox.setAlignment(Pos.CENTER_LEFT);

        // Add the components to the main content
        ordersMainContent.getChildren().addAll(topBox, orderTableView, orderStatusBox);
    }

    private void initializeOrderPlacementUI(VBox ordersMainContent) {
        TableView<Ingredient> ingredientTableView = createIngredientTableView();

        // Create a DatePicker for selecting the order date
        DatePicker orderDatePicker = new DatePicker();
        orderDatePicker.setPromptText("Select Order Date");

        // Create a button to generate the order
        Button generateOrderButton = new Button("Generate Order");
        generateOrderButton.setOnAction(e -> generateOrder(orderDatePicker, ingredientTableView));

        // Create a button to go back to the order viewing page
        Button goToViewOrderButton = new Button("Go to View Order");
        goToViewOrderButton.setOnAction(e -> switchToOrderViewingUI());

        // Create an HBox to hold the DatePicker, Generate Order button, and Go to View Order button
        HBox buttonBox = new HBox(orderDatePicker, generateOrderButton, goToViewOrderButton);
        buttonBox.setSpacing(20);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(buttonBox, Priority.ALWAYS);

        // Create a TableView to display ingredients and quantities
        ingredientTableView.setStyle("-fx-background-color: #1A1A1A;");

        // Add the components to the main content
        ordersMainContent.getChildren().addAll(buttonBox, ingredientTableView);
    }

    private TableView<Ingredient> createIngredientTableView() {
        TableView<Ingredient> tableView = new TableView<>();
        tableView.setEditable(true);
        tableView.setStyle("-fx-background-color: #1A1A1A;");

        // Create table columns
        TableColumn<Ingredient, String> nameColumn = new TableColumn<>("Ingredient");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setStyle("-fx-text-fill: white;");

        TableColumn<Ingredient, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            Ingredient ingredient = event.getRowValue();
            int newValue = event.getNewValue();
            ingredient.setQuantity(newValue);
        });
        quantityColumn.setStyle("-fx-text-fill: white;");

        // Add columns to the table view
        tableView.getColumns().addAll(nameColumn, quantityColumn);

        // Load ingredient data from the database
        List<Ingredient> ingredientList = getIngredientsFromDatabase();
        tableView.getItems().addAll(ingredientList);

        // Set the column resize policy to remove the empty column
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set the cell factory to style table cells
        tableView.setRowFactory(tv -> {
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

        return tableView;
    }

    private List<Ingredient> getIngredientsFromDatabase() {
        List<Ingredient> ingredientList = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ingredientID, ingredientName FROM Ingredient")) {

            while (rs.next()) {
                int ingredientID = rs.getInt("ingredientID");
                String name = rs.getString("ingredientName");
                Ingredient ingredient = new Ingredient(ingredientID, name, 0, 0);
                ingredientList.add(ingredient);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredientList;
    }

    private void generateOrder(DatePicker orderDatePicker, TableView<Ingredient> ingredientTableView) {
        LocalDate selectedDate = orderDatePicker.getValue();
        if (selectedDate == null) {
            showAlert("No Date Selected", "Please select a date for the order.");
            return;
        }

        List<Ingredient> ingredients = ingredientTableView.getItems();
        StringBuilder orderContent = new StringBuilder();
        orderContent.append("Order Date: ").append(selectedDate).append("\n\n");
        orderContent.append("Ingredients:\n");

        for (Ingredient ingredient : ingredients) {
            if (ingredient.getQuantity() > 0) {
                orderContent.append(ingredient.getName()).append(": ").append(ingredient.getQuantity()).append("\n");
            }
        }

        // Prompt the user to choose a file location to save the order
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Order");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showSaveDialog(getMainContent().getScene().getWindow());

        if (selectedFile != null) {
            try {
                FileWriter writer = new FileWriter(selectedFile);
                writer.write(orderContent.toString());
                writer.close();
                showSuccessAlert("Order Generated", "The order has been successfully generated and saved.");
            } catch (IOException e) {
                showAlert("Error", "An error occurred while saving the order.");
            }
        }
    }


    private void switchToOrderPlacementUI() {
        isPlacingOrder = true;
        VBox ordersMainContent = (VBox) getMainContent();
        ordersMainContent.getChildren().clear();
        initializeOrderPlacementUI(ordersMainContent);
    }

    private void switchToOrderViewingUI() {
        isPlacingOrder = false;
        VBox ordersMainContent = (VBox) getMainContent();
        ordersMainContent.getChildren().clear();
        initializeOrderViewingUI(ordersMainContent);
    }









    private TableView<Order> createTableView() {
        TableView<Order> tableView = new TableView<>();
        tableView.setEditable(true);
        tableView.setStyle("-fx-background-color: #1A1A1A;");

        // Create table columns
        TableColumn<Order, String> ingredientColumn = new TableColumn<>("Ingredient");
        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
        ingredientColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        ingredientColumn.setOnEditCommit(event -> {
            Order order = event.getRowValue();
            String newValue = event.getNewValue();
            order.setIngredient(newValue);
        });

        TableColumn<Order, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            Order order = event.getRowValue();
            int newValue = event.getNewValue();
            order.setQuantity(newValue);
        });

        TableColumn<Order, Double> costColumn = new TableColumn<>("Cost");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        costColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        costColumn.setOnEditCommit(event -> {
            Order order = event.getRowValue();
            double newValue = event.getNewValue();
            order.setCost(newValue);
        });

        // Set the style for table columns
        ingredientColumn.setStyle("-fx-text-fill: white;");
        quantityColumn.setStyle("-fx-text-fill: white;");
        costColumn.setStyle("-fx-text-fill: white;");

        // Add columns to the table view
        tableView.getColumns().addAll(ingredientColumn, quantityColumn, costColumn);

        // Set the cell factory to style table cells
        tableView.setRowFactory(tv -> {
            TableRow<Order> row = new TableRow<>();
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

        // Set the column resize policy to remove the empty column
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return tableView;
    }

    private void getOrder() {
        LocalDate selectedDate = orderDatePicker.getValue();
        if (selectedDate == null) {
            // Show an alert when no date is selected
            showAlert("No Date Selected", "Please select a date to get the order.");
            return;
        }


        // TODO: Retrieve the order information for the selected date from the external API
        // For testing purposes, you can use dummy data
        List<Order> orders = getDummyOrders();

        // Set the retrieved orders to the table view
        orderTableView.getItems().setAll(orders);

        // Set the initial order status based on the retrieved order
        String orderStatus = orders.isEmpty() ? "Pending" : orders.get(0).getStatus();
        orderStatusComboBox.setValue(orderStatus);
    }

    private void updateOrderStatus() {
        LocalDate selectedDate = orderDatePicker.getValue();
        if (selectedDate == null) {
            showAlert("No Date Selected", "Please select a date to update the order status.");
            return;
        }

        String selectedStatus = orderStatusComboBox.getValue();
        if (selectedStatus == null) {
            showAlert("No Status Selected", "Please select a status to update the order.");
            return;
        }

        // TODO: Update the order status in the external API or database

        // Update the status of all orders in the table view
        for (Order order : orderTableView.getItems()) {
            order.setStatus(selectedStatus);
        }
        showSuccessAlert("Status Updated", "Order status successfully changed to " + selectedStatus);

    }

    private List<Order> getDummyOrders() {
        // Create dummy orders for testing purposes
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("Ingredient 1", 10, 50.0));
        orders.add(new Order("Ingredient 2", 5, 30.0));
        orders.add(new Order("Ingredient 3", 8, 40.0));
        return orders;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}