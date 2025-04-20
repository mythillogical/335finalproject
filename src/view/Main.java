package view;

import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
	        try {
	            new RestaurantGUI().setVisible(true);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    });
	}

}
