package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Menu {
    private int menuID;
    private String name;
    private List<Dish> dishes;
    private Date effectiveDate;

    public Menu(int menuID, String name, Date effectiveDate) {
        this.menuID = menuID;
        this.name = name;
        this.effectiveDate = effectiveDate;
        this.dishes = new ArrayList<>();
    }

    // Getters and setters...
}
