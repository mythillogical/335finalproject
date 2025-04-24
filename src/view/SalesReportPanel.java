package view;

import javax.swing.*;

import model.RestaurantController;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.text.NumberFormat;

public class SalesReportPanel extends JPanel {
    private RestaurantController controller;
    private JButton backButton;
    private JTabbedPane tabbedPane;
    private JPanel salesByItemPanel;
    private JPanel revenueByItemPanel;
    private NumberFormat currencyFormat;

    public SalesReportPanel(RestaurantController controller) {
        this.controller = controller;
        this.currencyFormat = NumberFormat.getCurrencyInstance();
        
        setLayout(new BorderLayout());
        initializeUI();
    }
    
    private void initializeUI() {
        // Initialize back button
        backButton = new JButton("Back to Main Menu");
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);
        
        // Create tabbed pane for different reports
        tabbedPane = new JTabbedPane();
        
        // Create sales by item panel
        salesByItemPanel = new JPanel(new BorderLayout());
        salesByItemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create revenue by item panel
        revenueByItemPanel = new JPanel(new BorderLayout());
        revenueByItemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add tabs to the tabbed pane
        tabbedPane.addTab("Sales by Item", salesByItemPanel);
        tabbedPane.addTab("Revenue by Item", revenueByItemPanel);
        
        // Add tabbed pane to this panel
        add(tabbedPane, BorderLayout.CENTER);
        
        // Populate data initially
        refresh();
    }
    
    public void refresh() {
        refreshSalesByItem();
        refreshRevenueByItem();
    }
    
    private void refreshSalesByItem() {
        salesByItemPanel.removeAll();
        
        // Get sales data from controller
        List<Map.Entry<String, Integer>> salesData = controller.getSalesByItemSorted();
        
        // Create table model with item name and quantity columns
        String[] columnNames = {"Item Name", "Quantity Sold"};
        Object[][] data = new Object[salesData.size()][2];
        
        int index = 0;
        int totalItems = 0;
        for (Map.Entry<String, Integer> entry : salesData) {
            data[index][0] = entry.getKey();
            data[index][1] = entry.getValue();
            totalItems += entry.getValue();
            index++;
        }
        
        // Create table and scroll pane
        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Add summary panel at the bottom
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summaryPanel.add(new JLabel("Total Items Sold: " + totalItems));
        
        // Add components to the panel
        salesByItemPanel.add(new JLabel("Sales by Item Report", SwingConstants.CENTER), BorderLayout.NORTH);
        salesByItemPanel.add(scrollPane, BorderLayout.CENTER);
        salesByItemPanel.add(summaryPanel, BorderLayout.SOUTH);
        
        salesByItemPanel.revalidate();
        salesByItemPanel.repaint();
    }
    
    private void refreshRevenueByItem() {
        revenueByItemPanel.removeAll();
        
        // Get revenue data from controller
        List<Map.Entry<String, Double>> revenueData = controller.getRevenueByItem();
        
        // Create table model with item name and revenue columns
        String[] columnNames = {"Item Name", "Revenue"};
        Object[][] data = new Object[revenueData.size()][2];
        
        int index = 0;
        double totalRevenue = 0.0;
        for (Map.Entry<String, Double> entry : revenueData) {
            data[index][0] = entry.getKey();
            data[index][1] = currencyFormat.format(entry.getValue());
            totalRevenue += entry.getValue();
            index++;
        }
        
        // Create table and scroll pane
        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Add summary panel at the bottom
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summaryPanel.add(new JLabel("Total Revenue: " + currencyFormat.format(totalRevenue)));
        
        // Add components to the panel
        revenueByItemPanel.add(new JLabel("Revenue by Item Report", SwingConstants.CENTER), BorderLayout.NORTH);
        revenueByItemPanel.add(scrollPane, BorderLayout.CENTER);
        revenueByItemPanel.add(summaryPanel, BorderLayout.SOUTH);
        
        revenueByItemPanel.revalidate();
        revenueByItemPanel.repaint();
    }
    
    public JButton getBackButton() {
        return backButton;
    }
}