package model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** Stores server name, tips and assigned tables. */
public class Server implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String name;
	private double tips = 0.0;

	/* transient – a server’s live-table set is NOT persisted */
	private transient final Set<Table> tables = new HashSet<>();

	public Server(String name) { this.name = name; }

	/* package-private hooks from Table */
	void addTable(Table t){ tables.add(t); }
	void removeTable(Table t){ tables.remove(t); }

	/* getters */
	public String getName()  { return name;  }
	public double getTotalTips(){ return tips; }
	public int    getNumTables(){ return tables.size(); }
	public Set<Table> getTables(){ return Collections.unmodifiableSet(tables); }

	/* tips */
	public void addTips(double amt){ tips += amt; }

	@Override public String toString(){
		return String.format("%s · %d tbl · $%.2f", name, getNumTables(), tips);
	}

	/* csv helpers */
	public static String header(){ return "Name,Tips\n"; }
	public String toCsv()        { return String.format("%s,%.2f\n", name, tips); }
}
