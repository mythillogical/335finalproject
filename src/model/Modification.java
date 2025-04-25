package model;

import java.io.Serializable;

public class Modification implements Serializable {
	
	private static final long serialVersionUID = 1L;

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
