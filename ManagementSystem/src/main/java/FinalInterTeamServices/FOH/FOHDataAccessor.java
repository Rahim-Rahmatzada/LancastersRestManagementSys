package FinalInterTeamServices.FOH;

import DatabaseConnections.DataUserDatabaseConnector;
import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FOHDataAccessor implements FOHFinalInterface {

    @Override
    public List<Menu> getMenusByStatus(String status) {
        List<Menu> menus = new ArrayList<>();

        try (Connection connection = DatabaseConnections.DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT menuID, effectiveDate, menuStatus " +
                             "FROM Menu " +
                             "WHERE menuStatus = ?")) {

            statement.setString(1, status);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("menuID");
                LocalDate effectiveDate = resultSet.getDate("effectiveDate").toLocalDate();
                String menuStatus = resultSet.getString("menuStatus");

                Menu menu = new Menu(id, effectiveDate, menuStatus);
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

        try (Connection connection = DatabaseConnections.DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT d.dishID, d.name, d.price, d.dishDescription, d.allergyInfo, d.wineID " +
                             "FROM Dish d " +
                             "JOIN Menu_Dish md ON d.dishID = md.dishID " +
                             "WHERE md.menuID = ?")) {

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

        try (Connection connection = DatabaseConnections.DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT dishID, name, price, dishDescription, allergyInfo, wineID " +
                             "FROM Dish " +
                             "WHERE dishID = ?")) {

            statement.setInt(1, dishID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("dishID");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String description = resultSet.getString("dishDescription");
                String allergyInfo = resultSet.getString("allergyInfo");
                int wineID = resultSet.getInt("wineID");

                dish = new Dish(id, name, price, description, allergyInfo, wineID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dish;
    }

    @Override
    public Wine getWineByDish(int dishID) {
        Wine wine = null;

        try (Connection connection = DatabaseConnections.DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT w.wineID, w.wineName, w.wineType, w.wineVintage, w.wineQuantity " +
                             "FROM Dish d " +
                             "JOIN Wine w ON d.wineID = w.wineID " +
                             "WHERE d.dishID = ?")) {

            statement.setInt(1, dishID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("wineID");
                String name = resultSet.getString("wineName");
                String type = resultSet.getString("wineType");
                int vintage = resultSet.getInt("wineVintage");
                int quantity = resultSet.getInt("wineQuantity");

                wine = new Wine(id, name, type, vintage, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wine;
    }

    @Override
    public double getDishPrice(int dishID) {
        double price = 0.0;

        try (Connection connection = DatabaseConnections.DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT price FROM Dish WHERE dishID = ?")) {

            statement.setInt(1, dishID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                price = resultSet.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return price;
    }

    @Override
    public double getWinePrice(int wineID) {
        double price = 0.0;

        try (Connection connection = DatabaseConnections.DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT winePrice FROM Wine WHERE wineID = ?")) {

            statement.setInt(1, wineID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                price = resultSet.getDouble("winePrice");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return price;
    }

    @Override
    public int getWineQuantity(int wineID) {
        int quantity = 0;

        try (Connection connection = DatabaseConnections.DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT wineQuantity FROM Wine WHERE wineID = ?")) {

            statement.setInt(1, wineID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                quantity = resultSet.getInt("wineQuantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quantity;
    }

    @Override
    public List<StaffInfo> getStaffByRole(String role) {
        List<StaffInfo> staffMembers = new ArrayList<>();

        try (Connection connection = DatabaseConnections.DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT s.staffID, s.staffName, s.staffRole " +
                             "FROM StaffInfo s " +
                             "WHERE s.staffRole = ?")) {

            statement.setString(1, role);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int staffID = resultSet.getInt("staffID");
                String staffName = resultSet.getString("staffName");
                String staffRole = resultSet.getString("staffRole");

                StaffInfo staffMember = new StaffInfo(staffID, staffName, staffRole);
                staffMembers.add(staffMember);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return staffMembers;
    }

    @Override
    public List<Schedule> getStaffSchedule(int staffID) {
        List<Schedule> schedules = new ArrayList<>();

        try (Connection connection = DatabaseConnections.DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT ss.scheduleID, ss.dateWorking, ss.shiftStartingTime, ss.shiftEndingTime, ss.duration " +
                             "FROM StaffSchedule ss " +
                             "JOIN StaffSchedule_StaffInfo ssi ON ss.scheduleID = ssi.scheduleID " +
                             "WHERE ssi.staffID = ?")) {

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
    public boolean assignWaiterToTable(String staffName, int tableID) {
        try (Connection connection = DatabaseConnections.DataUserDatabaseConnector.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(
                     "SELECT s.staffID " +
                             "FROM StaffInfo s " +
                             "WHERE s.staffName = ? AND s.staffRole = 'waiter'");
             PreparedStatement insertStmt = connection.prepareStatement(
                     "INSERT INTO Tables_FOHStaff (tableID, staffInfoID) VALUES (?, ?)");
             PreparedStatement checkDuplicateStmt = connection.prepareStatement(
                     "SELECT COUNT(*) FROM Tables_FOHStaff WHERE tableID = ? AND staffInfoID = ?")) {

            checkStmt.setString(1, staffName);
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next()) {
                int staffID = resultSet.getInt("staffID");

                checkDuplicateStmt.setInt(1, tableID);
                checkDuplicateStmt.setInt(2, staffID);
                ResultSet duplicateResultSet = checkDuplicateStmt.executeQuery();

                if (duplicateResultSet.next() && duplicateResultSet.getInt(1) > 0) {
                    System.out.println("Waiter " + staffName + " is already assigned to Table " + tableID);
                    return false;
                }

                insertStmt.setInt(1, tableID);
                insertStmt.setInt(2, staffID);
                int rowsAffected = insertStmt.executeUpdate();

                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public List<StaffHolidayAssociation> getStaffHolidaysWithinDateRange(LocalDate startDate, LocalDate endDate) {
        List<StaffHolidayAssociation> staffHolidayAssociations = new ArrayList<>();

        try (Connection connection = DataUserDatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT shsi.staffID, shsi.holidayID, sh.startDate, sh.endDate, si.staffName " +
                             "FROM StaffHoliday sh " +
                             "JOIN StaffHoliday_StaffInfo shsi ON sh.holidayID = shsi.holidayID " +
                             "JOIN StaffInfo si ON shsi.staffID = si.staffID " +
                             "WHERE sh.startDate >= ? AND sh.endDate <= ?")) {

            statement.setDate(1, Date.valueOf(startDate));
            statement.setDate(2, Date.valueOf(endDate));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int staffID = resultSet.getInt("staffID");
                int holidayID = resultSet.getInt("holidayID");
                Date startDateValue = resultSet.getDate("startDate");
                Date endDateValue = resultSet.getDate("endDate");
                String staffName = resultSet.getString("staffName");

                StaffHoliday holiday = new StaffHoliday(holidayID, startDateValue.toLocalDate(), endDateValue.toLocalDate());
                StaffInfo staffInfo = new StaffInfo(staffID, staffName, null); // Assuming the StaffInfo constructor expects a null staffRole
                StaffHolidayAssociation association = new StaffHolidayAssociation(staffID, holidayID);

                staffHolidayAssociations.add(association);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return staffHolidayAssociations;
    }
}