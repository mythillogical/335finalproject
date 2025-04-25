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
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Dashboard showing Available / In-Progress / Recently-Closed tables. */
public class OrderManagementPanel extends JPanel
        implements RestaurantModel.ModelListener {

    /* mvc */
    private final RestaurantController ctrl;

    /* strips */
    private final JPanel panAvail  = new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));
    private final JPanel panBusy   = new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));
    private final JPanel panClosed = new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));

    public OrderManagementPanel(RestaurantController c){
        ctrl = c;

        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(10,10,10,10));

        JPanel rows = new JPanel(new GridLayout(3,1,0,10));
        rows.add(wrap("Available Tables", panAvail));
        rows.add(wrap("In Progress",      panBusy ));
        rows.add(wrap("Recently Closed",  panClosed));
        add(rows,BorderLayout.CENTER);

        refreshTablesPanels();
        ctrl.getModel().addListener(this);
    }

    @Override public void modelChanged(){ refreshTablesPanels(); }

    /* ---------- strip rebuild ---------- */
    private void refreshTablesPanels(){
        panAvail.removeAll();
        panBusy .removeAll();
        panClosed.removeAll();

        Tables tbls = ctrl.getModel().getTables();

        for(TableInfo ti: tbls.getAvailable(1)){
            Table t = tbls.getTable(ti.getId());
            if(t != null && !t.isOccupied()) panAvail.add(cardForAvailable(t));
        }
        for(Table t : tbls.getOccupiedTables()) panBusy.add(cardForBusy(t));
        for(Bill b : ctrl.getModel().getClosedTables()) panClosed.add(cardForClosed(b));

        revalidate(); repaint();
    }

    /* ---------- cards ---------- */
    private JPanel cardForAvailable(Table t){
        JPanel cd = makeCard(new Color(220,255,220));
        cd.add(label(t), BorderLayout.CENTER);
        cd.addMouseListener(new MouseAdapter(){
            @Override public void mouseClicked(MouseEvent e){ seatDialog(t); }
        });
        return cd;
    }

    private JPanel cardForBusy(Table t){
        JPanel cd = makeCard(new Color(255,255,220));
        cd.add(label(t), BorderLayout.NORTH);
        cd.add(new JLabel("Server: "+t.getServer().getName()), BorderLayout.CENTER);
        cd.add(new JLabel("Guests: "+t.getNumSeated()),         BorderLayout.SOUTH);
        cd.addMouseListener(new MouseAdapter(){
            @Override public void mouseClicked(MouseEvent e){ chooseBusyAction(t); }
        });
        return cd;
    }

    private JPanel cardForClosed(Bill b){
        JPanel cd = makeCard(new Color(255,220,220));
        cd.add(new JLabel("<html><b>Table "+ b.getServer()+"</b></html>"),
                BorderLayout.NORTH);
        cd.add(new JLabel("Total $"+String.format("%.2f", b.getTotalCost())),
                BorderLayout.CENTER);
        return cd;
    }

    /* ---------- busy-table dialog ---------- */
    private void chooseBusyAction(Table t){
        boolean empty = t.getItems().isEmpty();
        String first  = empty ? "Create order" : "Modify order";
        String[] ops  = { first, "Process payment" };

        int ch = JOptionPane.showOptionDialog(this,
                "Table "+t.getTableID()+": choose action",
                "Table Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, ops, ops[0]);

        if(ch == 0) openOrderWindow(t);
        else if(ch == 1) openPaymentWindow(t);
    }

    private void openOrderWindow(Table t){
        new OrderProcessingWindow(ctrl, t.getTableID(), this).setVisible(true);
    }
    private void openPaymentWindow(Table t){
        Bill b = ctrl.getModel().getTables().getBillTable(t.getTableID());
        if(b != null) new PaymentWindow(ctrl, b, t.getTableID(), this).setVisible(true);
    }

    /* ---------- seat-party dialog ---------- */
    private void seatDialog(Table t){

        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Seat table "+t.getTableID(), Dialog.ModalityType.APPLICATION_MODAL);

        dlg.setLayout(new BorderLayout(10,10));
        dlg.setSize(320,190);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(3,2,8,8));
        form.setBorder(new EmptyBorder(10,10,10,10));

        form.add(new JLabel("Server:"));
        JComboBox<String> srvBox = new JComboBox<>();
        ctrl.getModel().getServers().values()
                .forEach(s -> srvBox.addItem(s.getName()));
        form.add(srvBox);

        form.add(new JLabel("Guests:"));
        JSpinner spn = new JSpinner(
                new SpinnerNumberModel(1,1,t.getCapacity(),1));
        form.add(spn);

        form.add(new JLabel("Capacity:"));
        form.add(new JLabel(""+t.getCapacity()));

        dlg.add(form, BorderLayout.CENTER);

        JButton ok = new JButton("Seat");
        ok.addActionListener(e -> {
            String srv   = (String) srvBox.getSelectedItem();
            int guests   = (Integer) spn.getValue();
            if(ctrl.handleAssignTable(t.getTableID(), guests, srv))
                dlg.dispose();
        });
        dlg.add(ok, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    /* ---------- helpers ---------- */
    private static JPanel makeCard(Color bg){
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(170,80));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(6,6,6,6)));
        p.setBackground(bg);
        return p;
    }
    private static JLabel label(Table t){
        return new JLabel("<html><b>Table "+t.getTableID()+"</b></html>",
                SwingConstants.CENTER);
    }
    private static JPanel wrap(String title, JPanel body){
        JPanel w = new JPanel(new BorderLayout());
        w.setBorder(BorderFactory.createTitledBorder(title));
        w.add(new JScrollPane(body));
        return w;
    }

    /* external refresh from children */
    public void refresh(){ refreshTablesPanels(); }
}
