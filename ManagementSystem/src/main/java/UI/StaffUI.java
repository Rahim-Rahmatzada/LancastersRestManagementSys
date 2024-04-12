package UI;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.AdminDatabaseConnector;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.AdminDatabaseConnector;
import model.StaffInfo;

public class StaffUI extends BaseUI {
    private DatePicker datePicker;
    private Button getScheduleButton;
    private TableView<StaffInfo> scheduleTableView;

    public StaffUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Staff");
        setTopText("Staff Overview");

        VBox mainContent = new VBox();
        mainContent.setPadding(new Insets(20));
        mainContent.setSpacing(10);

        // Create a date picker and a "Get Schedule" button
        datePicker = new DatePicker();
        getScheduleButton = new Button("Get Schedule");
        getScheduleButton.setOnAction(e -> loadSchedule());

        HBox topControls = new HBox(10, datePicker, getScheduleButton);

        // Create a TableView to display the schedule
        scheduleTableView = new TableView<>();
        scheduleTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StaffInfo, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(param -> param.getValue().getNameProperty());

        TableColumn<StaffInfo, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(param -> param.getValue().getRoleProperty());

        TableColumn<StaffInfo, String> shiftStartColumn = new TableColumn<>("Shift Start");
        shiftStartColumn.setCellValueFactory(param -> param.getValue().getShiftStartProperty());

        TableColumn<StaffInfo, String> shiftEndColumn = new TableColumn<>("Shift End");
        shiftEndColumn.setCellValueFactory(param -> param.getValue().getShiftEndProperty());

        TableColumn<StaffInfo, String> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(param -> param.getValue().getDurationProperty());

        scheduleTableView.getColumns().addAll(nameColumn, roleColumn, shiftStartColumn, shiftEndColumn, durationColumn);

        mainContent.getChildren().addAll(topControls, scheduleTableView);
        setMainContent(mainContent);
    }

    private void loadSchedule() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            List<StaffInfo> staffSchedule = getStaffScheduleFromDatabase(selectedDate);
            scheduleTableView.getItems().setAll(staffSchedule);
        } else {
            showAlert("No Date Selected", "Please select a date to view the staff schedule.");
        }
    }

    private List<StaffInfo> getStaffScheduleFromDatabase(LocalDate date) {
        List<StaffInfo> staffSchedule = new ArrayList<>();

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

                    StaffInfo staffInfo = new StaffInfo(name, role, shiftStart.toString(), shiftEnd.toString(), duration);
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



//So user enters a date thorugh a date picker then hits the get schedule button, this will then loop through all the staff and check if the relation with the staff schedule
//where there is a dateWorking variable if it matches input date of user then output the staffs name role as well as their starting/ending time for shift and their duration
//in one table view

//Button to go to "View Staff Holidays", user can enter begin and end date then there will be an sql query looking through each staff and if their holiday dates fit the range
//if so output staff name, starting/ending date of holiday and numberOfDays

//Another button for "Modify Staff Schedule" which will lead to another UI subsection, there will be a table view of all staff listed then user can click on a user which
//will output the all schedule relations with that staff here they can add remove modify a specific staffs schedule, this info will be in another table view