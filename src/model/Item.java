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
    	double totalCost = baseCost;
        for (Modification mod : modifications) {
        	totalCost += mod.getAdditionalCost();
        }
        return totalCost;
    }

    public void addModification(Modification mod) {
        modifications.add(mod);
    }
}
