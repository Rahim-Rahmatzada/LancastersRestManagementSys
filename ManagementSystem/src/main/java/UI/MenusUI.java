package UI;


import javafx.scene.layout.VBox;

public class MenusUI extends BaseUI {

    public MenusUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Menus");
        setTopText("MENUUU Overviewwwwwwww");

        // Set the main content for the DashboardUI.
        VBox dashboardMainContent = new VBox();
        // Add components to dashboardMainContent as needed.
        setMainContent(dashboardMainContent);
    }




}


//    Menus UI
//    Menu Creation and Management: Create new menus, edit existing ones, and archive outdated menus.
//        Dish Construction and Editing: Combine approved recipes into dishes; edit ingredients, preparation, and presentation details.
//        Price Setting: Determine and update pricing for each dish based on cost and markup strategy.
//        Allergen Information: Ensure all allergen information is up to date and clearly labeled on menu items.
//        Menu Review: Allow management and other stakeholders to review and provide feedback on draft menus.
//        Printable Menus: Generate printable versions of the final menus for use in the restaurant.

//    Menu Costing and Review: Oversee menu pricing, ingredient cost tracking, and ensure profitability.