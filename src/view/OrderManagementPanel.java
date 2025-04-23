package view;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Order-entry screen: seat / close tables and add menu items.  Provides
 * feedback pop-ups and a colour-coded table selector (green = free,
 * red = occupied).
 */
public class OrderManagementPanel extends JPanel {

    /* MVC hook */
    private final RestaurantController controller;

    /* ─────────── widgets ─────────── */
    private final JComboBox<Integer> tableBox  = new JComboBox<>();
    private final JTextField         guestsTxt = new JTextField(4);
    private final JComboBox<String>  serverBox = new JComboBox<>();
    private final JTextField         tipTxt    = new JTextField(4);

    private final JComboBox<String>  menuBox   = new JComboBox<>();
    private final DefaultTableModel  orderTm   =
            new DefaultTableModel(new String[]{"Item", "Cost"}, 0);

    /* ================================================================= */
    public OrderManagementPanel(RestaurantController controller) {
        this.controller = controller;
        buildUI();
        refreshTableList();
        refreshServers();
        refreshOrder();
    }

    /* =================================================================
       UI BUILD
       ================================================================= */
    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /* ─── top control bar ─────────────────────────────────────── */
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

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

        /* ─── current order list ─────────────────────────────────── */
        add(new JScrollPane(new JTable(orderTm)), BorderLayout.CENTER);

        /* ─── bottom add-item bar ────────────────────────────────── */
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bottom.add(new JLabel("Menu item:"));
        bottom.add(menuBox);
        JButton addBtn = new JButton("Add to Order");
        addBtn.addActionListener(e -> addItem());
        bottom.add(addBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    /* =================================================================
       REFRESH HELPERS
       ================================================================= */

    /** fills table selector and menu list; called once on construction */
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

    /** reload servers combo – called from RestaurantGUI when servers change */
    void refreshServers() {
        serverBox.removeAllItems();
        controller.getModel().getServers().values()
                .forEach(s -> serverBox.addItem(s.getName()));
        if (serverBox.getItemCount() > 0) serverBox.setSelectedIndex(0);
    }

    /** rebuilds the visible order table for the currently selected table */
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

    /* =================================================================
       ACTIONS
       ================================================================= */
    private void seatTable() {
        String guestsStr = guestsTxt.getText().trim();
        String server    = (String) serverBox.getSelectedItem();

        if (guestsStr.isEmpty() || server == null) {
            warn("Missing data");
            return;
        }

        try {
            int guests  = Integer.parseInt(guestsStr);
            int tableId = (Integer) tableBox.getSelectedItem();

            if (controller.handleAssignTable(tableId, guests, server)) {
                info("Table "+tableId+" successfully seated");
                refreshOrder();
                tableBox.repaint();          // update colours
            }
        } catch (NumberFormatException ex) {
            error("Guests must be a whole number");
        }
    }

    private void closeTable() {
        try {
            double tip  = Double.parseDouble(tipTxt.getText().trim());
            int tableId = (Integer) tableBox.getSelectedItem();

            controller.handleCloseTable(tableId, tip);
            info("Table "+tableId+" closed");
            refreshOrder();
            tableBox.repaint();              // update colours
        } catch (NumberFormatException ex) {
            error("Tip must be a number");
        }
    }

    private void addItem() {
        int    tableId  = (Integer) tableBox.getSelectedItem();
        Table  t        = controller.getModel().getTables().getTable(tableId);

        if (t == null || !t.isOccupied()) {
            warn("Seat the table before adding an order");
            return;
        }

        String itemName = (String) menuBox.getSelectedItem();
        Item chosen = controller.getModel().getMenu().getAllItems().stream()
                .filter(i -> i.getName().equals(itemName))
                .findFirst().orElse(null);

        if (chosen == null) return;

        controller.handleAddOrder(tableId, new ArrayList<>(List.of(chosen)));
        refreshOrder();
    }

    /* =================================================================
       FEEDBACK UTILITIES
       ================================================================= */
    private void warn (String msg){ JOptionPane.showMessageDialog(this,msg,"Warning",JOptionPane.WARNING_MESSAGE); }
    private void info (String msg){ JOptionPane.showMessageDialog(this,msg,"Info",   JOptionPane.INFORMATION_MESSAGE); }
    private void error(String msg){ JOptionPane.showMessageDialog(this,msg,"Error",  JOptionPane.ERROR_MESSAGE); }

    /* =================================================================
       COLOURED COMBO-BOX RENDERER
       ================================================================= */
    private class TableComboRenderer extends DefaultListCellRenderer {
        @Override public Component getListCellRendererComponent(JList<?> list,
                                                                Object value,int idx,boolean sel,boolean focus) {
            JLabel l = (JLabel) super.getListCellRendererComponent(
                    list,value,idx,sel,focus);

            if (value instanceof Integer id) {
                Table t = controller.getModel().getTables().getTable(id);
                if (t != null && t.isOccupied()) {
                    l.setForeground(new Color(170, 30, 30));   // red
                } else {
                    l.setForeground(new Color(0, 128, 0));     // green
                }
            }
            return l;
        }
    }
}
