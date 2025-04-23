package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.*;
import model.Menu;

import java.util.ArrayList;

public class OrderProcessingWindow extends JFrame {
    private RestaurantController controller;
    private int tableNumber;
    private OrderManagementPanel parentPanel;
    private JTabbedPane categoryTabs;
    private JTextArea orderSummaryArea;
    private ArrayList<Item> currentOrderItems;
    private ArrayList<Item> existingOrderItems;
    
    public OrderProcessingWindow(RestaurantController controller, int tableNumber, OrderManagementPanel parentPanel) {
        this.controller = controller;
        this.tableNumber = tableNumber;
        this.parentPanel = parentPanel;
        this.currentOrderItems = new ArrayList<>();
        this.existingOrderItems = new ArrayList<>();
        
        // Get existing items if any
        Bill bill = controller.getModel().getBillTable(tableNumber);
        if (bill != null) {
            existingOrderItems.addAll(bill.getItems());
        }
        
        setTitle("Table " + tableNumber + " - Order Processing");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initializeUI();
        updateOrderSummary();
    }
    
    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Table and server info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        Table table = controller.getModel().getTables().getTable(tableNumber);
        
        JLabel tableLabel = new JLabel("Table: " + tableNumber);
        tableLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel serverLabel = new JLabel("Server: " + (table.getServer() != null ? table.getServer().getName() : "None"));
        serverLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel guestsLabel = new JLabel("Guests: " + table.getNumPeople());
        guestsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        infoPanel.add(tableLabel);
        infoPanel.add(serverLabel);
        infoPanel.add(guestsLabel);
        
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Center split panel for menu and order
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.7);
        
        // Menu panel with categories
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBorder(BorderFactory.createTitledBorder("Menu Items"));
        
        categoryTabs = new JTabbedPane();
        Menu menu = controller.getModel().getMenu();
        
        for (String category : menu.getCatigories()) {
            JPanel categoryPanel = new JPanel(new GridLayout(0, 3, 8, 8));
            categoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            for (Item item : menu.getItemsByCategory(category)) {
                JButton itemButton = createMenuItemButton(item);
                categoryPanel.add(itemButton);
            }
            
            JScrollPane scrollPane = new JScrollPane(categoryPanel);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            categoryTabs.addTab(category, scrollPane);
        }
        
        menuPanel.add(categoryTabs, BorderLayout.CENTER);
        splitPane.setLeftComponent(menuPanel);
        
        // Order summary panel
        JPanel orderPanel = new JPanel(new BorderLayout(0, 10));
        orderPanel.setBorder(BorderFactory.createTitledBorder("Current Order"));
        
        orderSummaryArea = new JTextArea();
        orderSummaryArea.setEditable(false);
        orderSummaryArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane orderScrollPane = new JScrollPane(orderSummaryArea);
        orderPanel.add(orderScrollPane, BorderLayout.CENTER);
        
        // Order control buttons
        JPanel orderButtonsPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        
        JButton removeItemButton = new JButton("Remove Selected Item");
        removeItemButton.addActionListener(e -> removeSelectedItem());
        
        JButton clearButton = new JButton("Clear New Items");
        clearButton.addActionListener(e -> {
            currentOrderItems.clear();
            updateOrderSummary();
        });
        
        JButton submitButton = new JButton("Submit Order");
        submitButton.addActionListener(e -> submitOrder());
        
        orderButtonsPanel.add(removeItemButton);
        orderButtonsPanel.add(clearButton);
        orderButtonsPanel.add(submitButton);
        
        orderPanel.add(orderButtonsPanel, BorderLayout.SOUTH);
        splitPane.setRightComponent(orderPanel);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        // Bottom panel with close button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close Window");
        closeButton.addActionListener(e -> dispose());
        bottomPanel.add(closeButton);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JButton createMenuItemButton(Item item) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        
        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel priceLabel = new JLabel("$" + String.format("%.2f", item.getCost()));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        button.add(nameLabel, BorderLayout.CENTER);
        button.add(priceLabel, BorderLayout.SOUTH);
        
        button.setPreferredSize(new Dimension(120, 60));
        
        button.addActionListener(e -> {
            currentOrderItems.add(item);
            updateOrderSummary();
        });
        
        return button;
    }
    
    private void updateOrderSummary() {
        StringBuilder sb = new StringBuilder();
        double total = 0.0;
        
        // Show existing items first
        if (!existingOrderItems.isEmpty()) {
            sb.append("EXISTING ITEMS:\n");
            for (Item item : existingOrderItems) {
                sb.append(String.format("%-30s $%.2f\n", item.getName(), item.getCost()));
                total += item.getCost();
            }
            sb.append("\n");
        }
        
        // Show new items
        if (!currentOrderItems.isEmpty()) {
            sb.append("NEW ITEMS:\n");
            for (Item item : currentOrderItems) {
                sb.append(String.format("%-30s $%.2f\n", item.getName(), item.getCost()));
                total += item.getCost();
            }
            sb.append("\n");
        }
        
        // Show total
        sb.append(String.format("\nTOTAL: $%.2f", total));
        
        orderSummaryArea.setText(sb.toString());
    }
    
    private void removeSelectedItem() {
        // Get selected text and try to match with an item
        String selectedText = orderSummaryArea.getSelectedText();
        if (selectedText == null || selectedText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select an item in the order summary to remove.",
                "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Try to find the item name in the selection
        String itemName = null;
        for (Item item : currentOrderItems) {
            if (selectedText.contains(item.getName())) {
                itemName = item.getName();
                break;
            }
        }
        
        if (itemName != null) {
            // Find and remove the first matching item
            for (int i = 0; i < currentOrderItems.size(); i++) {
                if (currentOrderItems.get(i).getName().equals(itemName)) {
                    currentOrderItems.remove(i);
                    updateOrderSummary();
                    return;
                }
            }
        }
        
        // Also check existing items for removal
        for (Item item : existingOrderItems) {
            if (selectedText.contains(item.getName())) {
                if (controller.getModel().removeItemFromTable(tableNumber, item)) {
                    existingOrderItems.remove(item);
                    updateOrderSummary();
                    return;
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Could not remove existing item. Please try again.",
                        "Removal Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        
        JOptionPane.showMessageDialog(this, 
            "Could not identify the selected item. Please select an item name.",
            "Selection Error", JOptionPane.WARNING_MESSAGE);
    }
    
    private void submitOrder() {
        if (!currentOrderItems.isEmpty()) {
            // Add new items to the table
            controller.handleAddOrder(tableNumber, new ArrayList<>(currentOrderItems));
            
            JOptionPane.showMessageDialog(this, 
                "Order submitted successfully!",
                "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear current items and refresh
            currentOrderItems.clear();
            
            // Refresh existing items
            Bill bill = controller.getModel().getBillTable(tableNumber);
            if (bill != null) {
                existingOrderItems.clear();
                existingOrderItems.addAll(bill.getItems());
            }
            
            updateOrderSummary();
            parentPanel.refresh();
        } else {
            JOptionPane.showMessageDialog(this, 
                "No new items to submit. Add items to the order first.",
                "Empty Order", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}