package model;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantModel {

	// holds menu items
	private final Menu menu = new Menu("Menu.csv");
	// tracks all tables
	private final Tables tables = new Tables("tables.txt");
	// active servers mapped by name
	private final HashMap<String, Server> servers = new HashMap<>();
	// list of closed bills with tips
	private final ArrayList<Bill> closedTables = new ArrayList<>();

	// add a new server
	public void addServer(String name) {
		servers.put(name, new Server(name));
	}

	// remove an existing server by name
	public boolean removeServer(String name) {
		return servers.remove(name) != null;
	}

	// seat guests at a table with a server, return false if fail
	public boolean assignTableToServer(int id, int guests, String server) {
		return tables.assignTable(id, guests, servers.get(server));
	}

	// add order items to a table
	public void addOrderToTable(int id, ArrayList<Item> order) {
		tables.addItemsOrderToTable(id, order);
	}

	// close a table, record bill and add tip to server
	public void closeTable(int id, double tip) {
		Bill b = tables.getBillTable(id); // capture items and server
		tables.closeTable(id);
		if (b != null) {
			// create new bill including tip
			Bill withTip = new Bill(
					new ArrayList<>(b.getItems()),
					b.getPeople(),
					b.getServer(),
					tip
			);
			closedTables.add(withTip);
			servers.get(withTip.getServer().getName()).addTips(tip);
		}
	}

	// getters for model data
	public Menu getMenu() {
		return menu;
	}

	public Tables getTables() {
		return tables;
	}

	public HashMap<String, Server> getServers() {
		return servers;
	}

	public ArrayList<Bill> getClosedTables() {
		return closedTables;
	}
}