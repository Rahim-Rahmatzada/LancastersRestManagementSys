package UI;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.LocalDateStringConverter;
import DatabaseConnections.AdminDatabaseConnector;
import model.DishAndWineHelper;
import model.Menu;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MenusUI extends BaseUI {
    private TableView<Menu> menuTableView;

    public MenusUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Menus");
        setTopText("Menu Overview");

        // Set the main content for the MenusUI
        VBox menusMainContent = new VBox();
        menusMainContent.setPadding(new Insets(20));
        menusMainContent.setSpacing(10);

        // Create a TableView to display menu data
        menuTableView = createMenuTableView();

        menusMainContent.getChildren().add(menuTableView);
        setMainContent(menusMainContent);
    }

    private TableView<Menu> createMenuTableView() {
        TableView<Menu> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #1A1A1A;");

        // Create table columns
        TableColumn<Menu, Integer> menuIDColumn = new TableColumn<>("Menu ID");
        menuIDColumn.setCellValueFactory(new PropertyValueFactory<>("menuID"));
        menuIDColumn.setStyle("-fx-text-fill: white;");

        TableColumn<Menu, LocalDate> effectiveDateColumn = new TableColumn<>("Effective Date");
        effectiveDateColumn.setCellValueFactory(cellData -> {
            LocalDate effectiveDate = cellData.getValue().getEffectiveDate();
            return new SimpleObjectProperty<>(effectiveDate);
        });
        effectiveDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        effectiveDateColumn.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            LocalDate newValue = event.getNewValue();
            updateMenuValue(menu, "effectiveDate", newValue);
        });
        effectiveDateColumn.setStyle("-fx-text-fill: white;");

        TableColumn<Menu, String> menuStatusColumn = new TableColumn<>("Menu Status");
        menuStatusColumn.setCellValueFactory(cellData -> {
            String menuStatus = cellData.getValue().getMenuStatus();
            return new SimpleStringProperty(menuStatus);
        });
        menuStatusColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        menuStatusColumn.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            String newValue = event.getNewValue();
            updateMenuValue(menu, "menuStatus", newValue);
        });
        menuStatusColumn.setStyle("-fx-text-fill: white;");

        // Add columns to the table view
        tableView.getColumns().addAll(menuIDColumn, effectiveDateColumn, menuStatusColumn);

        // Load menu data from the database
        ObservableList<Menu> menuData = getMenuDataFromDatabase();

        // Set the data to the table view
        tableView.setItems(menuData);

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

            // Add event handler to view dish and wine details when a row is clicked
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    Menu selectedMenu = row.getItem();
                    viewDishAndWineDetails(selectedMenu);
                }
            });

            return row;
        });

        // Remove the empty column
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
        tableView.setEditable(true);


        return tableView;
    }

    private void viewDishAndWineDetails(Menu selectedMenu) {
        // Get the main content VBox
        VBox menusMainContent = (VBox) getMainContent();

        // Check if the dish and wine details box already exists
        if (menusMainContent.getChildren().size() > 1) {
            // Remove the existing dish and wine details box
            menusMainContent.getChildren().remove(1);
        }

        // Create a new dish and wine details box
        VBox dishAndWineDetailsBox = new VBox();
        dishAndWineDetailsBox.setSpacing(10);

        // Retrieve dish and wine details from the database based on the selected menu
        List<DishAndWineHelper> dishAndWineList = getDishAndWineFromDatabase(selectedMenu.getMenuID());

        // Create a TableView to display dishes and wines
        TableView<DishAndWineHelper> dishAndWineTableView = createDishAndWineTableView(dishAndWineList);

        // Create a "Back" button to go back to the menu list
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> menusMainContent.getChildren().remove(dishAndWineDetailsBox));

        // Add the table view and "Back" button to the dish and wine details box
        dishAndWineDetailsBox.getChildren().addAll(dishAndWineTableView, backButton);

        // Add the dish and wine details box below the menu table view
        menusMainContent.getChildren().add(dishAndWineDetailsBox);
    }

    private TableView<DishAndWineHelper> createDishAndWineTableView(List<DishAndWineHelper> dishAndWineList) {
        TableView<DishAndWineHelper> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.setStyle("-fx-background-color: #1A1A1A;");

        // Create table columns
        TableColumn<DishAndWineHelper, String> dishNameColumn = new TableColumn<>("Dish Name");
        dishNameColumn.setCellValueFactory(new PropertyValueFactory<>("dishName"));
        dishNameColumn.setStyle("-fx-text-fill: white;");

        TableColumn<DishAndWineHelper, Double> dishPriceColumn = new TableColumn<>("Dish Price");
        dishPriceColumn.setCellValueFactory(new PropertyValueFactory<>("dishPrice"));
        dishPriceColumn.setStyle("-fx-text-fill: white;");

        TableColumn<DishAndWineHelper, String> dishDescriptionColumn = new TableColumn<>("Dish Description");
        dishDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("dishDescription"));
        dishDescriptionColumn.setStyle("-fx-text-fill: white;");

        TableColumn<DishAndWineHelper, String> allergyInfoColumn = new TableColumn<>("Allergy Info");
        allergyInfoColumn.setCellValueFactory(new PropertyValueFactory<>("allergyInfo"));
        allergyInfoColumn.setStyle("-fx-text-fill: white;");

        TableColumn<DishAndWineHelper, String> wineNameColumn = new TableColumn<>("Wine Name");
        wineNameColumn.setCellValueFactory(new PropertyValueFactory<>("wineName"));
        wineNameColumn.setStyle("-fx-text-fill: white;");

        TableColumn<DishAndWineHelper, Double> winePriceColumn = new TableColumn<>("Wine Price");
        winePriceColumn.setCellValueFactory(new PropertyValueFactory<>("winePrice"));
        winePriceColumn.setStyle("-fx-text-fill: white;");

        // Add columns to the table view
        tableView.getColumns().addAll(dishNameColumn, dishPriceColumn, dishDescriptionColumn, allergyInfoColumn, wineNameColumn, winePriceColumn);

        // Set the data to the table view
        tableView.setItems(FXCollections.observableArrayList(dishAndWineList));

        // Set the cell factory to style table cells
        tableView.setRowFactory(tv -> {
            TableRow<DishAndWineHelper> row = new TableRow<>();
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

    private List<DishAndWineHelper> getDishAndWineFromDatabase(int menuID) {
        List<DishAndWineHelper> dishAndWineList = new ArrayList<>();

        try (Connection conn = AdminDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT d.name AS dishName, d.price AS dishPrice, d.dishDescription, d.allergyInfo, w.wineName, w.winePrice " +
                             "FROM Dish d " +
                             "JOIN Wine w ON d.wineID = w.wineID " +
                             "JOIN Menu_Dish md ON d.dishID = md.dishID " +
                             "WHERE md.menuID = ?")) {

            stmt.setInt(1, menuID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String dishName = rs.getString("dishName");
                    double dishPrice = rs.getDouble("dishPrice");
                    String dishDescription = rs.getString("dishDescription");
                    String allergyInfo = rs.getString("allergyInfo");
                    String wineName = rs.getString("wineName");
                    double winePrice = rs.getDouble("winePrice");

                    DishAndWineHelper dishAndWine = new DishAndWineHelper(dishName, allergyInfo, wineName, dishPrice, dishDescription, winePrice);
                    dishAndWineList.add(dishAndWine);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dishAndWineList;
    }


    private ObservableList<Menu> getMenuDataFromDatabase() {
        ObservableList<Menu> menuList = FXCollections.observableArrayList();

        try (Connection conn = AdminDatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT menuID, effectiveDate, menuStatus FROM Menu")) {

            while (rs.next()) {
                int menuID = rs.getInt("menuID");
                LocalDate effectiveDate = rs.getDate("effectiveDate").toLocalDate();
                String menuStatus = rs.getString("menuStatus");

                Menu menu = new Menu(menuID, effectiveDate, menuStatus);
                menuList.add(menu);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menuList;
    }

    private void updateMenuValue(Menu menu, String column, Object newValue) {
        try (Connection conn = AdminDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE Menu SET " + column + " = ? WHERE menuID = ?")) {

            if (newValue instanceof String) {
                stmt.setString(1, (String) newValue);
            } else if (newValue instanceof LocalDate) {
                stmt.setDate(1, Date.valueOf((LocalDate) newValue));
            }

            stmt.setInt(2, menu.getMenuID());
            stmt.executeUpdate();

            // Refresh the table view
            reloadMenuTableView();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update menu value.");
        } catch (Exception e) {
            showAlert("Error", "Invalid value entered.");
        }
    }

    private void reloadMenuTableView() {
        // Reload the data in the table view
        ObservableList<Menu> menuData = getMenuDataFromDatabase();
        menuTableView.setItems(menuData);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}


//    Menus UI
//    Menu Creation and Management: Create new menus, edit existing ones, and archive outdated menus.
//        Dish Construction and Editing: Combine approved recipes into dishes; edit ingredients, preparation, and presentation details.
//        Price Setting: Determine and update pricing for each dish based on cost and markup strategy.
//        Allergen Information: Ensure all allergen information is up to date and clearly labeled on menu items.
//        Menu Review: Allow management and other stakeholders to review and provide feedback on draft menus.
//        Printable Menus: Generate printable versions of the final menus for use in the restaurant.

//    Menu Costing and Review: Oversee menu pricing, ingredient cost tracking, and ensure profitability.