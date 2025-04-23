package model;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantModel {
	private Menu menu;
	private Tables tables;
	private HashMap<String, Server> servers;
	private ArrayList<Bill> closedTables;

	public RestaurantModel() {
		this.tables = new Tables("tables.txt");
		this.menu = new Menu("Menu.csv");
		this.closedTables = new ArrayList<>();
		this.servers = new HashMap<>();
	}
	
	public void addServer(String name) {
		servers.put(name, new Server(name));
	}
	
	public boolean removeServer(String name) {
		return servers.remove(name) != null;
	}
	
	
	public boolean assignTableToServer(int numTable, int numPeople ,String serverName) {
		return tables.assignTable(numTable, numPeople, servers.get(serverName));
	}
	
	public void addOrderToTable(int numTable, ArrayList<Item> order) {
		tables.addItemsOrderToTable(numTable, order);
	}
	
	public boolean removeItemFromTable(int numTable, Item item) {
		return tables.removeItemFromTable(numTable, item);
	}
	
	public void closeTable(int numTable, double tip) {
		Bill bill = tables.getBillTable(numTable);
		tables.closeTable(numTable);
		if (bill != null) {
			this.closedTables.add(bill);
			String serverName = bill.getServer().getName();
			servers.get(serverName).addTips(tip);
		}
	}
	
	public ArrayList<Table> getAvalbleTables() {
		return tables.getNotOqubiedTable();
	}
	
	public Bill getBillTable(int numTable) {
		return tables.getBillTable(numTable);
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public Tables getTables() {
		return tables;
	}
	
	public HashMap<String, Server> getServers(){
		return servers;
	}
	
	public ArrayList<Bill> getClosedTables() {
		return this.closedTables;
	}

}
