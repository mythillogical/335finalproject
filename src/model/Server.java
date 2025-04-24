package model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// stores server name, tips, and assigned tables
public class Server {

	private final String name;
	private double tips = 0.0;
	private final Set<Table> tables = new HashSet<>();

	// ctor: init server with name
	public Server(String name) {
		this.name = name;
	}

	// add a table assignment
	void addTable(Table t) {
		tables.add(t);
	}

	// remove a table assignment
	void removeTable(Table t) {
		tables.remove(t);
	}

	// get server name
	public String getName() {
		return name;
	}

	// get total tips amount
	public double getTotalTips() {
		return tips;
	}

	// get count of tables served
	public int getNumTables() {
		return tables.size();
	}

	// get set of assigned tables (read-only)
	public Set<Table> getTables() {
		return Collections.unmodifiableSet(tables);
	}

	// add tip amount
	public void addTips(double amount) {
		tips += amount;
	}

	// format server info for display
	@Override
	public String toString() {
		return String.format("%s · %d tbl · $%.2f", name, getNumTables(), tips);
	}
}
