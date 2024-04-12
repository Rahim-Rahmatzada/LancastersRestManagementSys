package model;

import java.util.Date;
import java.sql.Time;

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


}
