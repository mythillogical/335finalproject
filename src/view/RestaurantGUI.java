package view;
import javax.swing.*;
import model.*;
//import model.Menu;
import java.awt.*;
import java.awt.event.*;
//import java.util.*;
//import java.util.List;
public class RestaurantGUI extends JFrame {
    private RestaurantController controller;
    private JButton OrderManagButton; // this for order management button
    private JButton salesRepoButton; // this is for sales report button
    private JButton serverRepoButton; //this is for server report button
    private JButton severManagButton; // this is for server management
    private JPanel mainPanel;
    private ServerManagementPanel serverManagementPanel;
    private OrderManagementPanel orderManagementPanel;
    private SalesReportPanel salesReportPanel; // New panel for sales reports
    
    public RestaurantGUI() {
        // Initialize the model and controller
        RestaurantModel model = new RestaurantModel();
        controller = new RestaurantController(model);
        
        setTitle("Restaurant Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        severManagButton = new JButton("Server Management");
        OrderManagButton = new JButton("Order Management");
        serverRepoButton = new JButton("Server Reports");
        salesRepoButton = new JButton("Sales Reports");
        initializeUI();
        setupActionListeners();
    }
    
    private void initializeUI() {
        mainPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.add(severManagButton);
        // add processing for Order Manage under hear
        centerPanel.add(OrderManagButton);
        // add processing for Sales Reports under hear
        centerPanel.add(salesRepoButton);
        // add processing for Severs Reports under hear
        centerPanel.add(serverRepoButton);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
        
        // Initialize server management panel
        serverManagementPanel = new ServerManagementPanel(controller);
        
        // Initialize order management panel with controller
        orderManagementPanel = new OrderManagementPanel(controller);
        
        // Initialize sales report panel with controller
        salesReportPanel = new SalesReportPanel(controller);
    }
    
    private void setupActionListeners() {
        severManagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showServerManagementPanel();
            }
        });
        
        // Add action listener to the back button in server management panel
        serverManagementPanel.getBackButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMainPanel();
            }
        });
        
        // Add action listener for Order Management button
        OrderManagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOrderManagementPanel();
            }
        });
        
        // Add action listener to the back button in order management panel
        orderManagementPanel.getBackButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMainPanel();
            }
        });
        
        // Add action listener for Sales Report button
        salesRepoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSalesReportPanel();
            }
        });
        
        // Add action listener to the back button in sales report panel
        salesReportPanel.getBackButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMainPanel();
            }
        });
    }
    
    private void showServerManagementPanel() {
        // Remove current panel and add server management panel
        getContentPane().removeAll();
        getContentPane().add(serverManagementPanel);
        getContentPane().revalidate();
        getContentPane().repaint();
    }
    
    private void showOrderManagementPanel() {
        // Remove current panel and add order management panel
        orderManagementPanel.refresh(); // Refresh data before showing
        getContentPane().removeAll();
        getContentPane().add(orderManagementPanel);
        getContentPane().revalidate();
        getContentPane().repaint();
    }
    
    private void showSalesReportPanel() {
        // Remove current panel and add sales report panel
        salesReportPanel.refresh(); // Refresh data before showing
        getContentPane().removeAll();
        getContentPane().add(salesReportPanel);
        getContentPane().revalidate();
        getContentPane().repaint();
    }
    
    private void showMainPanel() {
        // Remove current panel and add main panel
        getContentPane().removeAll();
        getContentPane().add(mainPanel);
        getContentPane().revalidate();
        getContentPane().repaint();
    }
}