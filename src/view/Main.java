package view;

import javax.swing.SwingUtilities;

/** Simple launcher so IDE “Run” knows where to start. */
public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(RestaurantGUI::new);
	}
}
