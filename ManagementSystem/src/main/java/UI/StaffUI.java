package UI;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import DatabaseConnections.AdminDatabaseConnector;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import model.ScheduleForUI;
import model.StaffHolidayForUI;
import model.StaffInfoForUI;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class StaffUI extends BaseUI {
    private DatePicker datePicker;
    private Button getScheduleButton;
    private Button viewHolidaysButton;
    private Button modifyScheduleButton;


    private TableView<StaffInfoForUI> staffTableView;
    private TableView<StaffHolidayForUI> holidayTableView;
    private TableView<ScheduleForUI> scheduleTableView;
    private ScheduleForUI schedule;


    public StaffUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Staff");
        setTopText("Staff Overview");
        initializeUI();
    }

    private void initializeUI() {
        VBox mainContent = new VBox();
        mainContent.setPadding(new Insets(20));
        mainContent.setSpacing(10);

        datePicker = new DatePicker();
        datePicker.setPromptText("Enter Date");
        getScheduleButton = new Button("Get Schedule");
        getScheduleButton.setOnAction(e -> loadSchedule());
        viewHolidaysButton = new Button("View Staff Holidays");
        viewHolidaysButton.setOnAction(e -> viewStaffHolidays());
        modifyScheduleButton = new Button("Modify Staff Schedule");
        modifyScheduleButton.setOnAction(e -> modifyStaffSchedule());

        HBox topControls = new HBox(10, datePicker, getScheduleButton, viewHolidaysButton, modifyScheduleButton);

        staffTableView = createScheduleTableView();

        mainContent.getChildren().addAll(topControls, staffTableView);
        setMainContent(mainContent);
    }
    private void updateDatabase(ScheduleForUI schedule) {
        try (Connection conn = AdminDatabaseConnector.getConnection()) {
            String query = "UPDATE YourTableName SET Date = ?, StartTime = ?, EndTime = ? WHERE ScheduleID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setDate(1, java.sql.Date.valueOf(schedule.getDate()));
            pstmt.setTime(2, schedule.getStartTime());
            pstmt.setTime(3, schedule.getEndTime());
            pstmt.setInt(4, schedule.getScheduleID());

            int updatedRows = pstmt.executeUpdate();

            if (updatedRows > 0) {
                System.out.println("Database updated successfully");
            } else {
                System.out.println("Failed to update database");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifyStaffSchedule() {
        VBox mainContent = (VBox) getMainContent();
        mainContent.getChildren().clear();

        VBox staffScheduleContent = new VBox();
        staffScheduleContent.setSpacing(10);


        // Create a table view to display all staff
        staffTableView = createStaffTableView();
        staffTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !staffTableView.getSelectionModel().isEmpty()) {
                StaffInfoForUI selectedStaff = staffTableView.getSelectionModel().getSelectedItem();
                int staffID = selectedStaff.getStaffID();
                Button backButton = new Button("Back");
                backButton.setOnAction(e -> backToMainView());
                HBox hBox = new HBox(10);
                TextField textField1 = new TextField();
                textField1.setPromptText("StaffID:ScheduleID");
                TextField textField2 = new TextField();
                textField2.setPromptText("StaffID:Schedule");
                Button addSchedule = new Button("Add Schedule");
                addSchedule.setOnAction(e -> addScheduleToDatabase(textField1.getText()));
                Button delSchedule = new Button("Delete Schedule");
                delSchedule.setOnAction(e -> deleteScheduleFromDatabase(textField2.getText()));
                hBox.getChildren().addAll(textField1, addSchedule, textField2, delSchedule);
                scheduleTableView = createScheduleTableView(staffID);
                staffScheduleContent.getChildren().setAll(backButton, staffTableView, scheduleTableView, hBox);
                staffTableView.setStyle("-fx-background-color: #1A1A1A;");
                scheduleTableView.setStyle("-fx-background-color: #1A1A1A;");
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> backToMainView());

        staffScheduleContent.getChildren().addAll(backButton, staffTableView);
        mainContent.getChildren().add(staffScheduleContent);
    }

    private TableView<StaffInfoForUI> createStaffTableView() {
        TableView<StaffInfoForUI> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StaffInfoForUI, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(param -> param.getValue().getStaffIDProperty().asObject());

        TableColumn<StaffInfoForUI, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(param -> param.getValue().getNameProperty());

        TableColumn<StaffInfoForUI, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(param -> param.getValue().getRoleProperty());

        idColumn.setStyle("-fx-text-fill: white;");
        nameColumn.setStyle("-fx-text-fill: white;");
        roleColumn.setStyle("-fx-text-fill: white;");

        tableView.getColumns().addAll(idColumn, nameColumn, roleColumn);

        // Set the cell factory to style table cells
        tableView.setRowFactory(tv -> {
            TableRow<StaffInfoForUI> row = new TableRow<>();
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

        // Load staff data from the database
        List<StaffInfoForUI> staff = getStaffFromDatabase();
        tableView.getItems().setAll(staff);

        return tableView;
    }

    private List<StaffInfoForUI> getStaffFromDatabase() {
        List<StaffInfoForUI> staff = new ArrayList<>();

        try (Connection conn = AdminDatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT staffID, staffName, staffRole FROM StaffInfo")) {

            while (rs.next()) {
                int staffID = rs.getInt("staffID");
                String name = rs.getString("staffName");
                String role = rs.getString("staffRole");

                StaffInfoForUI staffInfo = new StaffInfoForUI(staffID, name, role);
                staff.add(staffInfo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return staff;
    }

    private List<ScheduleForUI> getScheduleFromDatabase(int staffID) {
        List<ScheduleForUI> schedules = new ArrayList<>();

        try (Connection conn = AdminDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT ss.scheduleID, ss.dateWorking, ss.shiftStartingTime, ss.shiftEndingTime, ss.duration " +
                             "FROM StaffSchedule ss " +
                             "JOIN StaffSchedule_StaffInfo ssi ON ss.scheduleID = ssi.scheduleID " +
                             "WHERE ssi.staffID = ?")) {

            stmt.setInt(1, staffID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int scheduleID = rs.getInt("scheduleID");
                LocalDate date = rs.getDate("dateWorking").toLocalDate();
                Time startTime = rs.getTime("shiftStartingTime");
                Time endTime = rs.getTime("shiftEndingTime");
                String duration = rs.getString("duration");

                ScheduleForUI schedule = new ScheduleForUI(scheduleID, date, startTime, endTime, duration);
                schedules.add(schedule);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schedules;
    }


    private void backToMainView() {
        VBox mainContent = (VBox) getMainContent();
        mainContent.getChildren().clear();
        initializeUI();
    }

    private void viewStaffHolidays() {
        VBox mainContent = (VBox) getMainContent();
        mainContent.getChildren().clear();

        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        startDatePicker.setPromptText("Starting Date");
        endDatePicker.setPromptText("Ending Date");

        Button getHolidaysButton = new Button("Get Holidays");
        getHolidaysButton.setOnAction(e -> loadHolidays(startDatePicker.getValue(), endDatePicker.getValue()));

        Button backToScheduleButton = new Button("Back to Schedule");
        backToScheduleButton.setOnAction(e -> backToScheduleView());

        HBox holidayControls = new HBox(10, startDatePicker, endDatePicker, getHolidaysButton, backToScheduleButton);

        holidayTableView = createHolidayTableView();

        mainContent.getChildren().addAll(holidayControls, holidayTableView);
    }

    private void backToScheduleView() {
        VBox mainContent = (VBox) getMainContent();
        mainContent.getChildren().clear();

        datePicker = new DatePicker();
        datePicker.setPromptText("Enter Date");
        getScheduleButton = new Button("Get Schedule");
        getScheduleButton.setOnAction(e -> loadSchedule());
        viewHolidaysButton = new Button("View Staff Holidays");
        viewHolidaysButton.setOnAction(e -> viewStaffHolidays());
        modifyScheduleButton = new Button("Modify Staff Schedule");
        modifyScheduleButton.setOnAction(e -> modifyStaffSchedule());

        HBox topControls = new HBox(10, datePicker, getScheduleButton, viewHolidaysButton, modifyScheduleButton);

        staffTableView = createScheduleTableView();

        mainContent.getChildren().addAll(topControls, staffTableView);
    }

    private TableView<StaffHolidayForUI> createHolidayTableView() {
        TableView<StaffHolidayForUI> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #1A1A1A;");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StaffHolidayForUI, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(param -> param.getValue().getNameProperty());

        TableColumn<StaffHolidayForUI, LocalDate> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(param -> param.getValue().getStartDateProperty());

        TableColumn<StaffHolidayForUI, LocalDate> endDateColumn = new TableColumn<>("End Date");
        endDateColumn.setCellValueFactory(param -> param.getValue().getEndDateProperty());


        tableView.getColumns().addAll(nameColumn, startDateColumn, endDateColumn);

        // Set cell factory to style table cells
        nameColumn.setStyle("-fx-text-fill: white;");
        startDateColumn.setStyle("-fx-text-fill: white;");
        endDateColumn.setStyle("-fx-text-fill: white;");

        // Set the cell factory to style table cells (same as createScheduleTableView)
        tableView.setRowFactory(tv -> {
            TableRow<StaffHolidayForUI> row = new TableRow<>();
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

    private void loadHolidays(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            showAlert("Invalid Date Range", "Please select both start and end dates.");
            return;
        }

        if (startDate.isAfter(endDate)) {
            showAlert("Invalid Date Range", "Start date cannot be after the end date.");
            return;
        }

        List<StaffHolidayForUI> holidays = getStaffHolidaysFromDatabase(startDate, endDate);
        holidayTableView.getItems().setAll(holidays);
    }

    private List<StaffHolidayForUI> getStaffHolidaysFromDatabase(LocalDate startDate, LocalDate endDate) {
        List<StaffHolidayForUI> holidays = new ArrayList<>();

        try (Connection conn = AdminDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT si.staffName, sh.startDate, sh.endDate " +
                             "FROM StaffInfo si " +
                             "JOIN StaffHoliday_StaffInfo shsi ON si.staffID = shsi.staffID " +
                             "JOIN StaffHoliday sh ON shsi.holidayID = sh.holidayID " +
                             "WHERE sh.startDate BETWEEN ? AND ? OR sh.endDate BETWEEN ? AND ?")) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            stmt.setDate(3, Date.valueOf(startDate));
            stmt.setDate(4, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("staffName");
                    LocalDate startHolidayDate = rs.getDate("startDate").toLocalDate();
                    LocalDate endHolidayDate = rs.getDate("endDate").toLocalDate();
                    long duration = ChronoUnit.DAYS.between(startHolidayDate, endHolidayDate) + 1;

                    StaffHolidayForUI holiday = new StaffHolidayForUI(name, startHolidayDate, endHolidayDate, duration);
                    holidays.add(holiday);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return holidays;
    }

    private TableView<ScheduleForUI> createScheduleTableView(int staffID) {
        TableView<ScheduleForUI> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setStyle("-fx-background-color: #1A1A1A;");


        TableColumn<ScheduleForUI, Integer> scheduleIDColumn = new TableColumn<>("Schedule ID");
        scheduleIDColumn.setCellValueFactory(param -> param.getValue().getScheduleIDProperty().asObject());

        TableColumn<ScheduleForUI, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(param -> param.getValue().getDateProperty());
        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        dateColumn.setOnEditCommit(event -> {
            ScheduleForUI schedule = event.getRowValue();
            LocalDate newValue = event.getNewValue();
            schedule.setDate(newValue);
        });

        TableColumn<ScheduleForUI, LocalTime> startTimeColumn = new TableColumn<>("Start Time");
        startTimeColumn.setCellValueFactory(param -> {
            Time startTime = param.getValue().getStartTime();
            return new SimpleObjectProperty<>(startTime.toLocalTime());
        });
        startTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter()));
        startTimeColumn.setOnEditCommit(event -> {
            ScheduleForUI schedule = event.getRowValue();
            LocalTime newValue = event.getNewValue();
            schedule.setStartTime(Time.valueOf(newValue));
        });

        TableColumn<ScheduleForUI, LocalTime> endTimeColumn = new TableColumn<>("End Time");
        endTimeColumn.setCellValueFactory(param -> {
            Time endTime = param.getValue().getEndTime();
            return new SimpleObjectProperty<>(endTime.toLocalTime());
        });
        endTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter()));
        endTimeColumn.setOnEditCommit(event -> {
            ScheduleForUI schedule = event.getRowValue();
            LocalTime newValue = event.getNewValue();
            schedule.setEndTime(Time.valueOf(newValue));
        });

        //On edit commit for Date, startTime and endTime columns
        dateColumn.setOnEditCommit(event -> {
            ScheduleForUI schedule = event.getRowValue();
            LocalDate newValue = event.getNewValue();
            schedule.setDate(newValue);
            updateDatabase(schedule);
        });

        startTimeColumn.setOnEditCommit(event -> {
            ScheduleForUI schedule = event.getRowValue();
            LocalTime newValue = event.getNewValue();
            schedule.setStartTime(Time.valueOf(newValue));
            updateDatabase(schedule);
        });

        endTimeColumn.setOnEditCommit(event -> {
            ScheduleForUI schedule = event.getRowValue();
            LocalTime newValue = event.getNewValue();
            schedule.setEndTime(Time.valueOf(newValue));
            updateDatabase(schedule);
        });

        TableColumn<ScheduleForUI, String> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(param -> param.getValue().getDurationProperty());

        scheduleIDColumn.setStyle("-fx-text-fill: white;");
        dateColumn.setStyle("-fx-text-fill: white;");
        startTimeColumn.setStyle("-fx-text-fill: white;");
        endTimeColumn.setStyle("-fx-text-fill: white;");
        durationColumn.setStyle("-fx-text-fill: white;");

        tableView.getColumns().addAll(scheduleIDColumn, dateColumn, startTimeColumn, endTimeColumn, durationColumn);

        // Set the cell factory to style table cells
        tableView.setRowFactory(tv -> {
            TableRow<ScheduleForUI> row = new TableRow<>();
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

        // Load schedule data from the database
        List<ScheduleForUI> schedules = getScheduleFromDatabase(staffID);
        tableView.getItems().setAll(schedules);

        // Enable cell selection and editing
        tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
        tableView.setEditable(true);

        return tableView;
    }

    private TableView<StaffInfoForUI> createScheduleTableView() {
        TableView<StaffInfoForUI> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #1A1A1A;");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StaffInfoForUI, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(param -> param.getValue().getNameProperty());

        TableColumn<StaffInfoForUI, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(param -> param.getValue().getRoleProperty());

        TableColumn<StaffInfoForUI, String> shiftStartColumn = new TableColumn<>("Shift Start");
        shiftStartColumn.setCellValueFactory(param -> param.getValue().getShiftStartProperty());

        TableColumn<StaffInfoForUI, String> shiftEndColumn = new TableColumn<>("Shift End");
        shiftEndColumn.setCellValueFactory(param -> param.getValue().getShiftEndProperty());

        TableColumn<StaffInfoForUI, String> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(param -> param.getValue().getDurationProperty());

        nameColumn.setStyle("-fx-text-fill: white;");
        roleColumn.setStyle("-fx-text-fill: white;");
        shiftStartColumn.setStyle("-fx-text-fill: white;");
        shiftEndColumn.setStyle("-fx-text-fill: white;");
        durationColumn.setStyle("-fx-text-fill: white;");


        tableView.getColumns().addAll(nameColumn, roleColumn, shiftStartColumn, shiftEndColumn, durationColumn);

        // Set the cell factory to style table cells
        tableView.setRowFactory(tv -> {
            TableRow<StaffInfoForUI> row = new TableRow<>();
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

    private void loadSchedule() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            List<StaffInfoForUI> staffSchedule = getStaffScheduleFromDatabase(selectedDate);
            staffTableView.getItems().setAll(staffSchedule);
        } else {
            showAlert("No Date Selected", "Please select a date to view the staff schedule.");
        }
    }

    private List<StaffInfoForUI> getStaffScheduleFromDatabase(LocalDate date) {
        List<StaffInfoForUI> staffSchedule = new ArrayList<>();

        try (Connection conn = AdminDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT si.staffName, si.staffRole, ss.shiftStartingTime, ss.shiftEndingTime, ss.duration " +
                             "FROM StaffInfo si " +
                             "JOIN StaffSchedule_StaffInfo ssi ON si.staffID = ssi.staffID " +
                             "JOIN StaffSchedule ss ON ssi.scheduleID = ss.scheduleID " +
                             "WHERE ss.dateWorking = ?")) {

            stmt.setDate(1, Date.valueOf(date));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("staffName");
                    String role = rs.getString("staffRole");
                    Time shiftStart = rs.getTime("shiftStartingTime");
                    Time shiftEnd = rs.getTime("shiftEndingTime");
                    String duration = rs.getString("duration");

                    StaffInfoForUI staffInfo = new StaffInfoForUI(name, role, shiftStart.toString(), shiftEnd.toString(), duration);
                    staffSchedule.add(staffInfo);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return staffSchedule;
    }
    // adds a schedule to the database and stable
    private void addScheduleToDatabase(String input) {
        try (Connection conn = AdminDatabaseConnector.getConnection()) {
            String[] parts = input.split(":");
            if (parts.length != 2) {
                showAlert("Invalid Input", "Please enter StaffID:ScheduleID");
                return;
            }

            int staffID;
            int scheduleID;
            try {
                staffID = Integer.parseInt(parts[0]);
                scheduleID = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter valid StaffID and ScheduleID");
                return;
            }

            String query = "INSERT INTO StaffSchedule_StaffInfo (StaffID, ScheduleID) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, staffID);
            pstmt.setInt(2, scheduleID);

            int updatedRows = pstmt.executeUpdate();

            if (updatedRows > 0) {
                System.out.println("Schedule added to database successfully");
            } else {
                System.out.println("Failed to add schedule to database");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Method to delete schedule from database
    private void deleteScheduleFromDatabase(String input) {
        try (Connection conn = AdminDatabaseConnector.getConnection()) {
            String[] parts = input.split(":");
            if (parts.length != 2) {
                showAlert("Invalid Input", "Please enter StaffID:ScheduleID");
                return;
            }

            int staffID;
            int scheduleID;
            try {
                staffID = Integer.parseInt(parts[0]);
                scheduleID = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter valid StaffID and ScheduleID");
                return;
            }

            String query = "DELETE FROM StaffSchedule_StaffInfo WHERE StaffID = ? AND ScheduleID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, staffID);
            pstmt.setInt(2, scheduleID);

            int updatedRows = pstmt.executeUpdate();

            if (updatedRows > 0) {
                System.out.println("Schedule deleted from database successfully");
            } else {
                System.out.println("Failed to delete schedule from database");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
