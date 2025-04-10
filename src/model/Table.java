package model;

import java.util.ArrayList;

public class Table {
	private int tableID;
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

	public double close() {
		numSeated = 0;
		server = null;
		items = new ArrayList<>();
		return getCost(items);
	}

	public static double getCost(ArrayList<Item> items) {
		double cost = 0;
		for (Item item : items) {
			cost += item.getCost();
		}
		return cost;
	}
}
