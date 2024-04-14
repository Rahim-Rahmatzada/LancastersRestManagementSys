package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StaffInfoForUI {
    private StringProperty name;
    private StringProperty role;
    private StringProperty shiftStart;
    private StringProperty shiftEnd;
    private StringProperty duration;

    private IntegerProperty staffID;


    public StaffInfoForUI(String name, String role, String shiftStart, String shiftEnd, String duration) {
        this.name = new SimpleStringProperty(name);
        this.role = new SimpleStringProperty(role);
        this.shiftStart = new SimpleStringProperty(shiftStart);
        this.shiftEnd = new SimpleStringProperty(shiftEnd);
        this.duration = new SimpleStringProperty(duration);
    }

    public StaffInfoForUI(int staffID, String name, String role) {
        this.staffID = new SimpleIntegerProperty(staffID);
        this.name = new SimpleStringProperty(name);
        this.role = new SimpleStringProperty(role);
    }

    public int getStaffID() {
        return staffID.get();
    }

    public IntegerProperty getStaffIDProperty() {
        return staffID;
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

    public String getRole() {
        return role.get();
    }

    public StringProperty getRoleProperty() {
        return role;
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public String getShiftStart() {
        return shiftStart.get();
    }

    public StringProperty getShiftStartProperty() {
        return shiftStart;
    }

    public void setShiftStart(String shiftStart) {
        this.shiftStart.set(shiftStart);
    }

    public String getShiftEnd() {
        return shiftEnd.get();
    }

    public StringProperty getShiftEndProperty() {
        return shiftEnd;
    }

    public void setShiftEnd(String shiftEnd) {
        this.shiftEnd.set(shiftEnd);
    }

    public String getDuration() {
        return duration.get();
    }

    public StringProperty getDurationProperty() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration.set(duration);
    }
}