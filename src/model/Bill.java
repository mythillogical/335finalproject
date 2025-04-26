package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Immutable snapshot of what a party owes when a table is closed.
 * Stores: ordered items, party size, server name, and (optional) tip.
 * <p>
 * Serializable so an entire {@code List<Bill>} can be written to
 * {@code closedBills.dat} via {@link java.io.ObjectOutputStream}.
 */
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Item> items;
    private final int        people;
    private final String     serverName;   // store the name only (simpler)
    private final double     tip;

    /* construct with no tip */
    public Bill(ArrayList<Item> items, int people, Server server) {
        this(items, people, server, 0.0);
    }

    /* construct with a tip */
    public Bill(ArrayList<Item> items, int people, Server server, double tip) {
        this.items      = new ArrayList<>(items);
        this.people     = people;
        this.serverName = server != null ? server.getName() : "";
        this.tip        = tip;
    }

    /* ---------- helpers ---------- */
    public double getItemsCost()        { return Item.getItemsCost(new ArrayList<>(items)); }
    public double getTotalCost()        { return getItemsCost() + tip; }
    public double getCostSplitEvenly()  { return people == 0 ? 0 : getTotalCost() / people; }

    /* ---------- getters ---------- */
    public List<Item> getItems()  { return new ArrayList<>(items); }
    public int        getPeople() { return people; }
    public String     getServer() { return serverName; }
    public double     getTip()    { return tip; }
}
