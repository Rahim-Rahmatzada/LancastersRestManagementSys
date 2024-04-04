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
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
// TO DO: show ingredients of low stock as red in place order UI


public class StockOrdersUI extends BaseUI {

    private DatePicker orderDatePicker;
    private TableView<Order> orderTableView;
    private ComboBox<String> orderStatusComboBox;
    private boolean isPlacingOrder = false;

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private VBox orderDetailsBox;

    public StockOrdersUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Stock Orders");
        setTopText("Stock Orders Overview");

        // Set the main content for the StockOrdersUI.
        VBox ordersMainContent = new VBox();
        ordersMainContent.setSpacing(20);
        ordersMainContent.setPadding(new Insets(20));

        if (isPlacingOrder) {
//            initializeOrderPlacementUI(ordersMainContent);
            System.out.println("nooo");
        } else {
            initializeOrderViewingUI(ordersMainContent);
        }

        setMainContent(ordersMainContent);
    }

    private void initializeOrderViewingUI(VBox ordersMainContent) {
        // Create date pickers for selecting the start and end dates
        startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");
        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");

        // Create a button to get the orders
        Button getOrdersButton = new Button("Get Orders");
        getOrdersButton.setOnAction(e -> getOrders());

        // Create a button to go to the order placement page
        Button goToPlaceOrderButton = new Button("Go to Place Order");
//        goToPlaceOrderButton.setOnAction(e -> switchToOrderPlacementUI());

        // Create an HBox to hold the date pickers, "Get Orders" button, and "Go to Place Order" button
        HBox dateSelectionBox = new HBox(startDatePicker, endDatePicker, getOrdersButton, goToPlaceOrderButton);
        dateSelectionBox.setSpacing(10);
        dateSelectionBox.setAlignment(Pos.CENTER_LEFT);

        // Create a TableView to display the list of orders
        orderTableView = createOrderTableView();

        // Create a VBox to display order details
        orderDetailsBox = new VBox();
        orderDetailsBox.setSpacing(10);

        // Add the components to the main content
        ordersMainContent.getChildren().addAll(dateSelectionBox, orderTableView, orderDetailsBox);
    }

    private void getOrders() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showAlert("Invalid Date Range", "Please select both start and end dates.");
            return;
        }

        if (startDate.isAfter(endDate)) {
            showAlert("Invalid Date Range", "Start date cannot be after the end date.");
            return;
        }

        // Retrieve orders from the database based on the selected date range
        List<Order> orders = getOrdersFromDatabase(startDate, endDate);

        // Set the retrieved orders to the table view
        orderTableView.getItems().setAll(orders);
        orderDetailsBox.getChildren().clear();
    }

    private List<Order> getOrdersFromDatabase(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT orderID, dateOrdered, expectedDeliveryDate " +
                             "FROM StockOrders " +
                             "WHERE expectedDeliveryDate BETWEEN ? AND ?")) {

            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int orderID = rs.getInt("orderID");
                    LocalDate dateOrdered = rs.getDate("dateOrdered").toLocalDate();
                    LocalDate expectedDeliveryDate = rs.getDate("expectedDeliveryDate").toLocalDate();
                    Order order = new Order(orderID, dateOrdered, expectedDeliveryDate);
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }


    private TableView<Order> createOrderTableView() {
        TableView<Order> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #1A1A1A;");

        // Create table columns
        TableColumn<Order, Integer> orderIDColumn = new TableColumn<>("Order ID");
        orderIDColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        orderIDColumn.setStyle("-fx-text-fill: white;");

        TableColumn<Order, LocalDate> dateOrderedColumn = new TableColumn<>("Date Ordered");
        dateOrderedColumn.setCellValueFactory(new PropertyValueFactory<>("dateOrdered"));
        dateOrderedColumn.setStyle("-fx-text-fill: white;");

        TableColumn<Order, LocalDate> expectedDeliveryDateColumn = new TableColumn<>("Expected Delivery Date");
        expectedDeliveryDateColumn.setCellValueFactory(new PropertyValueFactory<>("expectedDeliveryDate"));
        expectedDeliveryDateColumn.setStyle("-fx-text-fill: white;");

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);


        // Add columns to the table view
        tableView.getColumns().addAll(orderIDColumn, dateOrderedColumn, expectedDeliveryDateColumn);

        // Set the cell factory to style table cells
        tableView.setRowFactory(tv -> {
            TableRow<Order> row = new TableRow<>();
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

            // Add event handler to view order details when a row is clicked
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    Order selectedOrder = row.getItem();
                    viewOrderDetails(selectedOrder);
                }
            });

            return row;
        });

        return tableView;
    }

    private void viewOrderDetails(Order order) {
        // Clear previous order details
        orderDetailsBox.getChildren().clear();

        // Retrieve order details from the database
        List<Ingredient> ingredients = getOrderIngredientsFromDatabase(order.getOrderID());

        // Create a TableView to display ingredients and quantities
        TableView<Ingredient> ingredientTableView = createIngredientTableView(ingredients);

        // Create a "Back" button to go back to the order list
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> orderDetailsBox.getChildren().clear());

        // Add the table view and "Back" button to the order details box
        orderDetailsBox.getChildren().addAll(ingredientTableView, backButton);
    }

    private List<Ingredient> getOrderIngredientsFromDatabase(int orderID) {
        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT i.ingredientID, i.ingredientName, soi.ingredientQuantityOrdered " +
                             "FROM Ingredient i " +
                             "JOIN StockOrders_Ingredient soi ON i.ingredientID = soi.ingredientID " +
                             "WHERE soi.stockOrdersID = ?")) {

            stmt.setInt(1, orderID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int ingredientID = rs.getInt("ingredientID");
                    String name = rs.getString("ingredientName");
                    int quantity = rs.getInt("ingredientQuantityOrdered");
                    Ingredient ingredient = new Ingredient(ingredientID, name, 0, quantity, 0);
                    ingredients.add(ingredient);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredients;
    }

//    private void initializeOrderPlacementUI(VBox ordersMainContent) {
//        TableView<Ingredient> ingredientTableView = createIngredientTableView();
//
//        // Create a DatePicker for selecting the order date
//        DatePicker orderDatePicker = new DatePicker();
//        orderDatePicker.setPromptText("Select Order Date");
//
//        // Create a button to generate the order
//        Button generateOrderButton = new Button("Generate Order");
//        generateOrderButton.setOnAction(e -> generateOrder(orderDatePicker, ingredientTableView));
//
//        // Create a button to go back to the order viewing page
//        Button goToViewOrderButton = new Button("Go to View Order");
//        goToViewOrderButton.setOnAction(e -> switchToOrderViewingUI());
//
//        // Create an HBox to hold the DatePicker, Generate Order button, and Go to View Order button
//        HBox buttonBox = new HBox(orderDatePicker, generateOrderButton, goToViewOrderButton);
//        buttonBox.setSpacing(20);
//        buttonBox.setAlignment(Pos.CENTER_LEFT);
//        HBox.setHgrow(buttonBox, Priority.ALWAYS);
//
//        // Create a TableView to display ingredients and quantities
//        ingredientTableView.setStyle("-fx-background-color: #1A1A1A;");
//
//        // Add the components to the main content
//        ordersMainContent.getChildren().addAll(buttonBox, ingredientTableView);
//    }

    private TableView<Ingredient> createIngredientTableView(List<Ingredient> ingredients) {
        TableView<Ingredient> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #1A1A1A;");

        // Create table columns
        TableColumn<Ingredient, String> nameColumn = new TableColumn<>("Ingredient");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setStyle("-fx-text-fill: white;");

        TableColumn<Ingredient, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setStyle("-fx-text-fill: white;");

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Wrap the table view in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Add columns to the table view
        tableView.getColumns().addAll(nameColumn, quantityColumn);

        // Set the items to the list of ingredients
        tableView.getItems().addAll(ingredients);

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
                Ingredient ingredient = new Ingredient(ingredientID, name, 0, 0,0);
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


//    private void switchToOrderPlacementUI() {
//        isPlacingOrder = true;
//        VBox ordersMainContent = (VBox) getMainContent();
//        ordersMainContent.getChildren().clear();
//        initializeOrderPlacementUI(ordersMainContent);
//    }

    private void switchToOrderViewingUI() {
        isPlacingOrder = false;
        VBox ordersMainContent = (VBox) getMainContent();
        ordersMainContent.getChildren().clear();
        initializeOrderViewingUI(ordersMainContent);
    }









//    private TableView<Order> createTableView() {
//        TableView<Order> tableView = new TableView<>();
//        tableView.setEditable(true);
//        tableView.setStyle("-fx-background-color: #1A1A1A;");
//
//        // Create table columns
//        TableColumn<Order, String> ingredientColumn = new TableColumn<>("Ingredient");
//        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
//        ingredientColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        ingredientColumn.setOnEditCommit(event -> {
//            Order order = event.getRowValue();
//            String newValue = event.getNewValue();
//            order.setIngredient(newValue);
//        });
//
//        TableColumn<Order, Integer> quantityColumn = new TableColumn<>("Quantity");
//        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
//        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
//        quantityColumn.setOnEditCommit(event -> {
//            Order order = event.getRowValue();
//            int newValue = event.getNewValue();
//            order.setQuantity(newValue);
//        });
//
//        TableColumn<Order, Double> costColumn = new TableColumn<>("Cost");
//        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
//        costColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
//        costColumn.setOnEditCommit(event -> {
//            Order order = event.getRowValue();
//            double newValue = event.getNewValue();
//            order.setCost(newValue);
//        });
//
//        // Set the style for table columns
//        ingredientColumn.setStyle("-fx-text-fill: white;");
//        quantityColumn.setStyle("-fx-text-fill: white;");
//        costColumn.setStyle("-fx-text-fill: white;");
//
//        // Add columns to the table view
//        tableView.getColumns().addAll(ingredientColumn, quantityColumn, costColumn);
//
//        // Set the cell factory to style table cells
//        tableView.setRowFactory(tv -> {
//            TableRow<Order> row = new TableRow<>();
//            row.setStyle("-fx-background-color: #1A1A1A;");
//
//            // Change the highlight color of the selected cell to red
//            row.setOnMouseEntered(event -> {
//                if (!row.isEmpty()) {
//                    row.setStyle("-fx-background-color: #333333;");
//                }
//            });
//
//            row.setOnMouseExited(event -> {
//                if (!row.isEmpty()) {
//                    row.setStyle("-fx-background-color: #1A1A1A;");
//                }
//            });
//
//            return row;
//        });
//
//        // Set the column resize policy to remove the empty column
//        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//
//        return tableView;
//    }

//    private void getOrder() {
//        LocalDate selectedDate = orderDatePicker.getValue();
//        if (selectedDate == null) {
//            // Show an alert when no date is selected
//            showAlert("No Date Selected", "Please select a date to get the order.");
//            return;
//        }
//
//
//        // TODO: Retrieve the order information for the selected date from the external API
//        // For testing purposes, you can use dummy data
//        List<Order> orders = getDummyOrders();
//
//        // Set the retrieved orders to the table view
//        orderTableView.getItems().setAll(orders);
//
//        // Set the initial order status based on the retrieved order
//        String orderStatus = orders.isEmpty() ? "Pending" : orders.get(0).getStatus();
//        orderStatusComboBox.setValue(orderStatus);
//    }

//    private void updateOrderStatus() {
//        LocalDate selectedDate = orderDatePicker.getValue();
//        if (selectedDate == null) {
//            showAlert("No Date Selected", "Please select a date to update the order status.");
//            return;
//        }
//
//        String selectedStatus = orderStatusComboBox.getValue();
//        if (selectedStatus == null) {
//            showAlert("No Status Selected", "Please select a status to update the order.");
//            return;
//        }
//
//        // TODO: Update the order status in the external API or database
//
//        // Update the status of all orders in the table view
//        for (Order order : orderTableView.getItems()) {
//            order.setStatus(selectedStatus);
//        }
//        showSuccessAlert("Status Updated", "Order status successfully changed to " + selectedStatus);
//
//    }

//    private List<Order> getDummyOrders() {
//        // Create dummy orders for testing purposes
//        List<Order> orders = new ArrayList<>();
//        orders.add(new Order("Ingredient 1", 10, 50.0));
//        orders.add(new Order("Ingredient 2", 5, 30.0));
//        orders.add(new Order("Ingredient 3", 8, 40.0));
//        return orders;
//    }

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

//    currently the stock order page works by user entering a date then seeing the ingredients from that order but what if user dosent know the exact date for order, so i want u to change the UI page. Instead the user should be ablee to enter a start and end date in date picker boxes. Then an sql query is to be made it should list all orders where the expectedDelivieryDate is within the range the user entered, then user should be able to click on the order and see what ingredients were bought in that order and the quanity of the ingredients. Futhermore they should be able to go back incase they clicked on the wrong order