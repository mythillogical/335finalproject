package model;

import java.util.ArrayList;

public class Table {
	private int tableId;
	private final int capacity;
	private int numSeated;
	// private int orders;
	private boolean assign;
	private Server server;
	private ArrayList<Item> items;

	public Table(int tableID, int capacity) {
		this.tableId = tableID;
		this.capacity = capacity;
		this.items = new ArrayList<>();
		this.assign = false;
		this.numSeated = 0;
	}

	public boolean canSeat(int people) {
		if (people > capacity || people < 0) return false;
		return true;
	}

	public void assign(int people, Server server) {
		this.numSeated += people;
		this.server = server;
		this.assign = true;
	}

	public void addItems(ArrayList<Item> items) {
		this.items.addAll(items);
		// this.orders++;
	}
	
	public void addItem(Item item) {
		this.items.add(item);
	}

	public Table close() {
	    Table closedTable = new Table(tableId, this.capacity);
	    closedTable.assign(this.numSeated, this.server);
	    closedTable.addItems(new ArrayList<>(this.items));
	    reset();
	    return closedTable;
	}

	private void reset() {
	    this.numSeated = 0;
	    this.server = null;
	    this.assign = false;
	    this.items.clear();
	}
	
	public double getTotalCost() {
        int totalCost = 0;
        for (Item item : items) {
        	totalCost += item.getTotalCost();
        }
        return totalCost;
    }
	
	public double getCostSplitEvenly() {
        return getTotalCost() / numSeated;
    }

	//public TableInfo getTableInfo() {
	//	return new TableInfo(tableId, capacity, numSeated);
	//}
	
	//public int getNumOrders() {
	//	return this.orders;
	//}

	public int getId() {
		return this.tableId;
	}
	
	public Server getServer() {
		return this.server;
	}
	
	public boolean getAssign() {
		return this.assign;
	}
	
	public String toString() {
		return "(" + Integer.toString(tableId) + ", " + Integer.toString(capacity) + ")";
	}

}