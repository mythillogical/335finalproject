package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/* immutable menu / order item */
public class Item {

    private final String name;
    private final String category;
    private final double baseCost;
    private final List<Modification> mods;

    /* plain item – no modifications yet */
    public Item(String n, String c, double cost) {
        name      = n;
        category  = c;
        baseCost  = cost;
        mods      = new ArrayList<>();
    }

    /* copy-ctor used when duplicating an existing item (keeps its mods) */
    public Item(Item other) {
        name      = other.name;
        category  = other.category;
        baseCost  = other.baseCost;
        mods      = new ArrayList<>(other.mods);
    }

    /* ctor used by ui when a custom set of mods is chosen */
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

    /* read-only view for ui code */
    public List<Modification> getModifications() {
        return Collections.unmodifiableList(mods);
    }

    /* mutators (menu loading only) */
    public void addModification(Modification m) { mods.add(m); }

    /* ---------- cost helpers ---------- */
    public double getTotalCost() {
        double t = baseCost;
        for (Modification m : mods) t += m.getPrice();
        return t;
    }

    public static double getItemsCost(ArrayList<Item> items) {
        double c = 0;
        for (Item i : items) c += i.getTotalCost();
        return c;
    }

    /* convert this item’s mods back to csv “desc:price;…” */
    public String modsToCsv() {
        if (mods.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Modification m : mods) {
            sb.append(m.getDescription())
                    .append(':')
                    .append(m.getPrice())
                    .append(';');
        }
        sb.deleteCharAt(sb.length() - 1);        // trailing ;
        return sb.toString();
    }

    /** helper used by the gui to create a *new* item instance that
     *  keeps name/category/price but applies a fresh list of mods */
    public Item withModifications(List<Modification> chosen){
        return new Item(name, category, baseCost, chosen);
    }

    /* ---------- equality so JList.remove works correctly ---------- */
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
    public int hashCode() {
        return Objects.hash(name, category, baseCost, mods);
    }

    /* ---------- misc ---------- */
    @Override
    public String toString() {
        return name + " $" + baseCost;
    }
}
