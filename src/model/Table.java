package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* one real-world table */
public class Table implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private final int  id;          // number printed on the table
	private final int  cap;         // max seats

	private int     seated = 0;     // current party size
	private boolean occ    = false; // is someone sitting here?
	private Server  srv    = null;  // waiter/waitress in charge
	private final List<Item> items = new ArrayList<>();   // live order

	/* ctor */
	public Table(int id, int cap) {
		this.id  = id;
		this.cap = cap;
	}

	/* seat math:  0 = empty, >0 = seats left, -1 = taken */
	public int canSeat(int guests) {
		return occ ? -1 : cap - guests;
	}

	/* seat a party + hook table into server object */
	public void seat(int guests, Server s) {
		seated = guests;
		occ    = true;
		srv    = s;
		srv.addTable(this);          // keep server state up-to-date
	}

	/* order helpers */
	public void addItems(List<Item> order) { items.addAll(order); }
	public boolean removeItem(Item i)      { return items.remove(i); }

	/* clear everything; detach from server */
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

	/* snapshot of what’s owed right now */
	public Bill getBill() {
		return new Bill(new ArrayList<>(items), seated, srv);
	}
}
