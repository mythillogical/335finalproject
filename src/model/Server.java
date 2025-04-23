package model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** A waiter / waitress currently working in the restaurant. */
public class Server {

	private final String  name;
	private       double  tips = 0.0;
	private final Set<Table> tables = new HashSet<>();

	/* -------------------------------------------------------------- */

	public Server(String name) { this.name = name; }

	/* bookkeeping (package-private – only Table should call these) */
	void addTable(Table t)    { tables.add(t); }
	void removeTable(Table t) { tables.remove(t); }

	/* -------------------------------------------------------------- */

	public String  getName()          { return name; }
	public double  getTotalTips()     { return tips; }
	public int     getNumTables()     { return tables.size(); }
	public Set<Table> getTables()     { return Collections.unmodifiableSet(tables); }

	public void addTips(double amount){ tips += amount; }

	@Override public String toString() {
		return String.format("%s · %d tbl · $%.2f", name, getNumTables(), tips);
	}
}
