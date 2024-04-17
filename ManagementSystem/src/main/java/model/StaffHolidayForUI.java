package model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

/**
 * Represents a staff holiday for UI display.
 */
public class StaffHolidayForUI {
    private StringProperty name;
    private ObjectProperty<LocalDate> startDate;
    private ObjectProperty<LocalDate> endDate;
    private LongProperty duration;

    /**
     * Constructs a StaffHolidayForUI object with the specified details.
     *
     * @param name      The name of the staff member.
     * @param startDate The start date of the holiday.
     * @param endDate   The end date of the holiday.
     * @param duration  The duration of the holiday.
     */
    public StaffHolidayForUI(String name, LocalDate startDate, LocalDate endDate, long duration) {
        this.name = new SimpleStringProperty(name);
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.endDate = new SimpleObjectProperty<>(endDate);
        this.duration = new SimpleLongProperty(duration);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public LocalDate getStartDate() {
        return startDate.get();
    }

    public ObjectProperty<LocalDate> getStartDateProperty() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    public LocalDate getEndDate() {
        return endDate.get();
    }

    public ObjectProperty<LocalDate> getEndDateProperty() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.set(endDate);
    }

    public long getDuration() {
        return duration.get();
    }

    public LongProperty getDurationProperty() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration.set(duration);
    }
}