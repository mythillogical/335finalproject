package model;

import java.util.ArrayList;
import java.util.List;

/** Immutable bill for a single table. */
public class Bill {

    private final ArrayList<Item> items;
    private final int     people;
    private final Server  server;

    public Bill(ArrayList<Item> items, int people, Server server) {
        this.items  = new ArrayList<>(items);   // defensive copy
        this.people = people;
        this.server = server;
    }

    //--

    public double getTotalCost()           { return Item.getItemsCost(items); }
    public double getCostSplitEvenly()     { return getTotalCost() / people;  }
    public Server getServer()              { return server; }

    /** Exposes the items that make up this bill (read-only). */
    public List<Item> getItems()           { return List.copyOf(items); }
}
