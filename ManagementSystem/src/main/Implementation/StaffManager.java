package main.Implementation;

import main.admin.StaffManagement;
import java.text.SimpleDateFormat;
import java.util.*;

public class StaffManager implements StaffManagement {
    // Maps each date to a schedule, where the schedule is a map of staff names to their roles for that day.
    private Map<Date, Map<String, String>> staffSchedules = new HashMap<>();

    // Maps each staff member to a list of Date arrays, where each array represents a holiday period (start and end dates).
    private Map<String, List<Date[]>> holidays = new HashMap<>();

    // Maps each staff member to their current role.
    private Map<String, String> staffRoles = new HashMap<>();

    // Maps each staff member to a list of Date arrays, where each array represents an absence period (start and end dates).
    private Map<String, List<Date[]>> absences = new HashMap<>();

    // Maps each staff member to their performance scores, with the scores being mapped against specific dates.
    private Map<String, Map<Date, Double>> staffPerformance = new HashMap<>();

    @Override
    public void scheduleStaff(String staffName, String staffRole, Date startDate, Date endDate) {
        // Schedule a staff member with a role for a range of dates from startDate to endDate.
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        // To include the end date in the scheduling.
        end.add(Calendar.DATE, 1);

        // Iterate through each day in the range, adding the staff member to the schedule for each day.
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            // Retrieve or create the schedule for the current date.
            Map<String, String> dailySchedule = staffSchedules.getOrDefault(date, new HashMap<>());
            // Assign the staff member to the role for the current date.
            dailySchedule.put(staffName, staffRole);
            // Update the master schedule with the daily schedule.
            staffSchedules.put(date, dailySchedule);
        }
    }

    @Override
    public Map<String, Map<String, String>> getStaffSchedules(Date startDate, Date endDate) {
        // Retrieves staff schedules within a specified date range.
        Map<String, Map<String, String>> schedulesInRange = new HashMap<>();
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Iterate over each date in the range, including both start and end dates.
        for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            // If there's a schedule for the current date, add it to the returned map.
            if (staffSchedules.containsKey(date)) {
                schedulesInRange.put(sdf.format(date), new HashMap<>(staffSchedules.get(date)));
            }
        }

        return schedulesInRange;
    }

    @Override
    public void recordHoliday(String staffName, Date startDate, Date endDate) {
        // Records a holiday period for a staff member.
        List<Date[]> staffHolidays = holidays.getOrDefault(staffName, new ArrayList<>());
        staffHolidays.add(new Date[]{startDate, endDate});
        // Update the holiday list for the staff member.
        holidays.put(staffName, staffHolidays);
    }

    @Override
    public void updateStaffRole(String staffName, String newRole) {
        // Updates the role of a staff member.
        staffRoles.put(staffName, newRole);
    }

    @Override
    public void recordAbsences(List<String> staffNames, List<Date> startDates, List<Date> endDates) {
        // Records absence periods for multiple staff members.
        for (int i = 0; i < staffNames.size(); i++) {
            List<Date[]> staffAbsences = absences.getOrDefault(staffNames.get(i), new ArrayList<>());
            staffAbsences.add(new Date[]{startDates.get(i), endDates.get(i)});
            // Update the absence list for each staff member.
            absences.put(staffNames.get(i), staffAbsences);
        }
    }

    @Override
    public Map<String, Double> getStaffPerformance(String staffName, Date date) {
        // Retrieves the performance score of a staff member for a specific date.
        Map<String, Double> performance = new HashMap<>();
        Map<Date, Double> datesPerformance = staffPerformance.get(staffName);
        // Check if there's a recorded performance for the given date.
        if (datesPerformance != null && datesPerformance.containsKey(date)) {
            performance.put(staffName, datesPerformance.get(date));
        }
        return performance;
    }
}
