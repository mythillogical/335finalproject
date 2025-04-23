package model;

import java.util.ArrayList;

public class Bill {
    private final ArrayList<Item> items;
    private final int people;
    private final Server server;
    private final int tableNum;
    private double tip;
    private boolean isPaid;

    public Bill(int tableNum, ArrayList<Item> items, int people, Server server) {
        this.items = items;
        this.people = people;
        this.server = server;
        this.tableNum = tableNum;
        this.tip = 0.0;
        this.isPaid = false;
    }
    
    public void setPaid(boolean choose) {
    	this.isPaid = choose;
    }
    
    public void addTips(double tips) {
    	this.tip += tips;
    }
    
    public double getTips() {
    	return tip;
    }
    
    public boolean getIsPaid() {
    	return this.isPaid;
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

	public ArrayList<Item> getItems() {
		return items;
	}
	
	public int getTableNum() {
		return tableNum;
	}
	
	public int getPeople() {
		return people;
	}
}
