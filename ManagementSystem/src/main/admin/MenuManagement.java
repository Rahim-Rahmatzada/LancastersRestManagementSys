package main.admin;

import java.util.List;
import java.util.Map;

public interface MenuManagement {

    //should update
    void UpdateMenu(String dishName, List<String> ingredients, double price);
    Map<String, Double> getMenuPricing();
    void updateIngredientCost(String ingredient, double cost);
    void setAllergenInfo(String dishName, List<String> allergens);
    void archiveMenu(String menuName);

    void setDishPrice(String dishName, List<String> ingredients);



}

