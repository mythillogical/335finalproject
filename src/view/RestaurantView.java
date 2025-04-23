package view;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Swing-based view helper; every public method builds a component and
 * swaps it into a single centre panel owned by the host frame.
 */
public class RestaurantView {

    private final JFrame  frame;
    private final JPanel  swapPanel;

    public RestaurantView(JFrame frame) {
        this.frame     = frame;
        this.swapPanel = new JPanel(new BorderLayout());
        frame.getContentPane().add(swapPanel, BorderLayout.CENTER);
    }

    /* public access so RestaurantGUI can re-attach the panel when needed */
    public JPanel getRootPanel() { return swapPanel; }

    /* ------------------------------------------------------------------
       TABLE OVERVIEW
       ------------------------------------------------------------------ */

    public void displayTables(List<TableInfo> tableInfos) {
        String[] cols = {"Table", "Capacity", "Seated", "Occupied"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        for (TableInfo ti : tableInfos) {
            tm.addRow(new Object[]{
                    ti.getId(),
                    ti.getCapacity(),
                    ti.getSeated(),
                    ti.getSeated() == 0 ? "No" : "Yes"});
        }
        refreshWith(new JScrollPane(new JTable(tm)), "Current Tables");
    }

    /* ------------------------------------------------------------------
       MENU LISTING
       ------------------------------------------------------------------ */

    public void displayMenu(List<Item> menu) {
        String[] cols = {"Name", "Category", "Base", "Total (mods)"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        for (Item i : menu) {
            tm.addRow(new Object[]{
                    i.getName(),
                    i.getCategory(),
                    String.format("$%.2f", i.getCost()),
                    String.format("$%.2f", i.getTotalCost())});
        }
        refreshWith(new JScrollPane(new JTable(tm)), "Menu");
    }

    /* ------------------------------------------------------------------
       BILL RENDERING
       ------------------------------------------------------------------ */

    public void displayBill(int tableNumber, Bill bill) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bill for table ").append(tableNumber).append("\n\n")
                .append("Total: $").append(String.format("%.2f", bill.getTotalCost())).append("\n")
                .append("Split evenly: $").append(String.format("%.2f", bill.getCostSplitEvenly())).append("\n\n");
        if (bill.getServer() != null)
            sb.append("Server: ").append(bill.getServer().getName()).append("\n");

        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 14));
        refreshWith(new JScrollPane(ta), "Bill");
    }

    /* ------------------------------------------------------------------
       ERROR POP-UP
       ------------------------------------------------------------------ */

    public void displayError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /* ------------------------------------------------------------------
       PRIVATE HELPERS
       ------------------------------------------------------------------ */

    private void refreshWith(JComponent c, String title) {
        swapPanel.removeAll();
        swapPanel.add(c, BorderLayout.CENTER);
        swapPanel.setBorder(BorderFactory.createTitledBorder(title));
        frame.revalidate();
        frame.repaint();
    }
}
