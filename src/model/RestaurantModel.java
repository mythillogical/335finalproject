package model;

import java.util.ArrayList;

public class RestaurantModel {
	private Menu menu;
	private Tables tables;
	private ArrayList<Server> servers;
	private ArrayList<Bill> closedTables;

	public RestaurantModel() {
		this.tables = new Tables("tables.txt");
		this.menu = new Menu("menu.csv");
		this.closedTables = new ArrayList<>();
		this.servers = new ArrayList<>();
	}
	
	public void addServer(String name) {
		servers.add(new Server(name));
	}
	
	public boolean removeServer(String name) {
		return servers.removeIf(server -> server.getName().equals(name));
	}
	
	
	public boolean assignTableToServer(int numTable, int numPeople ,Server server) {
		return tables.assignTable(numTable, numPeople, server);
	}
	
	public void addOrderToTable(int numTable, ArrayList<Item> order) {
		tables.addItemsOrderToTable(numTable, order);
	}
	
	public boolean removeItemFromTable(int numTable, Item item) {
		return tables.removeItemFromTable(numTable, item);
	}
	
	public Bill closeTable(int numTable) {
		Bill bill = tables.closeTable(numTable);
		if (bill != null) {
			this.closedTables.add(bill);
		}
		return bill;
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public Tables getTables() {
		return tables;
	}
	
	public ArrayList<Server> getServers(){
		return servers;
	}
	
	public ArrayList<Bill> getClosedTables() {
		return this.closedTables;
	}

}
