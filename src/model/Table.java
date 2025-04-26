package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * this represents a physical table in the restaurant. tracks the table's ID, capacity, current
 * occupancy, assigned server, and active orders. Supports seating guests, managing orders, and generating
 * bills. 
 * 
 * author: Michael B, Michael D, Asif R, Mohammed A
 */
public class Table implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private final int  id;          // number printed on the table
	private final int  cap;         // max seats

	private int     seated = 0;     // current party size
	private boolean occ    = false; // is someone sitting here?
	private Server  srv    = null;  // waiter/waitress in charge
	private final List<Item> items = new ArrayList<>();   // live order

	/*
	 * constructs a Table with a given ID and seating capacity
	 */
	public Table(int id, int cap) {
		this.id  = id;
		this.cap = cap;
	}

	/*
	 * determines if the table can seat a new party of the given size
	 */
	public int canSeat(int guests) {
		return occ ? -1 : cap - guests;
	}

	/*
	 * seats a party at the table and assigns a server
	 */
	public void seat(int guests, Server s) {
		seated = guests;
		occ    = true;
		srv    = s;
		srv.addTable(this);          // keep server state up-to-date
	}

	/*
	 * adds ordered items to the table's current order
	 */
	public void addItems(List<Item> order) { items.addAll(order); }
	
	/*
	 * removes an item from the table's current order
	 */
	public boolean removeItem(Item i)      { return items.remove(i); }

	/*
	 * clears the tables after payment: empties order, unassigns server, resets state
	 */
	public void close() {
		if (srv != null) srv.removeTable(this);
		items.clear();
		seated = 0;
		occ    = false;
		srv    = null;
	}

	/* getters */
	public int        getTableID() { return id; }
	public int        getCapacity(){ return cap; }
	public int        getNumSeated(){ return seated; }
	public boolean    isOccupied() { return occ; }
	public Server     getServer()  { return srv; }
	public List<Item> getItems()   { return new ArrayList<>(items); }

	/*
	 * returns a bill representing the current amount owed for the table
	 */
	public Bill getBill() {
		return new Bill(new ArrayList<>(items), seated, srv);
	}
}
