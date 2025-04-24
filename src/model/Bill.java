package model;

import java.util.ArrayList;
import java.util.List;

/** snapshot of bill when table closes */
public class Bill {

    private final List<Item> items;
    private final int people;
    private final Server server;
    }

    /** cost of ordered items */
    public double getItemsCost() {
        return Item.getItemsCost(new ArrayList<>(items));
    }

    /** total cost including tip */
    public double getTotalCost() {
        return getItemsCost() + tip;
    }

    /** split cost evenly among people */
    public double getCostSplitEvenly() {
        return people == 0 ? 0 : getTotalCost() / people;
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


