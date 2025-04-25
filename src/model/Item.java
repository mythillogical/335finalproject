package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.io.Serializable;

/* the class represents a single menu item that can be part of a customer's
 * order. each item has a name, category, base cost, and optional modifications.
 * this class is immutable with respect to name, category, and base cost, but supports
 * modification management. Implements Serializable to support saving as part of a
 * persisted order.
 * 
 *  @author: Michael B, Michael D, Asif R, Mohammed A
 */
public class Item implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private final String name;
    private final String category;
    private final double baseCost;
    private List<Modification> mods;

    /* 
     * constructs basic item – no modifications 
     */
    public Item(String n, String c, double cost) {
        name = n;
        category = c;
        baseCost = cost;
        mods = new ArrayList<>();
    }

    /* 
     * copy-constructor, used when duplicating an existing item (keeps its mods)
     */
    public Item(Item other) {
        name = other.name;
        category = other.category;
        baseCost = other.baseCost;
        mods = new ArrayList<>(other.mods);
    }

    /* 
     * constructs used by item with predefined modifications 
     */
    public Item(String n, String c, double cost, List<Modification> chosen) {
        name = n;
        category = c;
        baseCost = cost;
        mods = new ArrayList<>(chosen);
    }

    /* basic getters */
    public String getName()     { return name; }
    public String getCategory() { return category; }
    public double getCost()     { return baseCost; }

    /* read-only view for UI code */
    public List<Modification> getModifications() {
        return Collections.unmodifiableList(mods);
    }

    /* mutators (menu loading) */
    public void addModification(Modification m) { mods.add(m); }

    /* cost helpers */
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

    /* convert this item’s mods back to csv “name:price;…” */
    public String modsToCsv() {
        if (mods.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Modification m : mods) {
            sb.append(m.getDescription()).append(':').append(m.getPrice()).append(';');
        }
        sb.deleteCharAt(sb.length() - 1);        // trailing ;
        return sb.toString();
    }

    @Override 
    public String toString() {
        return name + " $" + baseCost;
    }
}
