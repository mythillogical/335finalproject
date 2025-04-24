package view;

import javax.swing.*;
=======


import model.*;
import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.util.*;
import java.util.List;
public class RestaurantGUI extends JFrame {
    private RestaurantController controller;
    private JButton OrderManagButton; // this for order management button
    private JButton salesRepoButton; // this is for sales report button
    private JButton serverRepoButton; //this is for server report button
    private JButton severManagButton; // this is for server management
    private JPanel mainPanel;
    private ServerManagementPanel serverManagementPanel;
    private OrderManagementPanel orderManagementPanel;
    
    public RestaurantGUI() {
        // Initialize the model and controller
        RestaurantModel model = new RestaurantModel();
        controller = new RestaurantController(model);
        
=======

public class RestaurantGUI extends JFrame {

    /* mvc */
    private final RestaurantModel  model = new RestaurantModel();
    private final RestaurantView   view  = new RestaurantView(this);
    private final RestaurantController controller =
            new RestaurantController(model, view);

    /* nav buttons (tables first, then menu editor) */
    private final JButton btnTables = new JButton("Tables");
    private final JButton btnMenu   = new JButton("Menu Editor");
    private final JButton btnServ   = new JButton("Server Management");
    private final JButton btnOrder  = new JButton("Order Management");
    private final JButton btnSales  = new JButton("Sales Report");

    /* lazy panels */
    private MenuEditorPanel        menuPane;
    private ServerManagementPanel  servPane;
    private OrderManagementPanel   orderPane;
    private SalesReportPanel       salesPane;

    public RestaurantGUI() {

        setTitle("Restaurant Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000,700);
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
    }
    
    private void setupActionListeners() {
        severManagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showServerManagementPanel();
            }


        /* toolbar */
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.add(btnTables);
        bar.add(btnMenu);
        bar.add(btnServ);
        bar.add(btnOrder);
        bar.add(btnSales);
        add(bar, BorderLayout.NORTH);

        /* actions */
        btnTables.addActionListener(e -> {
            view.displayTables(model.getTables().getTablesInfo());
            swapCenter(view.getRootPanel());
        });

        btnMenu.addActionListener(e -> {
            if (menuPane==null) menuPane = new MenuEditorPanel(controller);
            swapCenter(menuPane);
        });

        btnServ.addActionListener(e -> {
            if (servPane==null) servPane = new ServerManagementPanel(controller);
            swapCenter(servPane);
        });

        btnOrder.addActionListener(e -> {
            if (orderPane==null) orderPane = new OrderManagementPanel(controller);
            swapCenter(orderPane);

        });

        btnSales.addActionListener(e -> {
            if (salesPane==null) salesPane = new SalesReportPanel(controller);
            swapCenter(salesPane);
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


        /* default */
        view.displayTables(model.getTables().getTablesInfo());
        swapCenter(view.getRootPanel());
        setVisible(true);

    }

    /* helper swaps centre comp */
    private void swapCenter(JComponent next){
        Container cp=getContentPane();
        BorderLayout bl=(BorderLayout)cp.getLayout();
        Component old=bl.getLayoutComponent(BorderLayout.CENTER);
        if(old!=null) cp.remove(old);
        cp.add(next,BorderLayout.CENTER);
        revalidate(); repaint();
    }

    
    private void showOrderManagementPanel() {
        // Remove current panel and add order management panel
        orderManagementPanel.refresh(); // Refresh data before showing
        getContentPane().removeAll();
        getContentPane().add(orderManagementPanel);
        getContentPane().revalidate();
        getContentPane().repaint();
    }
    
    private void showMainPanel() {
        // Remove current panel and add main panel
        getContentPane().removeAll();
        getContentPane().add(mainPanel);
        getContentPane().revalidate();
        getContentPane().repaint();


    public static void main(String[] a){
        SwingUtilities.invokeLater(RestaurantGUI::new);

    }
}
