package model;

import java.sql.Time;
import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ScheduleForUI {
    private final IntegerProperty scheduleID;
    private final ObjectProperty<LocalDate> date;
    private final ObjectProperty<Time> startTime;
    private final ObjectProperty<Time> endTime;
    private final StringProperty duration;

    public ScheduleForUI(int scheduleID, LocalDate date, Time startTime, Time endTime, String duration) {
        this.scheduleID = new SimpleIntegerProperty(scheduleID);
        this.date = new SimpleObjectProperty<>(date);
        this.startTime = new SimpleObjectProperty<>(startTime);
        this.endTime = new SimpleObjectProperty<>(endTime);
        this.duration = new SimpleStringProperty(duration);
    }

    public int getScheduleID() {
        return scheduleID.get();
    }

    public IntegerProperty getScheduleIDProperty() {
        return scheduleID;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> getDateProperty() {
        return date;
    }

    public Time getStartTime() {
        return startTime.get();
    }

    public ObjectProperty<Time> getStartTimeProperty() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime.get();
    }

    public ObjectProperty<Time> getEndTimeProperty() {
        return endTime;
    }

    public String getDuration() {
        return duration.get();
    }

    public StringProperty getDurationProperty() {
        return duration;
    }

    public void setDate(LocalDate newValue) {
    }

    public void setStartTime(Time time) {
    }

    public void setEndTime(Time time) {
    }
}
