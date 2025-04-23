package model;

import java.util.ArrayList;

public class Table {
	private int tableID;
	private final int capacity;
	private int numSeated;
	// private int orders;
	private boolean isOccupied;
	private Server server;
	private ArrayList<Item> items;

	public Table(int tableID, int capacity) {
		this.tableID = tableID;
		this.capacity = capacity;
		this.numSeated = 0;
		this.isOccupied = false;
		this.items = new ArrayList<>();
	}

	public int canSeat(int people) {
		if (numSeated != 0) return -1;
		return capacity - people;
	}

	/*
	 * @pre numSeated == 0
	 */
	public void seat(int people, Server server) {
		numSeated += people;
		this.isOccupied = true;
		this.server = server;
	}

	public void addItems(ArrayList<Item> items) {
		this.items.addAll(items);
		// orders++;
	}
	
	public void addItem(Item item) {
		items.add(item);
	}
	
	public boolean removeItem(String itemName) {
		return items.removeIf(item -> item.getName().equals(itemName));
	}

	public void close() {
		numSeated = 0;
		server = null;
		this.isOccupied = false;
		items = new ArrayList<>();
	}
	
	public Bill getBill() {
		return new Bill(tableID, items, numSeated, server);
	}
	
	public TableInfo getTableInfo() {
		return new TableInfo(tableID, capacity, numSeated);
	}
	
	public boolean getIsOccupied() {
		return this.isOccupied;
	}
	
	public int getTableId() {
		return this.tableID;
	}
	
	public int getCapacity() {
		return this.capacity;
	}
	
	public Server getServer() {
		return server;
	}
	
	public int getNumPeople() {
		return this.numSeated;
	}

	public ArrayList<Item> getItem() {
		return items;
	}

}