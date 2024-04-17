package model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a menu.
 */
public class Menu {
    private int menuID;
    private LocalDate effectiveDate;
    private String menuStatus;
    private List<Dish> dishes;

    /**
     * Constructs a menu with the specified ID, effective date, and status.
     *
     * @param menuID        The ID of the menu.
     * @param effectiveDate The effective date of the menu.
     * @param menuStatus    The status of the menu.
     */
    public Menu(int menuID, LocalDate  effectiveDate, String menuStatus) {
        this.menuID = menuID;
        this.effectiveDate = effectiveDate;
        this.menuStatus = menuStatus;
        this.dishes = new ArrayList<>();
    }


    public int getMenuID() {
        return menuID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getMenuStatus() {
        return menuStatus;
    }

    public void setMenuStatus(String menuStatus) {
        this.menuStatus = menuStatus;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }


}