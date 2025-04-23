package model;

import java.util.ArrayList;
import java.util.List;

/** One physical table in the dining room. */
public class Table {

	private final int   tableID;
	private final int   capacity;

	private int         numSeated = 0;
	private boolean     occupied  = false;
	private Server      server    = null;
	private final List<Item> items = new ArrayList<>();

	/* -------------------------------------------------------------- */

	public Table(int tableID, int capacity) {
		this.tableID  = tableID;
		this.capacity = capacity;
	}

	/* -------------------------------------------------------------- */

	/** 0 = empty, >0 difference seats-left, −1 if already occupied. */
	public int canSeat(int guests) {
		if (occupied)          return -1;
		return capacity - guests;
	}

	/** Seats a party and links this table to the given server. */
	public void seat(int guests, Server s) {
		numSeated = guests;
		occupied  = true;
		server    = s;
		server.addTable(this);          // keep the Server in sync
	}

	/** Adds a whole ticket in one go. */
	public void addItems(List<Item> order) { items.addAll(order); }

	public boolean removeItem(Item i)      { return items.remove(i); }

	/** Clears the table and un-links it from the server. */
	public void close() {
		if (server != null) server.removeTable(this);
		items.clear();
		numSeated = 0;
		occupied  = false;
		server    = null;
	}

	/* -------------------------------------------------------------- */

	public int     getTableID()     { return tableID; }
	public int     getCapacity()    { return capacity; }
	public int     getNumSeated()   { return numSeated; }
	public boolean isOccupied()     { return occupied; }
	public Server  getServer()      { return server; }
	public List<Item> getItems()    { return new ArrayList<>(items); }

	public Bill getBill() {                   // server will never be null now
		return new Bill(new ArrayList<>(items), numSeated, server);
	}
}
