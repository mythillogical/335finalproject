package view;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrderManagementPanel extends JPanel {
    // reference to controller for mvc
    private final RestaurantController controller;

    // widgets for table seating and orders
    private final JComboBox<Integer> tableBox  = new JComboBox<>();
    private final JTextField         guestsTxt = new JTextField(4);
    private final JComboBox<String>  serverBox = new JComboBox<>();
    private final JTextField         tipTxt    = new JTextField(4);

    private final JComboBox<String>  menuBox   = new JComboBox<>();
    private final DefaultTableModel  orderTm   =
            new DefaultTableModel(new String[]{"item", "cost"}, 0);

    public OrderManagementPanel(RestaurantController controller) {
        this.controller = controller;
        buildUI();
        refreshTableList();
        refreshServers();
        refreshOrder();
    }

    // set up layout and add components
    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.add(new JLabel("table:"));
        tableBox.setRenderer(new TableComboRenderer());
        tableBox.addActionListener(e -> refreshOrder());
        top.add(tableBox);

        top.add(new JLabel("guests:"));
        top.add(guestsTxt);

        top.add(new JLabel("server:"));
        top.add(serverBox);

        JButton seatBtn = new JButton("seat table");
        seatBtn.addActionListener(e -> seatTable());
        top.add(seatBtn);

        top.add(new JLabel("tip:"));
        top.add(tipTxt);

        JButton closeBtn = new JButton("close table");
        closeBtn.addActionListener(e -> closeTable());
        top.add(closeBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(new JTable(orderTm)), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bottom.add(new JLabel("menu item:"));
        bottom.add(menuBox);
        JButton addBtn = new JButton("add to order");
        addBtn.addActionListener(e -> addItem());
        bottom.add(addBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    // load table ids and menu items into combos
    private void refreshTableList() {
        tableBox.removeAllItems();
        controller.getModel().getTables().getTablesInfo()
                .forEach(ti -> tableBox.addItem(ti.getId()));
        if (tableBox.getItemCount() > 0) tableBox.setSelectedIndex(0);

        if (menuBox.getItemCount() == 0) {
            controller.getModel().getMenu().getAllItems()
                    .forEach(i -> menuBox.addItem(i.getName()));
        }
    }

    // reload server names
    void refreshServers() {
        serverBox.removeAllItems();
        controller.getModel().getServers().values()
                .forEach(s -> serverBox.addItem(s.getName()));
        if (serverBox.getItemCount() > 0) serverBox.setSelectedIndex(0);
    }

    // show current orders for selected table
    private void refreshOrder() {
        orderTm.setRowCount(0);
        Integer sel = (Integer) tableBox.getSelectedItem();
        if (sel == null) return;

        Table t = controller.getModel().getTables().getTable(sel);
        if (t == null || !t.isOccupied()) return;

        for (Item i : t.getItems()) {
            orderTm.addRow(new Object[]{
                    i.getName(),
                    String.format("$%.2f", i.getTotalCost())
            });
        }
    }

    // attempt to seat table with given guests and server
    private void seatTable() {
        String guestsStr = guestsTxt.getText().trim();
        String server    = (String) serverBox.getSelectedItem();
        if (guestsStr.isEmpty() || server == null) {
            warn("missing data");
            return;
        }
        try {
            int guests  = Integer.parseInt(guestsStr);
            int tableId = (Integer) tableBox.getSelectedItem();
            if (controller.handleAssignTable(tableId, guests, server)) {
                info("table " + tableId + " seated");
                refreshOrder();
                tableBox.repaint();
            }
        } catch (NumberFormatException ex) {
            error("guests must be a number");
        }
    }

    // close the table and record tip
    private void closeTable() {
        try {
            double tip  = Double.parseDouble(tipTxt.getText().trim());
            int tableId = (Integer) tableBox.getSelectedItem();
            controller.handleCloseTable(tableId, tip);
            info("table " + tableId + " closed");
            refreshOrder();
            tableBox.repaint();
        } catch (NumberFormatException ex) {
            error("tip must be a number");
        }
    }

    // add selected menu item to current table
    private void addItem() {
        int   tableId = (Integer) tableBox.getSelectedItem();
        Table t       = controller.getModel().getTables().getTable(tableId);
        if (t == null || !t.isOccupied()) {
            warn("seat table first");
            return;
        }
        String name = (String) menuBox.getSelectedItem();
        Item   it   = controller.getModel().getMenu()
                .getAllItems().stream()
                .filter(i -> i.getName().equals(name))
                .findFirst().orElse(null);
        if (it == null) return;
        controller.handleAddOrder(tableId, new ArrayList<>(List.of(it)));
        refreshOrder();
    }

    // simple message dialogs
    private void warn (String m){ JOptionPane.showMessageDialog(this, m, "warning", JOptionPane.WARNING_MESSAGE); }
    private void info (String m){ JOptionPane.showMessageDialog(this, m, "info",    JOptionPane.INFORMATION_MESSAGE); }
    private void error(String m){ JOptionPane.showMessageDialog(this, m, "error",   JOptionPane.ERROR_MESSAGE); }

    // renderer to colour table ids by occupancy
    private class TableComboRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list,
                                                      Object value, int idx, boolean sel, boolean focus) {
            JLabel l = (JLabel) super.getListCellRendererComponent(list, value, idx, sel, focus);
            if (value instanceof Integer id) {
                Table t = controller.getModel().getTables().getTable(id);
                if (t != null && t.isOccupied()) {
                    l.setForeground(new Color(170, 30, 30));
                } else {
                    l.setForeground(new Color(0, 128, 0));
                }
            }
            return l;
        }
    }
}
