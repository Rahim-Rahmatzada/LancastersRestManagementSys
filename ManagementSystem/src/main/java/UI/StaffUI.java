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
import java.util.List;

public class StaffUI extends BaseUI {
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private TableView<ScheduleEntry> scheduleTable;
    private CheckBox scheduleCheckBox;
    private CheckBox absencesCheckBox;
    private CheckBox holidaysCheckBox;
    private TextField nameField;
    private DatePicker dateField;
    private TextField startTimeField;
    private TextField endTimeField;
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

        setMainContent(staffSchedulingMainContent);
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

        // Check if the employee already has a shift on the selected date
        boolean existingEntry = false;
        for (ScheduleEntry entry : scheduleTable.getItems()) {
            if (entry.getDate().equals(date) && entry.getEmployeeName().equals(name)) {
                // Update existing entry
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
        // Generate schedule data (dummy data for demonstration)
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            for (String employee : new String[]{"Alice", "Bob", "Charlie", "David"}) {
                LocalTime startTime = LocalTime.of(9, 0); // Example start time
                LocalTime endTime = LocalTime.of(17, 0); // Example end time

                // Calculate duration
                long durationHours = startTime.until(endTime, ChronoUnit.HOURS);
                long durationMinutes = startTime.until(endTime, ChronoUnit.MINUTES) % 60;
                String duration = durationHours + " hours " + durationMinutes + " minutes";

                String status = "ON"; // Default status

                // Add entry to data list
                data.add(new ScheduleEntry(date, startTime, endTime, duration, employee,status));
            }
            date = date.plusDays(1);
        }
    }

    private void generateAbsencesData(LocalDate startDate, LocalDate endDate, List<ScheduleEntry> data) {
        // Generate absences data (dummy data for demonstration)
        // For simplicity, let's assume there are no absences in this period
    }

    private void generateHolidaysData(LocalDate startDate, LocalDate endDate, List<ScheduleEntry> data) {
        // Generate holidays data (dummy data for demonstration)
        // For simplicity, let's assume there are no holidays in this period
    }


    private static class ScheduleEntry {
        private final LocalDate date;
        private final SimpleStringProperty employeeName;
        private final SimpleObjectProperty<LocalTime> startTime;
        private final SimpleObjectProperty<LocalTime> endTime;
        private final SimpleStringProperty duration;
        private final SimpleStringProperty status;
        private ComboBox<String> statusComboBox;

        public ScheduleEntry(LocalDate date, LocalTime startTime, LocalTime endTime, String duration, String employeeName, String status) {
            this.date = date;
            this.startTime = new SimpleObjectProperty<>(startTime);
            this.endTime = new SimpleObjectProperty<>(endTime);
            this.duration = new SimpleStringProperty(duration);
            this.employeeName = new SimpleStringProperty(employeeName);
            this.status = new SimpleStringProperty(status);
        }

        public LocalDate getDate() {
            return date;
        }

        public LocalTime getStartTime() {
            return startTime.get();
        }

        public void setStartTime(LocalTime startTime) {
            this.startTime.set(startTime);
        }

        public LocalTime getEndTime() {
            return endTime.get();
        }

        public void setEndTime(LocalTime endTime) {
            this.endTime.set(endTime);
        }

        public String getDuration() {
            return duration.get();
        }

        public void setDuration(String duration) {
            this.duration.set(duration);
        }
        public String getStatus() {
            return status.get();
        }

        public void setStatus(String status) {
            this.status.set(status);
        }

        public String getEmployeeName() {
            return employeeName.get();
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName.set(employeeName);
        }
        public SimpleStringProperty statusProperty() {
            return status;
        }

        // Property methods for TableView
        public javafx.beans.property.ObjectProperty<LocalDate> dateProperty() {
            return new javafx.beans.property.SimpleObjectProperty<>(date);
        }

        public javafx.beans.property.ObjectProperty<LocalTime> startTimeProperty() {
            return startTime;
        }

        public javafx.beans.property.ObjectProperty<LocalTime> endTimeProperty() {
            return endTime;
        }

        public javafx.beans.property.SimpleStringProperty durationProperty() {
            return duration;
        }

        public SimpleStringProperty employeeNameProperty() {
            return employeeName;
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


    public class StaffPerformanceEntry {
        private final LocalDate date;
        private final SimpleStringProperty employeeName;
        private final SimpleStringProperty performanceRating;

        public StaffPerformanceEntry(LocalDate date, String employeeName, String performanceRating) {
            this.date = date;
            this.employeeName = new SimpleStringProperty(employeeName);
            this.performanceRating = new SimpleStringProperty(performanceRating);
        }

        public LocalDate getDate() {
            return date;
        }

        public String getEmployeeName() {
            return employeeName.get();
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName.set(employeeName);
        }

        public String getPerformanceRating() {
            return performanceRating.get();
        }

        public void setPerformanceRating(String performanceRating) {
            this.performanceRating.set(performanceRating);
        }

        // Property methods for TableView
        public javafx.beans.property.ObjectProperty<LocalDate> dateProperty() {
            return new javafx.beans.property.SimpleObjectProperty<>(date);
        }

        public SimpleStringProperty employeeNameProperty() {
            return employeeName;
        }

        public SimpleStringProperty performanceRatingProperty() {
            return performanceRating;
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

        TableColumn<StaffPerformanceEntry, String> performanceColumn = new TableColumn<>("Performance Rating");
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
        Button addModifyPerformanceButton = new Button("Add/Modify Performance");
        addModifyPerformanceButton.setOnAction(event -> {
            LocalDate selectedDate = selectDatePicker.getValue();
            String selectedName = selectEmployeeName.getText();
            if (selectedDate != null) {
                // Call method to add or modify performance
                addOrModifyPerformance(selectedDate, selectedName);
            } else {
                // Show error message if date is not selected
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select a date.");
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
                selectEmployeeName,
                showPerformanceButton,
                addModifyPerformanceButton,
                deletePerformanceButton,
                staffPerformanceTable
        );
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
    private void fetchPerformanceData(LocalDate selectedDate, String selectedName, TableView<StaffPerformanceEntry> performanceTable) {
        // Fetch performance data based on selected date and employee name
        // You need to implement the logic to retrieve the performance data from your database or any other data source
        // Here, I'm assuming you have a method to retrieve performance data based on date and employee name
        List<StaffPerformanceEntry> performanceData = getPerformanceData(selectedDate, selectedName);

        // Clear existing data in the table
        performanceTable.getItems().clear();

        // Add fetched data to the table
        performanceTable.getItems().addAll(performanceData);
    }

    // Method to retrieve performance data based on date and employee name
    private List<StaffPerformanceEntry> getPerformanceData(LocalDate selectedDate, String selectedName) {
        // Implement your logic to fetch performance data from the database or any other data source
        // This is just a placeholder method; you need to replace it with your actual implementation
        // Here, I'm assuming you have a method to fetch performance data from the database
        // You can use JDBC or any ORM framework to interact with the database

        // If no employee name is provided, fetch performance data for all employees who worked on the selected date
        if (selectedName == null || selectedName.isEmpty()) {
            // Implement your logic to fetch performance data for all employees who worked on the selected date
            // Populate performanceData with the fetched data
        } else {
            // Implement your logic to fetch performance data for the specified employee who worked on the selected date
            // Populate performanceData with the fetched data
        }

        List<StaffPerformanceEntry> performanceData = new ArrayList<>();
        // Populate performanceData with the fetched data
        return performanceData;
    }
    // Method to add or modify performance
    private void addOrModifyPerformance(LocalDate selectedDate, String selectedName) {
        // Implement the logic to add or modify performance for the specified date and employee
        // This method will open a new window or dialog to input or modify performance details
        // You need to implement this method based on your application's requirements
        // For example, you can create a dialog box where the user can input or modify performance details
        // Once the details are entered or modified, update the performance entry in the database or data source
    }
    // Method to delete schedule for the specified date and employee
    private void deleteSchedule(LocalDate selectedDate, String selectedName) {
        // Implement the logic to delete schedule entry for the specified date and employee
        // This method will remove the schedule entry from the database or data source
        // Once the entry is deleted, refresh the schedule table to reflect the changes
    }
    // Method to delete performance entry for the specified date and employee
    private void deletePerformance(LocalDate selectedDate, String selectedName) {
        // Implement the logic to delete performance entry for the specified date and employee
        // This method will remove the performance entry from the database or data source
        // Once the entry is deleted, refresh the performance table to reflect the changes
    }
}


//    Staff Scheduling: Manage staff schedules including shifts, holidays, and absences to ensure adequate staffing.
//        Performance Tracking: Monitor staff performance and productivity metrics.