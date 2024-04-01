package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StockOrdersUI extends BaseUI {

    private DatePicker orderDatePicker;
    private TableView<Order> orderTableView;

    public StockOrdersUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Stock Orders");
        setTopText("Stock Orders Overview");

        // Set the main content for the StockOrdersUI.
        VBox ordersMainContent = new VBox();
        ordersMainContent.setSpacing(20);
        ordersMainContent.setPadding(new Insets(20));

        // Create a DatePicker for selecting the order date
        orderDatePicker = new DatePicker();
        orderDatePicker.setPromptText("Date For Order");

        // Create a button to get the order
        Button getOrderButton = new Button("Get Order");
        getOrderButton.setOnAction(e -> getOrder());

        // Create an HBox to hold the DatePicker and button
        HBox dateSelectionBox = new HBox(orderDatePicker, getOrderButton);
        dateSelectionBox.setSpacing(10);
        dateSelectionBox.setAlignment(Pos.CENTER_LEFT);

        // Create a TableView to display the order information
        orderTableView = createTableView();

        // Create a combo box for selecting the order status
        ComboBox<String> orderStatusComboBox = new ComboBox<>();
        orderStatusComboBox.getItems().addAll("Pending", "Shipped", "Received");
        orderStatusComboBox.setPromptText("Select Order Status");

        // Create a button to update the order status
        Button updateStatusButton = new Button("Update Status");
        updateStatusButton.setOnAction(e -> updateOrderStatus());

        // Create an HBox to hold the order status combo box and button
        HBox orderStatusBox = new HBox(orderStatusComboBox, updateStatusButton);
        orderStatusBox.setSpacing(10);
        orderStatusBox.setAlignment(Pos.CENTER_LEFT);

        // Add the components to the main content
        ordersMainContent.getChildren().addAll(dateSelectionBox, orderTableView, orderStatusBox);

        setMainContent(ordersMainContent);
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

        TableColumn<Order, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusColumn.setStyle("-fx-text-fill: white;");

        // Set the style for table columns
        ingredientColumn.setStyle("-fx-text-fill: white;");
        quantityColumn.setStyle("-fx-text-fill: white;");
        costColumn.setStyle("-fx-text-fill: white;");

        // Add columns to the table view
        tableView.getColumns().addAll(ingredientColumn, quantityColumn, costColumn, statusColumn);

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
    }

    private void updateOrderStatus() {
        Order selectedOrder = orderTableView.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("No Order Selected", "Please select an order to update the status.");
            return;
        }

        LocalDate selectedDate = orderDatePicker.getValue();
        if (selectedDate == null) {
            showAlert("No Date Selected", "Please select a date to update the order status.");
            return;
        }

        ComboBox<String> orderStatusComboBox = (ComboBox<String>) ((HBox) orderTableView.getParent().getParent()).lookup(".combo-box");
        String selectedStatus = orderStatusComboBox.getValue();
        if (selectedStatus == null) {
            showAlert("No Status Selected", "Please select a status to update the order.");
            return;
        }

        selectedOrder.setStatus(selectedStatus);
        orderTableView.refresh(); // Refresh the table view to reflect the updated status
    }

    private List<Order> getDummyOrders() {
        // Create dummy orders for testing purposes
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("Ingredient 1", 10, 50.0));
        orders.add(new Order("Ingredient 2", 5, 30.0));
        orders.add(new Order("Ingredient 3", 8, 40.0));
        orders.add(new Order("Ingredient 1", 10, 50.0));
        orders.add(new Order("Ingredient 2", 5, 30.0));
        orders.add(new Order("Ingredient 3", 8, 40.0));
        orders.add(new Order("Ingredient 1", 10, 50.0));
        orders.add(new Order("Ingredient 2", 5, 30.0));
        orders.add(new Order("Ingredient 3", 8, 40.0));
        orders.add(new Order("Ingredient 1", 10, 50.0));
        orders.add(new Order("Ingredient 2", 5, 30.0));
        orders.add(new Order("Ingredient 3", 8, 40.0));
        orders.add(new Order("Ingredient 1", 10, 50.0));
        orders.add(new Order("Ingredient 2", 5, 30.0));
        orders.add(new Order("Ingredient 3", 8, 40.0));
        orders.add(new Order("Ingredient 1", 10, 50.0));
        orders.add(new Order("Ingredient 2", 5, 30.0));
        orders.add(new Order("Ingredient 3", 8, 40.0));
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
}