package model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private int menuID;
    private LocalDate effectiveDate;
    private String menuStatus;
    private List<Dish> dishes;

    public Menu(int menuID, LocalDate  effectiveDate, String menuStatus) {
        this.menuID = menuID;
        this.effectiveDate = effectiveDate;
        this.menuStatus = menuStatus;
        this.dishes = new ArrayList<>();
    }

    public Date getEffectiveDateAsSqlDate() {
        return Date.valueOf(effectiveDate);
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

    // Other methods...
}