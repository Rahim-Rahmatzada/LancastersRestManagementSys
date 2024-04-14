package model;

public class StaffInfo {
    private int staffID;
    private String staffName;
    private String staffRole;


    public StaffInfo(int staffID, String staffName, String staffRole) {
        this.staffID = staffID;
        this.staffName = staffName;
        this.staffRole = staffRole;

    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }

}
