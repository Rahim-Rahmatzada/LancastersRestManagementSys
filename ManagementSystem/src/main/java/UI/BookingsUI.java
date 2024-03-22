package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import model.DatabaseConnector;

import java.sql.*;

public class BookingsUI extends BaseUI {

    public BookingsUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Bookings");

        setTopText("Bookings Overviewwwwwwww");

        // Set the main content for the BookingsUI.
        VBox bookingsMainContent = new VBox();

        setMainContent(bookingsMainContent);
    }


    private void viewBookings() {
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Booking")) {

            System.out.println("All bookings:");
            while (rs.next()) {
                // Retrieve and print all booking details here
                System.out.println("Booking ID: " + rs.getInt("bookingID"));
                // Add other fields from the booking table as needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}



//    Bookings Dashboard UI
//        View Upcoming Bookings: Display all future bookings with details such as date, time, customer name, and table number.
//        Manage Bookings: Options to add, edit, cancel, and confirm bookings.
//        Daily Booking Limit Settings: Adjust the number of diners allowed per half-hour slot and manage special event booking capacities.
//        Online Booking Integration: Synchronize with online booking platforms to update the internal booking system.
//        Table Management: View and modify the layout of the dining area for various seating configurations.
//        Walk-ins Management: Keep a couple of tables aside for walk-ins and manage their allocation.

//        Booking Analysis: Visualize booking trends over time and compare with historical data to anticipate busy periods using a graph
