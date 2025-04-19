package view;

import javax.swing.*;

import model.*;
import model.Menu;

import java.awt.*;
import java.util.*;
import java.util.List;

public class RestaurantGUI extends JFrame {
	private List<Server> servers;
	private Menu menu;  // Map<String, ArrayList<Item>> menuMap
	
	private JButton OrderManegButton;  // this for order management button
	private JButton salesRepoButton;  // this is for sales report button
	private JButton serverRepoButton;  //this is for server report button
	private JPanel mainPanel;
	
	public RestaurantGUI() {
		menu = new Menu("Menu.csv");
		servers = new ArrayList<>();
		
		setTitle("Restaurant Management System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		
		initializeUI();
	}
	
	private void initializeUI() {
		mainPanel = new JPanel(new BorderLayout());
		JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		OrderManegButton = new JButton("Orders Manage");
		// add processing for Order Manage under hear
		
		centerPanel.add(OrderManegButton);
		
		salesRepoButton = new JButton("Sales Reports");
		// add processing for Sales Reports under hear
		
		centerPanel.add(salesRepoButton);
		
		serverRepoButton = new JButton("Severs Reports");
		// add processing for Severs Reports under hear
		
		centerPanel.add(serverRepoButton);
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		add(mainPanel);
	}
}
