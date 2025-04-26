package model;

import java.io.Serializable;
import java.util.*;

/**
 * Immutable menu / order item.
 * Serializable so it can be embedded inside a serialised {@link Bill}.
 */
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final String category;
    private final double baseCost;
    private final List<Modification> mods;

    /* plain item – no modifications */
    public Item(String n, String c, double cost) {
        name      = n;
        category  = c;
        baseCost  = cost;
        mods      = new ArrayList<>();
    }

    /* copy-ctor */
    public Item(Item other) {
        name      = other.name;
        category  = other.category;
        baseCost  = other.baseCost;
        mods      = new ArrayList<>(other.mods);
    }

    /* ctor with predefined modifications */
    public Item(String n, String c, double cost, List<Modification> chosen) {
        name      = n;
        category  = c;
        baseCost  = cost;
        mods      = new ArrayList<>(chosen);
    }

    /* ---------- basic getters ---------- */
    public String getName()        { return name; }
    public String getCategory()    { return category; }
    public double getCost()        { return baseCost; }

    /* read-only view for UI */
    public List<Modification> getModifications() {
        return Collections.unmodifiableList(mods);
    }

    /* menu-loading helper */
    public void addModification(Modification m) { mods.add(m); }

    /* ---------- cost helpers ---------- */
    public double getTotalCost() {
        double t = baseCost;
        for (Modification m : mods) t += m.getPrice();
        return t;
    }
    public static double getItemsCost(List<Item> items) {
        double c = 0;
        for (Item i : items) c += i.getTotalCost();
        return c;
    }

    /* mods → CSV (still used when writing Menu.csv) */
    public String modsToCsv() {
        if (mods.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Modification m : mods)
            sb.append(m.getDescription())
              .append(':')
              .append(m.getPrice())
              .append(';');
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /* create a fresh instance with new mods */
    public Item withModifications(List<Modification> chosen) {
        return new Item(name, category, baseCost, chosen);
    }

    /* equality so JList.remove works properly */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item other = (Item) o;
        return  Double.compare(other.baseCost, baseCost) == 0 &&
                Objects.equals(name, other.name) &&
                Objects.equals(category, other.category) &&
                Objects.equals(mods, other.mods);
    }
    @Override
    public int hashCode() { return Objects.hash(name, category, baseCost, mods); }

    @Override
    public String toString() { return name + " $" + baseCost; }
}
