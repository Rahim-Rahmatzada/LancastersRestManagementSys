package main.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StaffManagement {
    void scheduleStaff(String staffName, Date scheduleDate);
    Map<Date, List<String>> getStaffSchedules();
    void recordHoliday(String staffName, Date startDate, Date endDate);
    void updateStaffRole(String staffName, String newRole);
    void recordAbsences(List<String> staffNames, List<Date> startDates, List<Date> endDates);


}

