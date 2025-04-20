package model;

import java.util.ArrayList;

public class RestaurantModel {
    private Menu menu;
    private Tables tables;

    public RestaurantModel() {
        tables = new Tables("tables.txt");
        menu = new Menu("menu.csv");
    }
}
