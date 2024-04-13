package model;

import java.sql.Time;
import java.util.Date;

public class Schedule {
    private int scheduleID;
    private Date dateWorking;
    private Time shiftStartingTime;
    private Time shiftEndingTime;
    private String duration;

    public Schedule(int scheduleID, Date dateWorking, Time shiftStartingTime, Time shiftEndingTime, String duration) {
        this.scheduleID = scheduleID;
        this.dateWorking = dateWorking;
        this.shiftStartingTime = shiftStartingTime;
        this.shiftEndingTime = shiftEndingTime;
        this.duration = duration;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public Date getDateWorking() {
        return dateWorking;
    }

    public void setDateWorking(Date dateWorking) {
        this.dateWorking = dateWorking;
    }

    public Time getShiftStartingTime() {
        return shiftStartingTime;
    }

    public void setShiftStartingTime(Time shiftStartingTime) {
        this.shiftStartingTime = shiftStartingTime;
    }

    public Time getShiftEndingTime() {
        return shiftEndingTime;
    }

    public void setShiftEndingTime(Time shiftEndingTime) {
        this.shiftEndingTime = shiftEndingTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
