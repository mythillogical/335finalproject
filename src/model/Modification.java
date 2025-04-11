package model;

public class Modification {
	private String description;
	private double additionalCost;
	
	public Modification(String description, double additionalCost) {
		this.description = description;
		this.additionalCost = additionalCost;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public double getAdditionalCost() {
		return this.additionalCost;
	}
}
