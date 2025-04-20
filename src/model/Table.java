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

	public Bill close() {
		Bill bill = new Bill(items, numSeated, server);
		reset();
		return bill;
	}
	
	private void reset() {
		numSeated = 0;
		server = null;
		this.isOccupied = false;
		items = new ArrayList<>();
	}

	public TableInfo getTableInfo() {
		return new TableInfo(tableID, capacity, numSeated);
	}

	public int getId() {
		return tableID;
	}
	
	public boolean getIsOccupied() {
		return this.isOccupied;
	}

}