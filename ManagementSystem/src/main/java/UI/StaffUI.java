package UI;

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

    public StaffUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Staff");
        setTopText("Staff Overview");

        // Set the main content for the StaffSchedulingUI.
        VBox staffSchedulingMainContent = new VBox();
        staffSchedulingMainContent.setPadding(new Insets(10));
        staffSchedulingMainContent.setSpacing(10);

        // Create date pickers for start and end dates
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();

        // Create checkboxes for selecting data types
        scheduleCheckBox = new CheckBox("Schedule");
        absencesCheckBox = new CheckBox("Absences");
        holidaysCheckBox = new CheckBox("Holidays");

        // Create a button to generate the schedule
        Button generateScheduleButton = new Button("Generate Schedule");
        generateScheduleButton.setOnAction(event -> generateSchedule());

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

        scheduleTable.getColumns().addAll(dateColumn, startColumn, endColumn, durationColumn, employeeColumn);

        // Add components to the main content VBox
        staffSchedulingMainContent.getChildren().addAll(
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

        setMainContent(staffSchedulingMainContent);
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

                // Add entry to data list
                data.add(new ScheduleEntry(date, startTime, endTime, duration, employee));
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
        private final LocalTime startTime;
        private final LocalTime endTime;
        private final String duration;
        private final SimpleStringProperty employeeName;

        public ScheduleEntry(LocalDate date, LocalTime startTime, LocalTime endTime, String duration, String employeeName) {
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
            this.duration = duration;
            this.employeeName = new SimpleStringProperty(employeeName);
        }

        public LocalDate getDate() {
            return date;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public String getDuration() {
            return duration;
        }
        // Property methods for TableView
        public javafx.beans.property.ObjectProperty<LocalDate> dateProperty() {
            return new javafx.beans.property.SimpleObjectProperty<>(date);
        }

        public javafx.beans.property.ObjectProperty<LocalTime> startTimeProperty() {
            return new javafx.beans.property.SimpleObjectProperty<>(startTime);
        }

        public javafx.beans.property.ObjectProperty<LocalTime> endTimeProperty() {
            return new javafx.beans.property.SimpleObjectProperty<>(endTime);
        }

        public javafx.beans.property.SimpleStringProperty durationProperty() {
            return new javafx.beans.property.SimpleStringProperty(duration);
        }

        public SimpleStringProperty employeeNameProperty() {
            return employeeName;
        }
    }
}


//    Staff Scheduling: Manage staff schedules including shifts, holidays, and absences to ensure adequate staffing.
//        Performance Tracking: Monitor staff performance and productivity metrics.