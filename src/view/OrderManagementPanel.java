package view;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Minimal order‑entry screen: choose a table, pick an item from the
 * menu, add to that table's running order.
 */
public class OrderManagementPanel extends JPanel {

    private final RestaurantController controller;

    private final JComboBox<Integer> tableBox = new JComboBox<>();
    private final JComboBox<String>  itemBox  = new JComboBox<>();
    private final DefaultTableModel  current  = new DefaultTableModel(new String[]{"Item","Cost"}, 0);

    public OrderManagementPanel(RestaurantController controller) {
        this.controller = controller;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        /* top selector */
        JPanel top = new JPanel(new FlowLayout());
        top.add(new JLabel("Table:"));
        controller.getModel().getTables().getTablesInfo().forEach(ti -> tableBox.addItem(ti.getId()));
        top.add(tableBox);
        top.add(new JLabel("Menu item:"));
        controller.getModel().getMenu().getAllItems().forEach(it -> itemBox.addItem(it.getName()));
        top.add(itemBox);
        JButton add = new JButton("Add to Order");
        add.addActionListener(e -> addItem());
        top.add(add);
        add(top, BorderLayout.NORTH);

        /* current order list */
        add(new JScrollPane(new JTable(current)), BorderLayout.CENTER);
    }

    private void addItem() {
        int tableId = (int) tableBox.getSelectedItem();
        String itemName = (String) itemBox.getSelectedItem();

        Item chosen = controller.getModel().getMenu().getAllItems().stream()
                .filter(i -> i.getName().equals(itemName)).findFirst().orElse(null);
        if (chosen == null) return;

        ArrayList<Item> one = new ArrayList<>();
        one.add(chosen);
        controller.handleAddOrder(tableId, one);

        current.addRow(new Object[]{chosen.getName(), String.format("$%.2f", chosen.getTotalCost())});
    }
}
