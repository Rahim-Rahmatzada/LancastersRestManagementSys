package UI;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleEntry {
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
        return new SimpleObjectProperty<>(date);
    }

    public javafx.beans.property.ObjectProperty<LocalTime> startTimeProperty() {
        return startTime;
    }

    public javafx.beans.property.ObjectProperty<LocalTime> endTimeProperty() {
        return endTime;
    }

    public SimpleStringProperty durationProperty() {
        return duration;
    }

    public SimpleStringProperty employeeNameProperty() {
        return employeeName;
    }
}
