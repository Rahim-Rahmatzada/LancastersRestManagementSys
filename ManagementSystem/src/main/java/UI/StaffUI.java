package UI;



import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;

//testing comment to be removed

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