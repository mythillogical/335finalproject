package model;

public class Modification {
	private String description;
	private double price;
	
	public Modification(String description, double price) {
		this.description = description;
		this.price = price;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	@Override
    public String toString() {
		return "\t- " + description + " = " + Double.toString(price);
	}
}
