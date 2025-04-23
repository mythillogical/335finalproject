package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Immutable snapshot of everything owed for a table once it is closed.
 * A bill is defined by the ordered items, the party size, the server
 * who handled the table and (optionally) the tip that was left.
 */
public class Bill {

    private final List<Item> items;
    private final int        people;
    private final Server     server;
    private final double     tip;

    /* ------------------------------------------------------------------
       CONSTRUCTORS
       ------------------------------------------------------------------ */

    /** Original signature – tip defaults to 0. */
    public Bill(ArrayList<Item> items, int people, Server server) {
        this(items, people, server, 0.0);
    }

    /** New signature that also stores the gratuity. */
    public Bill(ArrayList<Item> items, int people,
                Server server, double tip) {
        this.items  = new ArrayList<>(items);
        this.people = people;
        this.server = server;
        this.tip    = tip;
    }

    /* ------------------------------------------------------------------
       BASIC QUERIES
       ------------------------------------------------------------------ */

    /** Base cost of all ordered items. */
    public double getItemsCost() {
        return Item.getItemsCost(new ArrayList<>(items));
    }

    /** Base cost + tip (if any). */
    public double getTotalCost() {
        return getItemsCost() + tip;
    }

    public double getCostSplitEvenly() {
        return people == 0 ? 0 : getTotalCost() / people;
    }

    public List<Item> getItems() { return new ArrayList<>(items); }
    public int        getPeople() { return people; }
    public Server     getServer() { return server; }
    public double     getTip()    { return tip; }
}
