package model;

public class DishDetails  {

        private int dishID;
        private String name;
        private double price;
        private String dishDescription;
        private String allergyInfo;
        private int wineID;
        private String wineName;

        public DishDetails(int dishID, String name, double price, String dishDescription, String allergyInfo, int wineID, String wineName) {
            this.dishID = dishID;
            this.name = name;
            this.price = price;
            this.dishDescription = dishDescription;
            this.allergyInfo = allergyInfo;
            this.wineID = wineID;
            this.wineName = wineName;
        }
}

