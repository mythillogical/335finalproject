package view;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

/** Shows quantities, turnover, tips and offers a CSV export button. */
public class SalesReportPanel extends JPanel {

    private final RestaurantController controller;
    private final DefaultTableModel tm
            = new DefaultTableModel(new String[]{"Item","Qty"}, 0);

    private final JLabel lblRevenue = new JLabel("$0.00");
    private final JLabel lblTips    = new JLabel("$0.00");

    public SalesReportPanel(RestaurantController c) {
        controller = c;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(new JScrollPane(new JTable(tm)), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT, 15,0));
        south.add(new JLabel("Revenue:"));
        south.add(lblRevenue);
        south.add(new JLabel("Tips:"));
        south.add(lblTips);

        JButton export = new JButton("Export CSV");
        export.addActionListener(e -> exportCsv());
        south.add(export);

        add(south, BorderLayout.SOUTH);
    }

    /* ---------------- public ----------------- */
    public void refresh() {
        Map<String,Integer> tally = new TreeMap<>();
        double revenue = 0, tips = 0;

        for (Bill b : controller.getModel().getClosedTables()) {
            b.getItems().forEach(i -> tally.merge(i.getName(), 1, Integer::sum));
            revenue += Item.getItemsCost(new java.util.ArrayList<>(b.getItems()));
            tips    += b.getTip();
        }

        tm.setRowCount(0);
        tally.forEach((n,q) -> tm.addRow(new Object[]{n,q}));
        if (tally.isEmpty()) tm.addRow(new Object[]{"<no closed bills yet>", 0});

        lblRevenue.setText(String.format("$%.2f", revenue));
        lblTips.setText(String.format("$%.2f", tips));
    }

    /* ---------------- helpers ---------------- */
    private void exportCsv() {
        try {
            Path tmp = Files.createTempFile("sales-report-", ".csv");
            try (FileWriter w = new FileWriter(tmp.toFile())) {
                w.write("Item,Qty\n");
                for (int r=0;r<tm.getRowCount();r++)
                    w.write(tm.getValueAt(r,0)+","+tm.getValueAt(r,1)+"\n");
                w.write("\nRevenue,"+lblRevenue.getText().substring(1)+"\n");
                w.write("Tips,"    +lblTips.getText().substring(1)+"\n");
            }
            Desktop.getDesktop().open(tmp.toFile());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not write CSV\n"+ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
