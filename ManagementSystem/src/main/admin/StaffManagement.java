package main.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StaffManagement {
    void scheduleStaff(String staffName, String staffRole, Date startDate, Date endDate);
    Map<String, Map<String, String>> getStaffSchedules(Date startDate, Date endDate);
    void recordHoliday(String staffName, Date startDate, Date endDate);
    void updateStaffRole(String staffName, String newRole);
    void recordAbsences(List<String> staffNames, List<Date> startDates, List<Date> endDates);
    Map<String, Double> getStaffPerformance(String staffName, Date date);


}

//it should take a staff name and staff role then assign that staff to the schedule date, the schedule date should be a period from one date to another