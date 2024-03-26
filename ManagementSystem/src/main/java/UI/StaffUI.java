package UI;

import javafx.scene.layout.VBox;

public class StaffUI extends BaseUI {

    public StaffUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Staff");
        setTopText("Staff Overview");

        // Set the main content for the StaffUI.
        VBox staffMainContent = new VBox();
        // Add components to staffMainContent as needed.
        setMainContent(staffMainContent);
    }

}



//    Staff Scheduling: Manage staff schedules including shifts, holidays, and absences to ensure adequate staffing.
//        Performance Tracking: Monitor staff performance and productivity metrics.