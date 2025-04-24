package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.*;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderManagementPanel extends JPanel {
    private RestaurantController controller;
    private JButton backButton;
    private JComboBox<String> serverComboBox;
    private JPanel availableTablesPanel;
    private JPanel inProgressTablesPanel;
    private JPanel closedTablesPanel;
    private JTextArea orderSummaryArea;
    private int selectedTable = -1;
    
    public OrderManagementPanel(RestaurantController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Order Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        backButton = new JButton("Back to Main Menu");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(backButton);
        headerPanel.add(buttonPanel, BorderLayout.WEST);
        
        // Create tables container
        JPanel tablesContainer = new JPanel(new GridLayout(3, 1, 0, 10));
        tablesContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Available tables section
        JPanel availableTablesContainer = new JPanel(new BorderLayout());
        availableTablesContainer.setBorder(BorderFactory.createTitledBorder("Available Tables"));
        JLabel availableInfoLabel = new JLabel("Select an available table to assign it to a server and guests");
        availableInfoLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
        availableTablesContainer.add(availableInfoLabel, BorderLayout.NORTH);
        
        availableTablesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        availableTablesContainer.add(new JScrollPane(availableTablesPanel), BorderLayout.CENTER);
        
        // In progress tables section
        JPanel inProgressTablesContainer = new JPanel(new BorderLayout());
        inProgressTablesContainer.setBorder(BorderFactory.createTitledBorder("In Progress Tables"));
        JLabel inProgressInfoLabel = new JLabel("Select a table to modify orders or process payment");
        inProgressInfoLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
        inProgressTablesContainer.add(inProgressInfoLabel, BorderLayout.NORTH);
        
        inProgressTablesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        inProgressTablesContainer.add(new JScrollPane(inProgressTablesPanel), BorderLayout.CENTER);
        
        // Closed tables section
        JPanel closedTablesContainer = new JPanel(new BorderLayout());
        closedTablesContainer.setBorder(BorderFactory.createTitledBorder("Recently Closed Tables"));
        JLabel closedInfoLabel = new JLabel("Recently closed tables and their details");
        closedInfoLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
        closedTablesContainer.add(closedInfoLabel, BorderLayout.NORTH);
        
        closedTablesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        closedTablesContainer.add(new JScrollPane(closedTablesPanel), BorderLayout.CENTER);
        
        tablesContainer.add(availableTablesContainer);
        tablesContainer.add(inProgressTablesContainer);
        tablesContainer.add(closedTablesContainer);
        
        // Add panels to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(tablesContainer, BorderLayout.CENTER);
        
        // Initial table population
        refreshTablesPanels();
    }
    
    private void refreshTablesPanels() {
        // Clear panels
        availableTablesPanel.removeAll();
        inProgressTablesPanel.removeAll();
        closedTablesPanel.removeAll();
        
        // Get tables from the model
        Tables tableModel = controller.getModel().getTables();
        
        // Add available tables
        ArrayList<Table> availableTables = this.controller.getAvalibleTables();
        for (Table table : availableTables) {
            int tableNumber = table.getTableId();
            
            JPanel tableCard = createTableCard(
                "Table " + tableNumber,
                "Capacity: " + table.getCapacity(),
                "Available",
                new Color(220, 255, 220), // Light green
                e -> showAssignTableDialog(tableNumber, table.getCapacity())
            );
            
            availableTablesPanel.add(tableCard);
        }
        
        // Add in-progress tables
        for (Table table : tableModel.getTables()) {
            if (table.getIsOccupied()) {
                int tableNumber = table.getTableId();
                Bill currentBill = controller.getModel().getBillTable(tableNumber);
                double total = currentBill != null ? currentBill.getTotalCost() : 0.0;
                
                JPanel tableCard = createTableCard(
                    "Table " + tableNumber,
                    "Server: " + table.getServer().getName(),
                    "Guests: " + table.getNumPeople() + " | $" + String.format("%.2f", total),
                    new Color(255, 255, 220), // Light yellow
                    e -> showTableOptionsDialog(tableNumber)
                );
                
                inProgressTablesPanel.add(tableCard);
            }
        }
        
        // Add closed tables
        for (Bill bill : controller.getModel().getClosedTables()) {
            String serverName = bill.getServer() != null ? bill.getServer().getName() : "Unknown";
            
            JPanel tableCard = createTableCard(
                "Table " + bill.getTableNum(),
                "Server: " + serverName,
                "Total: $" + String.format("%.2f", bill.getTotalCost()),
                new Color(255, 220, 220), // Light red
                null // No action for closed tables
            );
            
            closedTablesPanel.add(tableCard);
        }
        
        // Refresh the UI
        revalidate();
        repaint();
    }
    
    private JPanel createTableCard(String title, String line1, String line2, Color backgroundColor, ActionListener action) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(180, 100));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        card.setBackground(backgroundColor);
        
        JLabel titleLabel = new JLabel("<html><b>" + title + "</b></html>");
        card.add(titleLabel, BorderLayout.NORTH);
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        infoPanel.setOpaque(false);
        infoPanel.add(new JLabel(line1));
        infoPanel.add(new JLabel(line2));
        card.add(infoPanel, BorderLayout.CENTER);
        
        if (action != null) {
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    action.actionPerformed(new ActionEvent(card, ActionEvent.ACTION_PERFORMED, "click"));
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 2),
                        BorderFactory.createEmptyBorder(6, 6, 6, 6)
                    ));
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                    ));
                }
            });
        }
        
        return card;
    }
    
    private void showAssignTableDialog(int tableNumber, int capacity) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Assign Table " + tableNumber, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Server selection
        formPanel.add(new JLabel("Server:"));
        JComboBox<String> serverCombo = new JComboBox<>();
        HashMap<String, Server> servers = controller.getModel().getServers();
        for (String serverName : servers.keySet()) {
            serverCombo.addItem(serverName);
        }
        formPanel.add(serverCombo);
        
        // Number of guests
        formPanel.add(new JLabel("Number of Guests:"));
        JSpinner guestSpinner = new JSpinner(new SpinnerNumberModel(1, 1, capacity, 1));
        formPanel.add(guestSpinner);
        
        // Capacity info
        JLabel capacityLabel = new JLabel("Table Capacity: " + capacity);
        formPanel.add(capacityLabel);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton assignButton = new JButton("Assign Table");
        assignButton.addActionListener(e -> {
            String selectedServer = (String) serverCombo.getSelectedItem();
            int numGuests = (Integer) guestSpinner.getValue();
            
            if (selectedServer != null) {
                boolean success = controller.handleAssignTable(tableNumber, numGuests, selectedServer);
                
                if (success) {
                    dialog.dispose();
                    refreshTablesPanels();
                    openOrderProcessingWindow(tableNumber);
                } else {
                    JOptionPane.showMessageDialog(dialog, 
                        "Failed to assign table. The table might not have enough capacity.",
                        "Assignment Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(assignButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void showTableOptionsDialog(int tableNumber) {
        String[] options = {"Modify Order", "Process Payment"};
        int choice = JOptionPane.showOptionDialog(this,
            "What would you like to do with Table " + tableNumber + "?",
            "Table Options",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        if (choice == 0) { // Modify Order
            openOrderProcessingWindow(tableNumber);
        } else if (choice == 1) { // Process Payment
            openPaymentWindow(tableNumber);
        }
    }
    
    private void openOrderProcessingWindow(int tableNumber) {
        Table table = controller.getModel().getTables().getTable(tableNumber);
        if (table != null) {
            // Create and show the OrderProcessingWindow
            OrderProcessingWindow orderWindow = new OrderProcessingWindow(
                controller, tableNumber, this);
            orderWindow.setVisible(true);
        }
    }
    
    private void openPaymentWindow(int tableNumber) {
        Bill bill = controller.getModel().getBillTable(tableNumber);
        if (bill != null) {
            PaymentWindow paymentWindow = new PaymentWindow(this, this.controller, bill);
            
            // Add window listener to handle payment completion
            paymentWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    if (bill.getIsPaid()) {
                        // Process when payment is completed
                        controller.handleCloseTable(tableNumber, bill.getTips());
                        refreshTablesPanels();
                    }
                }
            });
            
            paymentWindow.setVisible(true);
        }
    }
    
    public void refresh() {
        refreshTablesPanels();
    }
    
    public JButton getBackButton() {
        return backButton;
    }
}

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/* screen to seat / close tables and add items + mods */
public class OrderManagementPanel extends JPanel
        implements RestaurantModel.ModelListener { // hook into model events

    private final RestaurantController controller;

    /* selectors & fields */
    private final JComboBox<Integer> tableBox  = new JComboBox<>();
    private final JTextField         guestsTxt = new JTextField(4);
    private final JComboBox<String>  serverBox = new JComboBox<>();
    private final JTextField         tipTxt    = new JTextField(4);

    private final JComboBox<String>  menuBox   = new JComboBox<>();
    private final JComboBox<String>  modBox    = new JComboBox<>();

    private final DefaultTableModel  orderTm =
            new DefaultTableModel(new String[]{"Item","Cost"},0);

    public OrderManagementPanel(RestaurantController c) {
        controller = c;
        buildUI();
        refreshTableList();
        refreshServers();
        refreshMods();          // populate with first item’s mods
        refreshOrder();

        /* register for live updates (e.g. new servers) */
        controller.getModel().addListener(this);
    }

    /* ------------ model listener callback ------------ */
    @Override public void modelChanged() {
        refreshServers();                       // keep server combo in sync
    }

    /*layout*/
    private void buildUI() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        /* top bar */
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        top.add(new JLabel("Table:"));
        tableBox.setRenderer(new TableComboRenderer());
        tableBox.addActionListener(e -> refreshOrder());
        top.add(tableBox);

        top.add(new JLabel("Guests:"));
        top.add(guestsTxt);

        top.add(new JLabel("Server:"));
        top.add(serverBox);

        JButton seatBtn = new JButton("Seat Table");
        seatBtn.addActionListener(e -> seatTable());
        top.add(seatBtn);

        top.add(new JLabel("Tip:"));
        top.add(tipTxt);

        JButton closeBtn = new JButton("Close Table");
        closeBtn.addActionListener(e -> closeTable());
        top.add(closeBtn);
        add(top, BorderLayout.NORTH);

        /* order list */
        add(new JScrollPane(new JTable(orderTm)), BorderLayout.CENTER);

        /* bottom add bar */
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        bot.add(new JLabel("Menu Item:"));
        menuBox.addActionListener(e -> refreshMods());
        bot.add(menuBox);

        bot.add(new JLabel("Mod:"));
        bot.add(modBox);

        JButton addBtn = new JButton("Add to Order");
        addBtn.addActionListener(e -> addItem());
        bot.add(addBtn);
        add(bot, BorderLayout.SOUTH);
    }

    /*refresh helpers*/
    private void refreshTableList() {
        tableBox.removeAllItems();
        controller.getModel().getTables().getTablesInfo()
                .forEach(t -> tableBox.addItem(t.getId()));
        if(tableBox.getItemCount()>0) tableBox.setSelectedIndex(0);

        if(menuBox.getItemCount()==0)
            controller.getModel().getMenu().getAllItems()
                    .forEach(i -> menuBox.addItem(i.getName()));
    }

    /* called from modelChanged + constructor */
    void refreshServers() {
        String prev = (String) serverBox.getSelectedItem();
        serverBox.removeAllItems();
        controller.getModel().getServers().values()
                .forEach(s -> serverBox.addItem(s.getName()));
        if(prev != null) serverBox.setSelectedItem(prev);
        if(serverBox.getItemCount()>0 && serverBox.getSelectedIndex()<0)
            serverBox.setSelectedIndex(0);
    }

    /* rebuild mod selector based on chosen base item */
    private void refreshMods() {
        modBox.removeAllItems();
        String sel = (String) menuBox.getSelectedItem();
        Item base = controller.getModel().getMenu().getAllItems().stream()
                .filter(i -> i.getName().equals(sel)).findFirst().orElse(null);
        if(base==null) return;
        modBox.addItem("None");                       // default
        for(Modification m: base.getModifications())
            modBox.addItem(m.getDescription());
    }

    /* rebuild ticket table for currently selected table */
    private void refreshOrder() {
        orderTm.setRowCount(0);
        Integer id = (Integer) tableBox.getSelectedItem();
        if(id==null) return;
        Table t = controller.getModel().getTables().getTable(id);
        if(t==null||!t.isOccupied()) return;
        for(Item i: t.getItems())
            orderTm.addRow(new Object[]{i.getName(),String.format("$%.2f",i.getTotalCost())});
    }

    /*actions*/
    private void seatTable() {
        try {
            int guests = Integer.parseInt(guestsTxt.getText().trim());
            int id     = (Integer) tableBox.getSelectedItem();
            String srv = (String) serverBox.getSelectedItem();
            if(srv==null) { warn("select a server"); return; }
            if(controller.handleAssignTable(id,guests,srv)){
                info("table "+id+" seated");
                refreshOrder(); tableBox.repaint();
            }
        } catch(NumberFormatException ex){ error("guests must be a number"); }
    }

    private void closeTable() {
        try{
            double tip = Double.parseDouble(tipTxt.getText().trim());
            int id = (Integer) tableBox.getSelectedItem();
            controller.handleCloseTable(id,tip);
            info("table "+id+" closed");
            refreshOrder(); tableBox.repaint();
        } catch(NumberFormatException ex){ error("tip must be a number"); }
    }

    private void addItem() {
        int id = (Integer) tableBox.getSelectedItem();
        Table t = controller.getModel().getTables().getTable(id);
        if(t==null||!t.isOccupied()){ warn("seat table first"); return; }

        String baseName = (String) menuBox.getSelectedItem();
        Item base = controller.getModel().getMenu().getAllItems().stream()
                .filter(i -> i.getName().equals(baseName)).findFirst().orElse(null);
        if(base==null) return;

        Item fresh = new Item(base);                // clone to attach chosen mod
        String modDesc = (String) modBox.getSelectedItem();
        if(modDesc!=null && !modDesc.equals("None")){
            base.getModifications().stream()
                    .filter(m -> m.getDescription().equals(modDesc))
                    .findFirst().ifPresent(fresh::addModification);
        }
        controller.handleAddOrder(id,new ArrayList<>(List.of(fresh)));
        refreshOrder();
    }

    /*utilities*/
    private void warn (String m){ JOptionPane.showMessageDialog(this,m,"Warning",JOptionPane.WARNING_MESSAGE);}
    private void info (String m){ JOptionPane.showMessageDialog(this,m,"Info",   JOptionPane.INFORMATION_MESSAGE);}
    private void error(String m){ JOptionPane.showMessageDialog(this,m,"Error",  JOptionPane.ERROR_MESSAGE);}

    /* colour tables green/ red in combo */
    private class TableComboRenderer extends DefaultListCellRenderer{
        @Override public Component getListCellRendererComponent(JList<?> list,
                                                                Object value,int idx,boolean sel,boolean foc){
            JLabel l=(JLabel)super.getListCellRendererComponent(list,value,idx,sel,foc);
            if(value instanceof Integer id){
                Table t=controller.getModel().getTables().getTable(id);
                l.setForeground((t!=null&&t.isOccupied())?new Color(170,30,30):new Color(0,128,0));
            }
            return l;
        }
    }
}
