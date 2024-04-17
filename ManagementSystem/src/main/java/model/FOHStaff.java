package model;

/**
 * Class representing Front of House (FOH) staff members.
 */
public class FOHStaff {
    private int staffID;
    private String name;
    private String role;

    /**
     * Constructs an FOHStaff object with the specified parameters.
     *
     * @param staffID The ID of the staff member.
     * @param name    The name of the staff member.
     * @param role    The role of the staff member.
     */
    public FOHStaff(int staffID, String name, String role) {
        this.staffID = staffID;
        this.name = name;
        this.role = role;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}