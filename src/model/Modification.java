package model;

import java.io.Serializable;

/* the class represents a modification (customization) that can be added to a menu item,
 * such as "extra cheese" or "no onions". Each modification has description and an
 * associated price adjustment. Implements Serializable to allow modifications to be saved
 * with items in persistence. Example: "Add bacon: $1.50"
 * 
 *  @author: Michael B, Michael D, Asif R, Mohammed A
 */
public class Modification implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String description;
	private double price;
	
	/*
	 * constructs a modification with the given description and price
	 */
	public Modification(String description, double price) {
		this.description = description;
		this.price = price;
	}
	
	/*
	 * returns the description of this modification
	 */
	public String getDescription() {
		return this.description;
	}
	
	/*
	 * returns a string representation of the modification
	 */
	public double getPrice() {
		return this.price;
	}
	
	@Override
    public String toString() {
		return "\t- " + description + " = " + Double.toString(price);
	}
}
