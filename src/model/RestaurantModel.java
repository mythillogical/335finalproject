package model;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantModel {
    private Menu menu;
    private Tables tables;
    private HashMap<String, Server> servers;
    private ArrayList<Table> assignTables;

    public RestaurantModel() {
    	assignTables = new ArrayList<>();
        tables = new Tables("tables.txt");
        menu = new Menu("menu.csv");
    }

    public void addServer(String serverName) {
        servers.put(serverName, new Server(serverName));
    }

    public boolean removeServer(String serverName) {
        return servers.remove(serverName) != null;
    }

    public ArrayList<Table> availableTables(int people) {
        return tables.getAvailable(people);
    }
    
    public void assignTable(int tableNum, int numPeople, String serverName) {
    	for (Table table : tables.getTables()) {
    		if (table.getId() == tableNum) {
    			
    		}
    	}
    }
    
    
    public Table closeTable(int tableId, double tip) {
        return tables.closeTable(tableId);
    }
    
    public ArrayList<Table> getTables() {
        return tables.getTables();
    }
    
    public ArrayList<Table> getAssignTables() {
    	return this.assignTables;
    }
    
    public Menu getMenu() {
    	return this.menu;
    }
}
