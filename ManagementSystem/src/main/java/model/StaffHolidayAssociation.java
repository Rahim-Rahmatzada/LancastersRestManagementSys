package model;

/**
 * Represents the association between a staff member and a holiday.
 */
public class StaffHolidayAssociation {
    private int staffID;
    private int holidayID;

    /**
     * Constructs a StaffHolidayAssociation object with the specified staff ID and holiday ID.
     *
     * @param staffID   The ID of the staff member.
     * @param holidayID The ID of the holiday associated with the staff member.
     */
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