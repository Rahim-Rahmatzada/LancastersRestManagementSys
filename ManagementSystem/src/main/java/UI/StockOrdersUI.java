package UI;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import model.DatabaseConnector;
import model.Ingredient;
import model.Order;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Menu;

/**
 * The StockOrdersUI class represents the user interface for managing stock orders.
 * It provides functionality for viewing and placing stock orders.
 */

//TO DO LET USER CHANGE STATUS


public class StockOrdersUI extends BaseUI {

    private TableView<Order> orderTableView;
    private boolean isPlacingOrder = false;

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private VBox orderDetailsBox;

    private TableView<Menu> menuTableView;
    private TableView<Ingredient> ingredientTableView;
    private DatePicker placementDatePicker;

    /**
     * Constructs a new instance of the StockOrdersUI class.
     *
     * @param uiSwitcher The UISwitcher instance for navigating between different UI screens.
     */

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

    /**
     * Initializes the order viewing UI.
     *
     * @param ordersMainContent The main content VBox for the orders UI.
     */
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
        goToPlaceOrderButton.setOnAction(e -> switchToOrderPlacementUI());

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

    /**
     * Retrieves orders based on the selected date range.
     */

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

    /**
     * Retrieves orders from the database based on the specified date range.
     *
     * @param startDate The start date of the date range.
     * @param endDate   The end date of the date range.
     * @return A list of orders within the specified date range.
     */

    private List<Order> getOrdersFromDatabase(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT orderID, dateOrdered, expectedDeliveryDate, orderStatus " +
                             "FROM StockOrders " +
                             "WHERE expectedDeliveryDate BETWEEN ? AND ?")) {

            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int orderID = rs.getInt("orderID");
                    LocalDate dateOrdered = rs.getDate("dateOrdered").toLocalDate();
                    LocalDate expectedDeliveryDate = rs.getDate("expectedDeliveryDate").toLocalDate();
                    String orderStatus = rs.getString("orderStatus");
                    Order order = new Order(orderID, dateOrdered, expectedDeliveryDate, orderStatus);
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    /**
     * Creates a TableView for displaying orders.
     *
     * @return The created TableView for orders.
     */

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

        TableColumn<Order, String> orderStatusColumn = new TableColumn<>("Order Status");
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        orderStatusColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        orderStatusColumn.setOnEditCommit(event -> {
            Order order = event.getRowValue();
            String newStatus = event.getNewValue();
            order.setOrderStatus(newStatus);
            updateOrderStatusInDatabase(order.getOrderID(), newStatus);
        });
        orderStatusColumn.setStyle("-fx-text-fill: white;");

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);


        // Add columns to the table view
        tableView.getColumns().addAll(orderIDColumn, dateOrderedColumn, expectedDeliveryDateColumn, orderStatusColumn);

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

//             Add event handler to view order details when a row is clicked
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    Order selectedOrder = row.getItem();
                    viewOrderDetails(selectedOrder);
                }
            });

            return row;
        });

        // Enable cell editing for the "Order Status" column
        orderStatusColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set the cell value factory for the "Order Status" column
        orderStatusColumn.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            String orderStatus = order.getOrderStatus();
            System.out.println("Getting order status: " + orderStatus); // Print statement
            return new SimpleStringProperty(orderStatus);
        });

        return tableView;
    }

    private void updateOrderStatusInDatabase(int orderID, String newStatus) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE StockOrders SET orderStatus = ? WHERE orderID = ?")) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, orderID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the details of the selected order.
     *
     * @param order The selected order.
     */

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

    /**
     * Retrieves the ingredients of an order from the database.
     *
     * @param orderID The ID of the order.
     * @return A list of ingredients for the specified order.
     */

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

    /**
     * Initializes the order placement UI.
     *
     * @param ordersMainContent The main content VBox for the orders UI.
     */

    private void initializeOrderPlacementUI(VBox ordersMainContent) {
        // Create a DatePicker for selecting the order date
        placementDatePicker = new DatePicker();
        placementDatePicker.setPromptText("Select Order Date");

        // Create a TableView to display the list of menus
        menuTableView = createMenuTableView();

        // Create a VBox to display ingredient details
        VBox ingredientDetailsBox = new VBox();
        ingredientDetailsBox.setSpacing(10);

        // Create a button to generate the order
        Button generateOrderButton = new Button("Generate Order");
        generateOrderButton.setOnAction(e -> generateOrder());

        // Create a button to go back to the order viewing page
        Button goToViewOrderButton = new Button("Go to View Order");
        goToViewOrderButton.setOnAction(e -> switchToOrderViewingUI());

        // Create an HBox to hold the DatePicker, Generate Order button, and Go to View Order button
        HBox buttonBox = new HBox(placementDatePicker, generateOrderButton, goToViewOrderButton);
        buttonBox.setSpacing(20);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(buttonBox, Priority.ALWAYS);

        // Add the components to the main content
        ordersMainContent.getChildren().addAll(buttonBox, menuTableView, ingredientDetailsBox);
    }

    /**
     * Creates a TableView for displaying menus.
     *
     * @return The created TableView for menus.
     */
    private TableView<Menu> createMenuTableView() {
        TableView<Menu> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #1A1A1A;");

        // Create table columns
        TableColumn<Menu, Integer> menuIDColumn = new TableColumn<>("Menu ID");
        menuIDColumn.setCellValueFactory(new PropertyValueFactory<>("menuID"));
        menuIDColumn.setStyle("-fx-text-fill: white;");

        TableColumn<Menu, LocalDate> effectiveDateColumn = new TableColumn<>("Effective Date");
        effectiveDateColumn.setCellValueFactory(new PropertyValueFactory<>("effectiveDate"));
        effectiveDateColumn.setStyle("-fx-text-fill: white;");

        TableColumn<Menu, String> menuStatusColumn = new TableColumn<>("Menu Status");
        menuStatusColumn.setCellValueFactory(new PropertyValueFactory<>("menuStatus"));
        menuStatusColumn.setStyle("-fx-text-fill: white;");

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Add columns to the table view
        tableView.getColumns().addAll(menuIDColumn, effectiveDateColumn, menuStatusColumn);

        // Set the cell factory to style table cells
        tableView.setRowFactory(tv -> {
            TableRow<Menu> row = new TableRow<>();
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

            // Add event handler to view ingredient details when a row is clicked
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    Menu selectedMenu = row.getItem();
                    viewIngredientDetails(selectedMenu);
                }
            });

            return row;
        });

        // Load menu data from the database
        List<Menu> menus = getMenusFromDatabase();
        tableView.getItems().addAll(menus);

        return tableView;
    }

    /**
     * Displays the ingredient details for the selected menu.
     *
     * @param menu The selected menu.
     */
    private void viewIngredientDetails(Menu menu) {
        // Clear previous ingredient details
        VBox ingredientDetailsBox = (VBox) getMainContent().getChildren().get(2);
        ingredientDetailsBox.getChildren().clear();

        // Retrieve ingredient details from the database based on the selected menu
        List<Ingredient> ingredients = getIngredientsFromDatabase(menu.getMenuID());

        // Create a TableView to display ingredients and quantities
        ingredientTableView = createIngredientTableView(ingredients);

        // Add the table view to the ingredient details box
        ingredientDetailsBox.getChildren().add(ingredientTableView);
    }

    /**
     * Creates a TableView for displaying ingredients.
     *
     * @param ingredients The list of ingredients to display.
     * @return The created TableView for ingredients.
     */

    private TableView<Ingredient> createIngredientTableView(List<Ingredient> ingredients) {
        TableView<Ingredient> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #1A1A1A;");
        tableView.setEditable(true);

        // Create table columns
        TableColumn<Ingredient, String> nameColumn = new TableColumn<>("Ingredient");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setStyle("-fx-text-fill: white;");

        TableColumn<Ingredient, Integer> quantityColumn = new TableColumn<>("Quantity Ordered");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            Ingredient ingredient = event.getRowValue();
            int newValue = event.getNewValue();
            ingredient.setQuantity(newValue);
        });
        quantityColumn.setStyle("-fx-text-fill: white;");

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Add columns to the table view
        tableView.getColumns().addAll(nameColumn, quantityColumn);

        // Set the items to the list of ingredients
        tableView.getItems().addAll(ingredients);

        // Set the cell factory to style table cells based on the low stock threshold
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
                    int currentQuantity = getIngredientQuantityFromDatabase(ingredient.getIngredientID());
                    int threshold = getIngredientThresholdFromDatabase(ingredient.getIngredientID());

                    if (currentQuantity < threshold) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("-fx-text-fill: white;");
                    }
                }
            }
        });

        // Set the row factory to style table rows
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

    /**
     * Retrieves the current quantity of an ingredient from the database.
     *
     * @param ingredientID The ID of the ingredient.
     * @return The current quantity of the ingredient.
     */

    private int getIngredientQuantityFromDatabase(int ingredientID) {
        int quantity = 0;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT ingredientQuantity FROM Ingredient WHERE ingredientID = ?")) {

            stmt.setInt(1, ingredientID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    quantity = rs.getInt("ingredientQuantity");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quantity;
    }


    /**
     * Retrieves the threshold of an ingredient from the database.
     *
     * @param ingredientID The ID of the ingredient.
     * @return The threshold of the ingredient.
     */

    private int getIngredientThresholdFromDatabase(int ingredientID) {
        int threshold = 0;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT ingredientThreshold FROM Ingredient WHERE ingredientID = ?")) {

            stmt.setInt(1, ingredientID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    threshold = rs.getInt("ingredientThreshold");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return threshold;
    }

    /**
     * Retrieves the ingredients of a menu from the database.
     *
     * @param menuID The ID of the menu.
     * @return A list of ingredients for the specified menu.
     */
    private List<Ingredient> getIngredientsFromDatabase(int menuID) {
        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT i.ingredientID, i.ingredientName " +
                             "FROM Ingredient i " +
                             "JOIN Dish_Ingredient di ON i.ingredientID = di.ingredientID " +
                             "JOIN Menu_Dish md ON di.dishID = md.dishID " +
                             "WHERE md.menuID = ?")) {

            stmt.setInt(1, menuID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int ingredientID = rs.getInt("ingredientID");
                    String name = rs.getString("ingredientName");
                    Ingredient ingredient = new Ingredient(ingredientID, name, 0, 0, 0);
                    ingredients.add(ingredient);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredients;
    }

    /**
     * Retrieves the menus from the database.
     *
     * @return A list of menus.
     */
    private List<Menu> getMenusFromDatabase() {
        List<Menu> menus = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT menuID, effectiveDate, menuStatus FROM Menu")) {

            while (rs.next()) {
                int menuID = rs.getInt("menuID");
                LocalDate effectiveDate = rs.getDate("effectiveDate").toLocalDate();
                String menuStatus = rs.getString("menuStatus");
                Menu menu = new Menu(menuID, effectiveDate, menuStatus);
                menus.add(menu);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menus;
    }

    /**
     * Generates an order based on the selected ingredients and quantities.
     */

    private void generateOrder() {
        LocalDate orderDate = placementDatePicker.getValue();
        if (orderDate == null) {
            showAlert("No Date Selected", "Please select a date for the order.");
            return;
        }

        List<Ingredient> ingredients = ingredientTableView.getItems();
        List<Ingredient> orderedIngredients = new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            if (ingredient.getQuantity() > 0) {
                orderedIngredients.add(ingredient);
            }
        }

        if (orderedIngredients.isEmpty()) {
            showAlert("No Ingredients Selected", "Please specify the quantity for at least one ingredient.");
            return;
        }

        // Generate a new order ID by incrementing the last order ID
        int newOrderID = getLastOrderID() + 1;

        // Calculate the expected delivery date as two days after the order date
        LocalDate expectedDeliveryDate = orderDate.plusDays(2);

        // Save the order to the database
        saveOrderToDatabase(newOrderID, orderDate, expectedDeliveryDate, orderedIngredients);

        showSuccessAlert("Order Generated", "The order has been successfully generated.");

        // Clear the ingredient table view and switch back to order viewing UI
        ingredientTableView.getItems().clear();
        switchToOrderViewingUI();
    }

    /**
     * Retrieves the last order ID from the database.
     *
     * @return The last order ID.
     */
    private int getLastOrderID() {
        int lastOrderID = 0;

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(orderID) AS maxOrderID FROM StockOrders")) {

            if (rs.next()) {
                lastOrderID = rs.getInt("maxOrderID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastOrderID;
    }

    /**
     * Saves an order and its associated ingredients to the database.
     *
     * @param orderID The ID of the order.
     * @param orderDate The date of the order.
     * @param expectedDeliveryDate The expected delivery date of the order.
     * @param ingredients The list of ingredients in the order.
     */

    private void saveOrderToDatabase(int orderID, LocalDate orderDate, LocalDate expectedDeliveryDate,
                                     List<Ingredient> ingredients) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(
                     "INSERT INTO StockOrders (orderID, dateOrdered, expectedDeliveryDate) " +
                             "VALUES (?, ?, ?)");
             PreparedStatement ingredientStmt = conn.prepareStatement(
                     "INSERT INTO StockOrders_Ingredient (stockOrdersID, ingredientID, ingredientQuantityOrdered) " +
                             "VALUES (?, ?, ?)")) {

            // Save the order details
            orderStmt.setInt(1, orderID);
            orderStmt.setDate(2, java.sql.Date.valueOf(orderDate));
            orderStmt.setDate(3, java.sql.Date.valueOf(expectedDeliveryDate));
            orderStmt.executeUpdate();

            // Save the ordered ingredients
            for (Ingredient ingredient : ingredients) {
                ingredientStmt.setInt(1, orderID);
                ingredientStmt.setInt(2, ingredient.getIngredientID());
                ingredientStmt.setInt(3, ingredient.getQuantity());
                ingredientStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Switches to the order placement UI.
     */
    private void switchToOrderPlacementUI() {
        isPlacingOrder = true;
        VBox ordersMainContent = (VBox) getMainContent();
        ordersMainContent.getChildren().clear();
        initializeOrderPlacementUI(ordersMainContent);
    }

    /**
     * Switches to the order viewing UI.
     */
    private void switchToOrderViewingUI() {
        isPlacingOrder = false;
        VBox ordersMainContent = (VBox) getMainContent();
        ordersMainContent.getChildren().clear();
        initializeOrderViewingUI(ordersMainContent);
    }

    /**
     * Shows an alert dialog with the specified title and content.
     *
     * @param title The title of the alert.
     * @param content The content of the alert.
     */

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Shows a success alert dialog with the specified title and content.
     *
     * @param title The title of the success alert.
     * @param content The content of the success alert.
     */

    private void showSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

