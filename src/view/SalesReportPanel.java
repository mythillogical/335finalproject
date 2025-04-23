package view;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Desktop;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

/** panel for showing item counts, revenue, tips and csv export */
public class SalesReportPanel extends JPanel {

    private final RestaurantController controller;
    private final DefaultTableModel tm = new DefaultTableModel(new String[]{"item","qty"}, 0);

    private final JLabel lblRevenue = new JLabel("$0.00");
    private final JLabel lblTips    = new JLabel("$0.00");

    public SalesReportPanel(RestaurantController c) {
        controller = c;
        buildUI();
    }

    // build ui components
    private void buildUI() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(new JScrollPane(new JTable(tm)), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT,15,0));
        south.add(new JLabel("revenue:"));
        south.add(lblRevenue);
        south.add(new JLabel("tips:"));
        south.add(lblTips);

        JButton export = new JButton("export csv");
        export.addActionListener(e -> exportCsv());
        south.add(export);

        add(south, BorderLayout.SOUTH);
    }

    // refresh table data and labels
    public void refresh() {
        Map<String,Integer> tally = new TreeMap<>();
        double revenue = 0, tips = 0;

        for (Bill b : controller.getModel().getClosedTables()) {
            for (Item i : b.getItems()) {
                tally.merge(i.getName(), 1, Integer::sum);
            }
            revenue += Item.getItemsCost(new java.util.ArrayList<>(b.getItems()));
            tips    += b.getTip();
        }

        tm.setRowCount(0);
        if (tally.isEmpty()) {
            tm.addRow(new Object[]{"<no closed bills>",0});
        } else {
            tally.forEach((n,q) -> tm.addRow(new Object[]{n,q}));
        }

        lblRevenue.setText(String.format("$%.2f", revenue));
        lblTips.setText(String.format("$%.2f", tips));
    }

    // export report to csv and open file
    private void exportCsv() {
        try {
            Path tmp = Files.createTempFile("sales-report-", ".csv");
            try (FileWriter w = new FileWriter(tmp.toFile())) {
                w.write("item,qty\n");
                for (int r = 0; r < tm.getRowCount(); r++) {
                    w.write(tm.getValueAt(r,0) + "," + tm.getValueAt(r,1) + "\n");
                }
                w.write("\nrevenue," + lblRevenue.getText().substring(1) + "\n");
                w.write("tips,"    + lblTips.getText().substring(1) + "\n");
            }
            Desktop.getDesktop().open(tmp.toFile());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "could not write csv\n"+ex.getMessage(),
                    "error", JOptionPane.ERROR_MESSAGE);
        }
    }
}