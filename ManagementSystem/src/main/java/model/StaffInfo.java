package model;

/**
 * Represents staff information.
 */
public class StaffInfo {
    private int staffID;
    private String staffName;
    private String staffRole;

    /**
     * Constructs a StaffInfo object with the specified details.
     *
     * @param staffID   The ID of the staff.
     * @param staffName The name of the staff.
     * @param staffRole The role of the staff.
     */
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
