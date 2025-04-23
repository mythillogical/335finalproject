package view;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Displays a very simple sales summary: Item → quantity sold.
 * Call {@link #refresh()} before showing the panel so the table
 * reflects the most recent closed bills.
 */
public class SalesReportPanel extends JPanel {

    private final RestaurantController controller;
    private final DefaultTableModel    tm =
            new DefaultTableModel(new String[]{"Item", "Qty"}, 0);

    public SalesReportPanel(RestaurantController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        add(new JScrollPane(new JTable(tm)), BorderLayout.CENTER);
    }

    /** Re-computes quantities from all closed bills in the model. */
    public void refresh() {
        tm.setRowCount(0);
        Map<String,Integer> tally = new HashMap<>();

        for (Bill bill : controller.getModel().getClosedTables()) {
            for (Item item : bill.getItems()) {
                tally.merge(item.getName(), 1, Integer::sum);
            }
        }

        if (tally.isEmpty()) {
            tm.addRow(new Object[]{"<no closed bills yet>", 0});
        } else {
            tally.forEach((name, qty) -> tm.addRow(new Object[]{name, qty}));
        }
    }
}
