package view;

import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			RestaurantGUI app = new RestaurantGUI();
			app.setVisible(true);
		});
	}

}
