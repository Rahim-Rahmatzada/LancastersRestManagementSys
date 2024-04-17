package model;

import java.time.LocalDate;


/**
 * Represents a staff member's holiday period.
 */
public class StaffHoliday {
    private int holidayID;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Constructs a StaffHoliday object with the specified details.
     *
     * @param holidayID The ID of the holiday.
     * @param startDate The start date of the holiday period.
     * @param endDate   The end date of the holiday period.
     */
    public StaffHoliday(int holidayID, LocalDate startDate, LocalDate endDate) {
        this.holidayID = holidayID;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    

}