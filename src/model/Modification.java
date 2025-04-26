package model;

import java.io.Serializable;

/** Simple description + price pair used to customise an {@link Item}. */
public class Modification implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String description;
	private final double price;

	public Modification(String description, double price) {
		this.description = description;
		this.price       = price;
	}

	/* ---------- getters ---------- */
	public String getDescription() { return description; }
	public double getPrice()       { return price; }

	@Override
	public String toString() {
		return "\t- " + description + " = " + price;
	}
}
