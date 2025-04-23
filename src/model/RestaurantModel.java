package model;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantModel {

	private final Menu                    menu   = new Menu("Menu.csv");
	private final Tables                  tables = new Tables("tables.txt");
	private final HashMap<String, Server> servers = new HashMap<>();
	private final ArrayList<Bill>         closedTables = new ArrayList<>();

	/* -------------------------------------------------------------- */
	public void addServer(String name)          { servers.put(name, new Server(name)); }
	public boolean removeServer(String name)    { return servers.remove(name) != null; }

	public boolean assignTableToServer(int id, int guests, String server) {
		return tables.assignTable(id, guests, servers.get(server));
	}

	public void addOrderToTable(int id, ArrayList<Item> order) {
		tables.addItemsOrderToTable(id, order);
	}

	public void closeTable(int id, double tip) {
		Bill b = tables.getBillTable(id);     // snapshot before clearing
		tables.closeTable(id);

		if (b != null) {
			/* re-wrap to include the gratuity */
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

	/* -------------------------------------------------------------- */
	public Menu                    getMenu()         { return menu; }
	public Tables                  getTables()       { return tables; }
	public HashMap<String,Server>  getServers()      { return servers; }
	public ArrayList<Bill>         getClosedTables() { return closedTables; }
}
