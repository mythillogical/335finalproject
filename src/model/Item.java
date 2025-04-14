package model;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private String name;
    private String category;
    private double baseCost;
    private List<Modification> modifications;

    public Item(String name, String category, double baseCost) {
        this.name = name;
        this.category = category;
        this.baseCost = baseCost;
        this.modifications = new ArrayList<>();
    }
    
    public String getName() {
    	return name;
    }
    
    public String getCategory() {
        return category;
    }
    
    public double getCost() {
    	return baseCost;
    }
    
    public double getTotalCost() {
    	double totalCost = baseCost;
        for (Modification mod : modifications) {
        	totalCost += mod.getPrice();
        }
        return totalCost;
    }

    public void addModification(Modification mod) {
        modifications.add(mod);
    }

    public static double getItemsCost(ArrayList<Item> items) {
        double cost = 0;
        for (Item item : items) {
            cost += item.getTotalCost();
        }
        return cost;
    }
    
    @Override
    public String toString() {
    	String str = name + " = " + Double.toString(this.baseCost);
    	if (modifications.size() > 0) {
    		str += "\n  Modifications:\n";
    		for (int i = 0; i < modifications.size(); i++) {
    			str += modifications.get(i).toString();
    			if (i != modifications.size() - 1) {
    				str += "\n";
    			}
    		}
    	}
    	return str;
    }
    
}
