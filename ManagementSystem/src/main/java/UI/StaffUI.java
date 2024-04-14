package UI;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.AdminDatabaseConnector;
import model.StaffHolidayForUI;
import model.StaffInfoForUI;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class StaffUI extends BaseUI {
    private DatePicker datePicker;
    private Button getScheduleButton;
    private Button viewHolidaysButton;

    private TableView<StaffInfoForUI> scheduleTableView;
    private TableView<StaffHolidayForUI> holidayTableView;


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
        getScheduleButton = new Button("Get Schedule");
        getScheduleButton.setOnAction(e -> loadSchedule());
        viewHolidaysButton = new Button("View Staff Holidays");
        viewHolidaysButton.setOnAction(e -> viewStaffHolidays());

        HBox topControls = new HBox(10, datePicker, getScheduleButton, viewHolidaysButton);

        scheduleTableView = createScheduleTableView();

        mainContent.getChildren().addAll(topControls, scheduleTableView);
        setMainContent(mainContent);
    }

    private void viewStaffHolidays() {
        VBox mainContent = (VBox) getMainContent();
        mainContent.getChildren().clear();

        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        Button getHolidaysButton = new Button("Get Holidays");
        getHolidaysButton.setOnAction(e -> loadHolidays(startDatePicker.getValue(), endDatePicker.getValue()));

        HBox holidayControls = new HBox(10, startDatePicker, endDatePicker, getHolidaysButton);

        holidayTableView = createHolidayTableView();

        mainContent.getChildren().addAll(holidayControls, holidayTableView);
    }

    private TableView<StaffHolidayForUI> createHolidayTableView() {
        TableView<StaffHolidayForUI> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StaffHolidayForUI, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(param -> param.getValue().getNameProperty());

        TableColumn<StaffHolidayForUI, LocalDate> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(param -> param.getValue().getStartDateProperty());

        TableColumn<StaffHolidayForUI, LocalDate> endDateColumn = new TableColumn<>("End Date");
        endDateColumn.setCellValueFactory(param -> param.getValue().getEndDateProperty());

        TableColumn<StaffHolidayForUI, Long> durationColumn = new TableColumn<>("Duration (Days)");
//        durationColumn.setCellValueFactory(param -> param.getValue().getDurationProperty());
        //NNEEEDD TO FIXXX HTISSSSS

        tableView.getColumns().addAll(nameColumn, startDateColumn, endDateColumn, durationColumn);

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
            scheduleTableView.getItems().setAll(staffSchedule);
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
