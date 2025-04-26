package view;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/*
 * View component for displaying restaurant information.
 * 
 * Handles the display of tables, menu items, bills, and error messages.
 * 
 * Acts as the bridge between the model's state and the user interface panels.
 * 
 * Author: Michael B, Michael D, Asif R, Mohammed A
 * Course: CSc 335 - Object-Oriented Programming and Design
 * Semester: Spring 2025
 */
public class RestaurantView {

    private final JFrame frame;
    private final JPanel swapPanel;

    public RestaurantView(JFrame frame) {
        this.frame     = frame;
        this.swapPanel = new JPanel(new BorderLayout());
        frame.getContentPane().add(swapPanel, BorderLayout.CENTER);
    }

    /* ---------- overview panel ---------- */
    public JPanel getRootPanel() { return swapPanel; }

    public void displayTables(List<TableInfo> tableInfos) {
        String[] cols = {"Table", "Capacity", "Seated", "Occupied"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        for (TableInfo ti : tableInfos) {
            tm.addRow(new Object[]{
                    ti.getId(),
                    ti.getCapacity(),
                    ti.getSeated(),
                    ti.getSeated() == 0 ? "No" : "Yes"
            });
        }
        refreshWith(new JScrollPane(new JTable(tm)), "current tables");
    }

    public void displayMenu(List<Item> menu) {
        String[] cols = {"Name", "Category", "Base", "Total (mods)"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        for (Item i : menu) {
            tm.addRow(new Object[]{
                    i.getName(),
                    i.getCategory(),
                    String.format("$%.2f", i.getCost()),
                    String.format("$%.2f", i.getTotalCost())
            });
        }
        refreshWith(new JScrollPane(new JTable(tm)), "menu");
    }

    public void displayBill(int tableNumber, Bill bill) {
        StringBuilder sb = new StringBuilder();
        sb.append("bill for table ").append(tableNumber).append("\n\n")
                .append("total: $").append(String.format("%.2f", bill.getTotalCost())).append("\n")
                .append("split evenly: $").append(String.format("%.2f", bill.getCostSplitEvenly())).append("\n\n");

        String srv = bill.getServer();
        if (srv != null && !srv.isBlank()) sb.append("server: ").append(srv).append("\n");

        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 14));
        refreshWith(new JScrollPane(ta), "bill");
    }

    public void displayError(String message) {
        JOptionPane.showMessageDialog(frame, message, "error", JOptionPane.ERROR_MESSAGE);
    }

    private void refreshWith(JComponent c, String title) {
        swapPanel.removeAll();
        swapPanel.add(c, BorderLayout.CENTER);
        swapPanel.setBorder(BorderFactory.createTitledBorder(title));
        frame.revalidate();
        frame.repaint();
    }
}
