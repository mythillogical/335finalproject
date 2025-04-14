package model;

import java.util.ArrayList;

public class Bill {
    private final ArrayList<Item> items;
    private final int people;
    private final Server server;

    public Bill(ArrayList<Item> items, int people, Server server) {
        this.items = items;
        this.people = people;
        this.server = server;
    }

    public double getTotalCost() {
        return Item.getItemsCost(items);
    }

    public double getCostSplitEvenly() {
        return getTotalCost() / people;
    }

    public Server getServer() {
        return server;
    }
}
