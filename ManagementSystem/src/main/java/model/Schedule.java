package model;

import java.util.Date;

public class Schedule {
    private int scheduleID;
    private String staffName;
    private Date startDate;
    private Date endDate;
    private String role;

    public Schedule(int scheduleID, String staffName, Date startDate, Date endDate, String role) {
        this.scheduleID = scheduleID;
        this.staffName = staffName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.role = role;
    }

    // Getters and setters...
}
