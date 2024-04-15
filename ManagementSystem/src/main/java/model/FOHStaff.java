package model;

public class FOHStaff {
    private int staffID;
    private String name;
    private String role;

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