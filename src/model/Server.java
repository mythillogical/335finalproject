package model;

import java.util.ArrayList;

public class Server {
	private String name;
	private double totalTips;
	// private ArrayList<Table> tables;
	
	public Server (String name) {
		this.name = name;
		this.totalTips = 0;
	}

	public void addTips(double amount) {
		this.totalTips += amount; 
	}

	public String getName() {
		return name;
	}

	public double getTotalTips() {
		return totalTips;
	}

	//public ArrayList<Table> getTables() { return new ArrayList<>(tables); }

	//public int getNumTables() { return tables.size(); }

	//public void assignTable(Table table) { tables.add(table); }

	//public void removeTable(Table table) { tables.remove(table); }

	@Override
    public String toString() {
    	return name + "\t" + Double.toString(totalTips);
    }
}
