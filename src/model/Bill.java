package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * This class represents a customer's bill including ordered items,
 * the server, party size, and tips. Also handles calculation of item total, tip,
 * and split costs. 
 * 
 * @author: Michael B, Michael D, Asif R, Mohammed A
 * 
 */
public class Bill implements Serializable {

	private static final long serialVersionUID = 1L;

    private final List<Item> items;
    private final int people;
    private final Server server;
    private final double tip;

    /* 
     * method constructs a bill without a tip. 
     *  
     *  */
    public Bill(ArrayList<Item> items, int people, Server server) {
        this(items, people, server, 0.0);
    }

    /*
     * method constructs a bill with a tip. 
     *  
     *  */
    public Bill(ArrayList<Item> items, int people, Server server, double tip) {
        this.items = new ArrayList<>(items);
        this.people = people;
        this.server = server;
        this.tip = tip;
    }

    /** 
     * returns the total cost of all ordered items
     * 
     *  */
    public double getItemsCost() {
        return Item.getItemsCost(new ArrayList<>(items));
    }

    /** 
     * returns the grand total (items and tip)
     *  
     *  */
    public double getTotalCost() {
        return getItemsCost() + tip;
    }

    /** 
     * split cost evenly among guests 
     * */
    public double getCostSplitEvenly() {
        return people == 0 ? 0 : getTotalCost() / people;
    }

    /*
     * gets the list of ordered items
     */
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    /*
     * gets the number of people
     */
    public int getPeople() {
        return people;
    }

    /*
     * gets the server responsible for this bill
     */
    public Server getServer() {
        return server;
    }

    /*
     * gets the tip amount on this bill
     */
    public double getTip() {
        return tip;
    }
}