package view;

/* Main entry point for the Restaurant Management System application.
* 
* Initializes the graphical user interface (GUI) and links the 
* model, view, and controller components following the MVC pattern.
* 
* This class creates the main application window and starts the program.
* 
* Author: Michael B, Michael D, Asif R, Mohammed A
* Course: CSc 335 - Object-Oriented Programming and Design
* Semester: Spring 2025
*/
public class Main {
	public static void main(String[] args) {
		// delegate to RestaurantGUI
		RestaurantGUI.main(args);
	}
}
