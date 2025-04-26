package model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;

/*
 * represents a server (waiter/ waitress) in the restaurant system. stores the server's name,
 * total tips earned, and assigned tables. 
 * 
 * Author: Michael B, Michael D, Asif R, Mohammed A
 */
public class Server implements Serializable {

	// implementation for Serializable Interface
	private static final long serialVersionUID = 1L; 
	
	private final String name;
	private double tips = 0.0;
	
	// table is also serializable
	private final Set<Table> tables = new HashSet<>();

	/*
	 * constructs a Server with the given name
	 */
	public Server(String name) {
		this.name = name;
	}

	/*
	 * adds a table assignment to the server
	 */
	public void addTable(Table t) {
		tables.add(t);
	}

	/*
	 * removes a table assignment to the server
	 */
	public void removeTable(Table t) {
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
