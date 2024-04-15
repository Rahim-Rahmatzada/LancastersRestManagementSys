package FinalInterfaces.FOH;

import model.Dish;
import model.FOHStaff;
import model.Menu;
import model.Schedule;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FOHImplementation implements FOHInterface {
    private Connection connection;

    public FOHImplementation(Connection connection) {
        this.connection = connection;
    }

    // Menu and Dish Information
    @Override
    public List<Menu> getActiveMenus() {
        List<Menu> menus = new ArrayList<>();
        try {
            String query = "SELECT * FROM Menu WHERE menuStatus = 'Active'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int menuID = resultSet.getInt("menuID");
                Date effectiveDate = resultSet.getDate("effectiveDate");
                LocalDate localEffectiveDate = effectiveDate.toLocalDate();
                String menuStatus = resultSet.getString("menuStatus");
                Menu menu = new Menu(menuID, localEffectiveDate, menuStatus);
                menus.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menus;
    }

    @Override
    public List<Dish> getDishesByMenu(int menuID) {
        List<Dish> dishes = new ArrayList<>();
        try {
            String query = "SELECT d.* FROM Dish d " +
                    "JOIN Menu_Dish md ON d.dishID = md.dishID " +
                    "WHERE md.menuID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, menuID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int dishID = resultSet.getInt("dishID");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String description = resultSet.getString("dishDescription");
                String allergyInfo = resultSet.getString("allergyInfo");
                int wineID = resultSet.getInt("wineID");
                Dish dish = new Dish(dishID, name, price, description, allergyInfo, wineID);
                dishes.add(dish);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }

    @Override
    public Dish getDishDetails(int dishID) {
        Dish dish = null;
        try {
            String query = "SELECT * FROM Dish WHERE dishID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, dishID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String description = resultSet.getString("dishDescription");
                String allergyInfo = resultSet.getString("allergyInfo");
                int wineID = resultSet.getInt("wineID");
                dish = new Dish(dishID, name, price, description, allergyInfo, wineID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dish;
    }

    // Staff Management
    @Override
    public List<FOHStaff> getFOHStaff() {
        List<FOHStaff> fohStaff = new ArrayList<>();
        try {
            String query = "SELECT s.* FROM StaffInfo s " +
                    "JOIN FOHStaff f ON s.staffID = f.staffInfoID";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int staffID = resultSet.getInt("staffID");
                String name = resultSet.getString("staffName");
                String role = resultSet.getString("staffRole");
                FOHStaff staff = new FOHStaff(staffID, name, role);
                fohStaff.add(staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fohStaff;
    }

    @Override
    public List<Schedule> getStaffSchedule(int staffID) {
        List<Schedule> schedules = new ArrayList<>();
        try {
            String query = "SELECT ss.* FROM StaffSchedule ss " +
                    "JOIN StaffSchedule_StaffInfo ssi ON ss.scheduleID = ssi.scheduleID " +
                    "WHERE ssi.staffID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, staffID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int scheduleID = resultSet.getInt("scheduleID");
                Date dateWorking = resultSet.getDate("dateWorking");
                Time startTime = resultSet.getTime("shiftStartingTime");
                Time endTime = resultSet.getTime("shiftEndingTime");
                String duration = resultSet.getString("duration");
                Schedule schedule = new Schedule(scheduleID, dateWorking, startTime, endTime, duration);
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    @Override
    public void assignStaffToTable(int staffID, int tableID) {
        try {
            String query = "INSERT INTO Tables_FOHStaff (tableID, staffInfoID) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, tableID);
            statement.setInt(2, staffID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}