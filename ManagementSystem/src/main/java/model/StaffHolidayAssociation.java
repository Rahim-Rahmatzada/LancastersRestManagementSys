package model;

public class StaffHolidayAssociation {
    private int staffID;
    private int holidayID;

    public StaffHolidayAssociation(int staffID, int holidayID) {
        this.staffID = staffID;
        this.holidayID = holidayID;
    }

    public int getStaffID() {
        return staffID;
    }

    public int getHolidayID() {
        return holidayID;
    }
}