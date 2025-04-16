package model;

import java.util.ArrayList;

public class Table {
	private int tableId;
	private final int capacity;
	private int numSeated;
	private int orders;
	private Server server;
	private ArrayList<Item> items;

	public Table(int tableID, int capacity) {
		this.tableID = tableID;
		this.capacity = capacity;
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
		this.server = server;
	}

	public void addItems(ArrayList<Item> items) {
		this.items.addAll(items);
		orders++;
	}

	public Bill close() {
		Bill bill = new Bill(items, numSeated, server);
		numSeated = 0;
		server = null;
		items = new ArrayList<>();
		return bill;
	}

	public TableInfo getTableInfo() {
		return new TableInfo(tableId, capacity, numSeated);
	}

	public int getId() {
		return tableId;
	}

}