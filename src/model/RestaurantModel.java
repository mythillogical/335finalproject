package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestaurantModel {
    private Menu menu;
    private ArrayList<Server> servers;
    private Tables tables;

    public RestaurantModel() {
        tables = new Tables("tables.txt");
        menu = new Menu("menu.csv");
    }

    public void addServer(String serverName) {
        servers.add(new Server(serverName));
    }

    public boolean removeServer(String serverName) {
        return servers.removeIf(server -> server.getName().equals(serverName));
    }

    public ArrayList<TableInfo> availableTables(int people) {
        return tables.getAvailable(people);
    }

    public ArrayList<TableInfo> getTables() {
        return tables.getTablesInfo();
    }

    public Bill closeTable(int id) {
        return tables.closeTable(id);
    }
}
