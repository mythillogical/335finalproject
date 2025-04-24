package model;

import java.util.ArrayList;
import java.util.List;

/** snapshot of bill when table closes */
public class Bill {

    private final List<Item> items = null;
    private final int people = 0;
    private final Server server = null;

    /** cost of ordered items */
    public double getItemsCost() {
        return Item.getItemsCost(new ArrayList<>(items));
    }

    /** total cost including tip */
    public double getTotalCost() {
        return getItemsCost();
    }

    /** split cost evenly among people */
    public double getCostSplitEvenly() {
        return getTotalCost() / people;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    public int getPeople() {
        return people;
    }

    public Server getServer() {
        return server;
    }

}
