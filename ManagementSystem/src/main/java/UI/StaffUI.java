package UI;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import model.DatabaseConnector;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StaffUI extends BaseUI {
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private TableView<ScheduleEntry> scheduleTable;
    private CheckBox scheduleCheckBox;
    private CheckBox absencesCheckBox;
    private CheckBox holidaysCheckBox;
    private TextField nameField = new TextField();
    private DatePicker dateField;
    private TextField startTimeField= new TextField();
    private TextField endTimeField= new TextField();
    private ComboBox<String> statusComboBox;
    private Button staffPerformanceButton;
    private Button staffScheduleButton;
    private VBox staffSchedulingMainContent = new VBox();
    private Button generateScheduleButton;
    private Button addButton;



    public StaffUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Staff");
        setTopText("Staff Overview");

        // Set the main content for the StaffSchedulingUI.
        staffSchedulingMainContent.setPadding(new Insets(10));
        staffSchedulingMainContent.setSpacing(10);

        statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("ON", "OFF", "HOLIDAY", "ABSENT");

        staffPerformanceButton = new Button("Staff Performance");
        staffScheduleButton = new Button("Staff Schedule");
        staffPerformanceButton.setOnAction(event -> switchToStaffPerformanceView());
        staffScheduleButton.setOnAction(event -> switchToSchedulingView());

        // Create date pickers for start and end dates
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();

        // Create checkboxes for selecting data types
        scheduleCheckBox = new CheckBox("Schedule");
        absencesCheckBox = new CheckBox("Absences");
        holidaysCheckBox = new CheckBox("Holidays");
        scheduleCheckBox.setStyle("-fx-text-fill: white;");
        absencesCheckBox.setStyle("-fx-text-fill: white;");
        holidaysCheckBox.setStyle("-fx-text-fill: white;");

        // Create a button to generate the schedule
        generateScheduleButton = new Button("Generate Schedule");
        generateScheduleButton.setOnAction(event -> generateSchedule());

        // Initialize input fields
        nameField = new TextField();
        dateField = new DatePicker();
        startTimeField = new TextField();
        endTimeField = new TextField();

        // Create a button to Add/Modify Shifts
        addButton = new Button("Add/Modify Shift");
        addButton.setOnAction(event -> addOrModifyShift());

        // Create the schedule table
        scheduleTable = new TableView<>();
        scheduleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Add columns to the schedule table
        TableColumn<ScheduleEntry, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        TableColumn<ScheduleEntry, LocalTime> startColumn = new TableColumn<>("Start Time");
        startColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());

        TableColumn<ScheduleEntry, LocalTime> endColumn = new TableColumn<>("End Time");
        endColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());

        TableColumn<ScheduleEntry, String> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty());

        TableColumn<ScheduleEntry, String> employeeColumn = new TableColumn<>("Employee");
        employeeColumn.setCellValueFactory(cellData -> cellData.getValue().employeeNameProperty());
        employeeColumn.setCellFactory(column -> new ComboBoxTableCell<>(FXCollections.observableArrayList("Alice", "Bob", "Charlie", "David")));

        TableColumn<ScheduleEntry, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        scheduleTable.getColumns().add(statusColumn);

        scheduleTable.getColumns().addAll(dateColumn, startColumn, endColumn, durationColumn, employeeColumn);

        // Add components to the main content VBox
        staffSchedulingMainContent.getChildren().addAll(
                staffPerformanceButton,
                new Text("Start Date:"),
                startDatePicker,
                new Text("End Date:"),
                endDatePicker,
                new Text("Select Data Types:"),
                scheduleCheckBox,
                absencesCheckBox,
                holidaysCheckBox,
                generateScheduleButton,
                scheduleTable
        );
        staffSchedulingMainContent.getChildren().addAll(
                new Text("Name:"),
                nameField,
                new Text("Status:"),
                statusComboBox,
                new Text("Date:"),
                dateField,
                new Text("Start Time:"),
                startTimeField,
                new Text("End Time:"),
                endTimeField,
                addButton,
                createDeleteScheduleButton()
        );
        nameField.setPromptText("Enter Employee Name");
        startTimeField.setPromptText("Enter Starting Time HH:MM");
        endTimeField.setPromptText("Enter Ending Time HH:MM");

        setMainContent(staffSchedulingMainContent);
        setTextColor(staffSchedulingMainContent);
    }

    private void addOrModifyShift() {
        String name = nameField.getText();
        LocalDate date = dateField.getValue();
        LocalTime startTime = LocalTime.parse(startTimeField.getText());
        LocalTime endTime = LocalTime.parse(endTimeField.getText());
        String status = statusComboBox.getValue();

        // Validate input
        if (name.isEmpty() || date == null || startTime == null || endTime == null || startTime.isAfter(endTime) || status == null) {
            // Show error message if input is invalid
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid data.");
            alert.showAndWait();
            return;
        }

        // Update or insert the shift into the database
        try (Connection conn = DatabaseConnector.getConnection()) {
            // Check if the employee already has a shift on the selected date
            String query = "SELECT * FROM StaffSchedule WHERE scheduleDate = ? AND employeeName = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setString(2, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Update existing entry
                String updateQuery = "UPDATE StaffSchedule SET startTime = ?, endTime = ?, status = ? WHERE scheduleDate = ? AND employeeName = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setTime(1, Time.valueOf(startTime));
                updateStmt.setTime(2, Time.valueOf(endTime));
                updateStmt.setString(3, status);
                updateStmt.setDate(4, Date.valueOf(date));
                updateStmt.setString(5, name);
                updateStmt.executeUpdate();
            } else {
                // Insert new entry
                String insertQuery = "INSERT INTO StaffSchedule (startTime, endTime, scheduleDate, employeeName, status) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setTime(1, Time.valueOf(startTime));
                insertStmt.setTime(2, Time.valueOf(endTime));
                insertStmt.setDate(3, Date.valueOf(date));
                insertStmt.setString(4, name);
                insertStmt.setString(5, status);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error accessing database: " + e.getMessage());
            // Handle the error appropriately (e.g., show an alert to the user)
            return;
        }

        // Update the UI
        boolean existingEntry = false;
        for (ScheduleEntry entry : scheduleTable.getItems()) {
            if (entry.getDate().equals(date) && entry.getEmployeeName().equals(name)) {
                // Update existing entry in UI
                entry.setStartTime(startTime);
                entry.setEndTime(endTime);
                entry.setDuration(calculateDuration(startTime, endTime));
                entry.setStatus(status); // Set status
                existingEntry = true;
                break;
            }
        }

        if (!existingEntry) {
            // Create a new ScheduleEntry with the input data
            ScheduleEntry entry = new ScheduleEntry(date, startTime, endTime, calculateDuration(startTime, endTime), name, status); // Add status

            // Add the entry to the schedule table
            scheduleTable.getItems().add(entry);
        }
    }

    // Helper method to calculate duration
    private String calculateDuration(LocalTime startTime, LocalTime endTime) {
        long durationHours = startTime.until(endTime, ChronoUnit.HOURS);
        long durationMinutes = startTime.until(endTime, ChronoUnit.MINUTES) % 60;
        return durationHours + " hours " + durationMinutes + " minutes";
    }
    private void generateSchedule() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null && !startDate.isAfter(endDate)) {
            // Clear existing table data
            scheduleTable.getItems().clear();

            // Generate data based on selected checkboxes
            List<ScheduleEntry> data = new ArrayList<>();
            if (scheduleCheckBox.isSelected()) {
                generateScheduleData(startDate, endDate, data);
            }
            if (absencesCheckBox.isSelected()) {
                generateAbsencesData(startDate, endDate, data);
            }
            if (holidaysCheckBox.isSelected()) {
                generateHolidaysData(startDate, endDate, data);
            }

            // Add data to the table
            scheduleTable.setItems(FXCollections.observableArrayList(data));
        } else {
            // Show error message if dates are not valid
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Dates");
            alert.setHeaderText(null);
            alert.setContentText("Please select valid start and end dates.");
            alert.showAndWait();
        }
    }

    private void generateScheduleData(LocalDate startDate, LocalDate endDate, List<ScheduleEntry> data) {
        // Connect to the database using DatabaseConnector
        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "SELECT staffName, startTime, endTime, scheduleDate FROM StaffInfo INNER JOIN StaffSchedule ON StaffInfo.staffScheduleID = StaffSchedule.scheduleID";
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                // Loop through the result set and populate the schedule data
                while (rs.next()) {
                    String employee = rs.getString("staffName");
                    LocalTime startTime = LocalTime.parse(rs.getString("startTime"));
                    LocalTime endTime = LocalTime.parse(rs.getString("endTime"));
                    LocalDate scheduleDate = LocalDate.parse(rs.getString("scheduleDate"));

                    // Check if the schedule date is within the specified range
                    if (!scheduleDate.isBefore(startDate) && !scheduleDate.isAfter(endDate)) {
                        // Calculate duration
                        long durationHours = startTime.until(endTime, ChronoUnit.HOURS);
                        long durationMinutes = startTime.until(endTime, ChronoUnit.MINUTES) % 60;
                        String duration = durationHours + " hours " + durationMinutes + " minutes";

                        String status = "ON"; // Default status

                        // Add entry to data list
                        data.add(new ScheduleEntry(scheduleDate, startTime, endTime, duration, employee, status));
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error executing query: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    private void generateAbsencesData(LocalDate startDate, LocalDate endDate, List<ScheduleEntry> data) {
        // Connect to the SQLite database
        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "SELECT StaffInfo.staffName, StaffHoliday.startDate, StaffHoliday.endDate FROM StaffHoliday " +
                    "INNER JOIN StaffHoliday_StaffInfo ON StaffHoliday.holidayID = StaffHoliday_StaffInfo.holidayID " +
                    "INNER JOIN StaffInfo ON StaffHoliday_StaffInfo.staffID = StaffInfo.staffID " +
                    "WHERE (StaffHoliday.startDate BETWEEN ? AND ?) OR (StaffHoliday.endDate BETWEEN ? AND ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, startDate.toString());
            pstmt.setString(2, endDate.toString());
            pstmt.setString(3, startDate.toString());
            pstmt.setString(4, endDate.toString());
            ResultSet rs = pstmt.executeQuery();

            // Loop through the result set and populate the absences data
            while (rs.next()) {
                String employee = rs.getString("staffName");
                LocalDate absenceStartDate = LocalDate.parse(rs.getString("startDate"));
                LocalDate absenceEndDate = LocalDate.parse(rs.getString("endDate"));

                // Check if the absence period intersects with the specified date range
                LocalDate rangeStart = startDate.isAfter(absenceStartDate) ? startDate : absenceStartDate;
                LocalDate rangeEnd = endDate.isBefore(absenceEndDate) ? endDate : absenceEndDate;

                // Generate entries for each day of absence within the date range
                LocalDate date = rangeStart;
                while (!date.isAfter(rangeEnd)) {
                    // Add entry to data list
                    data.add(new ScheduleEntry(date, null, null, "Absent", employee, "OFF"));
                    date = date.plusDays(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error accessing database: " + e.getMessage());
        }
    }

    private void generateHolidaysData(LocalDate startDate, LocalDate endDate, List<ScheduleEntry> data) {
        // Connect to the SQLite database
        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "SELECT * FROM StaffHoliday WHERE startDate BETWEEN ? AND ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, startDate.toString());
            pstmt.setString(2, endDate.toString());
            ResultSet rs = pstmt.executeQuery();

            // Loop through the result set and populate the holidays data
            while (rs.next()) {
                LocalDate holidayDate = LocalDate.parse(rs.getString("startDate"));

                // Add entry to data list
                data.add(new ScheduleEntry(holidayDate, null, null, "Holiday", "N/A", "OFF"));
            }
        } catch (SQLException e) {
            System.out.println("Error accessing database: " + e.getMessage());
        }
    }

    private Button createDeleteScheduleButton() {
        Button deleteScheduleButton = new Button("Delete Schedule");
        deleteScheduleButton.setOnAction(event -> {
            LocalDate selectedDate = dateField.getValue();
            String selectedName = nameField.getText();
            if (selectedDate != null && !selectedName.isEmpty()) {
                // Call method to delete schedule for the specified date and employee
                deleteSchedule(selectedDate, selectedName);
            } else {
                // Show error message if date or name is not selected
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select both a date and an employee name.");
                alert.showAndWait();
            }
        });
        return deleteScheduleButton;
    }

    private void deleteSchedule(LocalDate date, String name) {
        // Delete the schedule from the database
        try (Connection conn = DatabaseConnector.getConnection()) {
            String deleteQuery = "DELETE FROM StaffSchedule WHERE scheduleDate = ? AND employeeName = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setString(2, name);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                // Schedule deleted successfully from the database
                System.out.println("Schedule deleted successfully.");
            } else {
                // No schedule found for the specified date and employee
                System.out.println("No schedule found for the specified date and employee.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting schedule from the database: " + e.getMessage());
            // Handle the error appropriately (e.g., show an alert to the user)
        }

        // Remove the schedule from the UI
        Iterator<ScheduleEntry> iterator = scheduleTable.getItems().iterator();
        while (iterator.hasNext()) {
            ScheduleEntry entry = iterator.next();
            if (entry.getDate().equals(date) && entry.getEmployeeName().equals(name)) {
                iterator.remove();
                break;
            }
        }
    }

    // Define method to switch to staff performance view
    private void switchToStaffPerformanceView() {
        // Clear existing content
        staffSchedulingMainContent.getChildren().clear();

        // Create table for staff performance
        TableView<StaffPerformanceEntry> staffPerformanceTable = new TableView<>();
        staffPerformanceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StaffPerformanceEntry, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        TableColumn<StaffPerformanceEntry, String> employeeColumn = new TableColumn<>("Employee Name");
        employeeColumn.setCellValueFactory(cellData -> cellData.getValue().employeeNameProperty());

        TableColumn<StaffPerformanceEntry, String> performanceColumn = new TableColumn<>("Tables assisted");
        performanceColumn.setCellValueFactory(cellData -> cellData.getValue().performanceRatingProperty());

        staffPerformanceTable.getColumns().addAll(dateColumn, employeeColumn, performanceColumn);

        // Create buttons to select date and employee name
        DatePicker selectDatePicker = new DatePicker();
        TextField selectEmployeeName = new TextField();
        Button showPerformanceButton = new Button("Show Performance");
        showPerformanceButton.setOnAction(event -> {
            LocalDate selectedDate = selectDatePicker.getValue();
            String selectedName = selectEmployeeName.getText();
            if (selectedDate != null && !selectedName.isEmpty()) {
                // Call method to fetch performance data based on selected date and employee name
                fetchPerformanceData(selectedDate, selectedName, staffPerformanceTable);
            } else {
                // Show error message if date or name is not selected
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select both date and employee name.");
                alert.showAndWait();
            }
        });
        // Button to add or modify performance
        Button modifyPerformanceButton = new Button("Modify Performance");
        modifyPerformanceButton.setOnAction(event -> {
            LocalDate selectedDate = selectDatePicker.getValue();
            String selectedName = selectEmployeeName.getText();
            String newPerformanceRating = null; // Retrieve the new performance rating from your UI component, e.g., textField.getText();

            if (selectedDate != null && !selectedName.isEmpty() && !newPerformanceRating.isEmpty()) {
                StaffPerformanceEntry performanceEntry = new StaffPerformanceEntry(selectedDate, selectedName, newPerformanceRating);
                // Call method to update performance in the database
                updatePerformance(performanceEntry);
                // Refresh performance data in the UI
                fetchPerformanceData(selectedDate, selectedName, staffPerformanceTable);
            } else {
                // Show error message if any required field is empty
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all the fields.");
                alert.showAndWait();
            }
        });

        // Button to delete performance entry
        Button deletePerformanceButton = new Button("Delete Performance");
        deletePerformanceButton.setOnAction(event -> {
            LocalDate selectedDate = selectDatePicker.getValue();
            String selectedName = selectEmployeeName.getText();
            if (selectedDate != null && !selectedName.isEmpty()) {
                // Call method to delete performance entry for the specified date and employee
                deletePerformance(selectedDate, selectedName);
            } else {
                // Show error message if date or name is not selected
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select both a date and an employee name.");
                alert.showAndWait();
            }
        });
        staffSchedulingMainContent.getChildren().addAll(
                staffScheduleButton,
                selectDatePicker,
                new Text("Employee Name"),
                selectEmployeeName,
                showPerformanceButton,
                modifyPerformanceButton,
                deletePerformanceButton,
                staffPerformanceTable
        );
        selectEmployeeName.setPromptText("Enter Employee Name");
        setTextColor(staffSchedulingMainContent);
    }
    // Define method to switch back to scheduling view
    private void switchToSchedulingView() {
        // Clear existing content
        staffSchedulingMainContent.getChildren().clear();

        // Re-add scheduling components
        staffSchedulingMainContent.getChildren().addAll(
                staffPerformanceButton,
                new Text("Start Date:"),
                startDatePicker,
                new Text("End Date:"),
                endDatePicker,
                new Text("Select Data Types:"),
                scheduleCheckBox,
                absencesCheckBox,
                holidaysCheckBox,
                generateScheduleButton,
                scheduleTable
        );
        staffSchedulingMainContent.getChildren().addAll(
                new Text("Name:"),
                nameField,
                new Text("Status:"),
                statusComboBox,
                new Text("Date:"),
                dateField,
                new Text("Start Time:"),
                startTimeField,
                new Text("End Time:"),
                endTimeField,
                addButton,
                createDeleteScheduleButton()
        );
    }
    // Method to fetch performance data based on selected date and employee name
    public void fetchPerformanceData(LocalDate selectedDate, String selectedName, TableView<StaffPerformanceEntry> performanceTable) {
        // Fetch performance data from the database
        List<StaffPerformanceEntry> performanceData = getPerformanceDataFromDB(selectedDate, selectedName);

        // Clear existing data in the table
        performanceTable.getItems().clear();

        // Add fetched data to the table
        performanceTable.getItems().addAll(performanceData);
    }

    // Method to retrieve performance data from the database
    private List<StaffPerformanceEntry> getPerformanceDataFromDB(LocalDate selectedDate, String selectedName) {
        List<StaffPerformanceEntry> performanceData = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection()) {
            String query;
            if (selectedName == null || selectedName.isEmpty()) {
                // If no employee name is provided, fetch performance data for all employees who worked on the selected date
                query = "SELECT * FROM StaffPerformance WHERE performanceDate = ?";
            } else {
                // Fetch performance data for the specified employee who worked on the selected date
                query = "SELECT * FROM StaffPerformance WHERE performanceDate = ? AND employeeName = ?";
            }
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, selectedDate.toString());
            if (selectedName != null && !selectedName.isEmpty()) {
                pstmt.setString(2, selectedName);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString("performanceDate"));
                String employeeName = rs.getString("employeeName");
                String performanceRating = rs.getString("performanceRating");
                StaffPerformanceEntry entry = new StaffPerformanceEntry(date, employeeName, performanceRating);
                performanceData.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions (e.g., show an alert to the user)
        }
        return performanceData;
    }

    // Method to update performance entry in the database
    public void updatePerformance(StaffPerformanceEntry performanceEntry) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "UPDATE StaffPerformance SET performanceRating = ? WHERE performanceDate = ? AND employeeName = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, performanceEntry.getPerformanceRating());
            pstmt.setString(2, performanceEntry.getDate().toString());
            pstmt.setString(3, performanceEntry.getEmployeeName());
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                // Performance entry updated successfully in the database
                System.out.println("Performance entry updated successfully.");
            } else {
                // No performance entry found for the specified date and employee
                System.out.println("No performance entry found for the specified date and employee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions (e.g., show an alert to the user)
        }
    }

    // Method to delete performance entry for the specified date and employee
    public void deletePerformance(LocalDate selectedDate, String selectedName) {
        // Implement the logic to delete performance entry for the specified date and employee
        // This method will remove the performance entry from the database
        try (Connection conn = DatabaseConnector.getConnection()) {
            String query;
            if (selectedName == null || selectedName.isEmpty()) {
                // If no employee name is provided, delete all performance entries for the selected date
                query = "DELETE FROM StaffPerformance WHERE performanceDate = ?";
            } else {
                // Delete performance entry for the specified date and employee
                query = "DELETE FROM StaffPerformance WHERE performanceDate = ? AND employeeName = ?";
            }
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, selectedDate.toString());
            if (selectedName != null && !selectedName.isEmpty()) {
                pstmt.setString(2, selectedName);
            }
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                // Performance entry deleted successfully from the database
                System.out.println("Performance entry deleted successfully.");
            } else {
                // No performance entry found for the specified date and employee
                System.out.println("No performance entry found for the specified date and employee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions (e.g., show an alert to the user)
        }
    }
    // Method to set white color for all text nodes
    private void setTextColor(VBox vbox) {
        for (javafx.scene.Node node : vbox.getChildren()) {
            if (node instanceof Text) {
                ((Text) node).setFill(Color.WHITE);
            } else if (node instanceof VBox) {
                setTextColor((VBox) node);
            }
        }
    }
}


//    Staff Scheduling: Manage staff schedules including shifts, holidays, and absences to ensure adequate staffing.
//        Performance Tracking: Monitor staff performance and productivity metrics.