package FinalInterTeamServices;
import model.Ingredient;
import model.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class BOHInterfaceFinal {

    public List<Order> getOrdersFromDatabase(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = new ArrayList<>();
        //implement connection to database
        //implement queries that gets all stock orders and returns everything given a range of date
        return orders;
    }

    public List<Ingredient> getOrderIngredientsFromDatabase(int orderID) {
        List<Ingredient> ingredients = new ArrayList<>();
        //implement connection to database
        //implement queries that gets all the ingredients given an order ID
        return ingredients;
    }

    public List<Order> getInProgressOrdersFromDatabase(LocalDate startDate, LocalDate endDate) {
        List<Order> inProgressOrders = new ArrayList<>();
        //implement connection to database
        //implement queries that given a range of date, will return a list of stock orders where order status = in process
        return inProgressOrders;
    }
        //MenuUI still need to be made
        //implement connection to database
        //implement queries that returns menus with status "x"

}