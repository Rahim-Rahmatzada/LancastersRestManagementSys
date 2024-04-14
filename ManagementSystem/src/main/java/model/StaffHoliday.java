package model;

import java.time.LocalDate;

public class StaffHoliday {
    private int holidayID;
    private LocalDate startDate;
    private LocalDate endDate;

    public StaffHoliday(int holidayID, LocalDate startDate, LocalDate endDate) {
        this.holidayID = holidayID;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    

}