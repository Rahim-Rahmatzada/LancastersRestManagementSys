package UI;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

// CLASS IS USED FOR StaffUI
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
