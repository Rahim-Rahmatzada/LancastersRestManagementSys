package UI;

import javafx.scene.layout.VBox;

public class DashboardUI extends BaseUI {

    public DashboardUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Dashboard");
        setTopText("Dashboard Overview");

        // Set the main content for the DashboardUI.
        VBox dashboardMainContent = new VBox();
        // Add components to dashboardMainContent as needed.
        setMainContent(dashboardMainContent);

        preloadUIClasses();

    }

    private void preloadUIClasses() {
        Thread preloadThread = new Thread(() -> {
            // Preload other UI classes
            uiSwitcher.preloadInventoryUI();
            uiSwitcher.preloadSalesUI();
            uiSwitcher.preloadBookingsUI();
            uiSwitcher.preloadMenusUI();
            uiSwitcher.preloadStaffUI();
            uiSwitcher.preloadWineUI();
            uiSwitcher.preloadStockOrdersUI();
        });
        preloadThread.setDaemon(true); // Set the thread as a daemon thread
        preloadThread.start();
    }

















//
//    Welcome Message:
//    Display a personalized welcome message to greet the user when they log in or access the dashboard.
//    Include the user's name or role to make the message more engaging.
//    Key Metrics and Statistics:
//    Show important metrics and statistics related to your application's domain.
//    For example, you can display the total number of sales, revenue, customers, or any other relevant data.
//    Use visual elements like cards, tiles, or widgets to present the metrics in an easy-to-understand format.
//    Charts and Graphs:
//    Include charts and graphs to visually represent data and trends.
//    Use line charts to show sales trends over time, bar charts to compare different categories, or pie charts to show distribution.
//    Provide interactive features like zooming, panning, or tooltips to allow users to explore the data further.
//    Recent Activity or Notifications:
//    Display a list or feed of recent activities or notifications relevant to the user.
//    Show the latest orders, customer inquiries, system alerts, or any other important updates.
//    Use a scrollable list or a paginated table to present the information in a clear and concise manner.
//    Quick Actions or Shortcuts:
//    Provide quick action buttons or shortcuts to commonly used features or tasks.
//    For example, you can have buttons to quickly create a new order, view pending tasks, or navigate to frequently accessed pages.
//    Use intuitive icons and labels to make the actions easily recognizable.
//    Calendar or Upcoming Events:
//    Integrate a calendar component to display upcoming events, meetings, or deadlines.
//    Highlight important dates or milestones relevant to the user or the application's domain.
//    Allow users to click on events to view more details or navigate to related pages.
//    Performance Indicators or Progress Bars:
//    Use progress bars or performance indicators to visualize the progress of key metrics or goals.
//    For example, you can show the percentage of sales targets achieved, customer satisfaction ratings, or project completion status.
//    Use colors and labels to clearly communicate the current status and progress.
//    News or Announcements:
//    Include a section for displaying important news, announcements, or updates related to your application or industry.
//    Keep users informed about new features, system maintenance, or any other relevant information.
//    Use a collapsible or expandable panel to show the full content of the announcements.
//    User Profile or Settings:
//    Provide a quick access to the user's profile or settings.
//    Display the user's name, profile picture, or any other relevant information.
//    Include links or buttons to allow users to edit their profile, change preferences, or log out.
//    Navigation or Menu:
//    Include a navigation menu or sidebar to provide easy access to different sections or modules of your application.
//    Use clear and concise labels to guide users to the desired pages or features.
//    Consider using icons or tooltips to enhance the usability and visual appeal of the navigation.

}
