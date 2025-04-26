package view;

import controller.RestaurantController;
import model.*;

import javax.swing.*;
import java.awt.*;

/**
 * Sub-window for processing payment and closing a table.
 * 
 * Handles tipping and final bill calculation for a table.
 * 
 * Tied to the Order Management and Table Management workflows.
 * 
 * Author: Michael B, Michael D, Asif R, Mohammed A
 * Course: CSc 335 - Object-Oriented Programming and Design
 * Semester: Spring 2025
 */
public class PaymentWindow extends JDialog {

    private final RestaurantController ctrl;
    private final Bill bill; // immutable snapshot – used for display only
    private final int  tableId;
    private final OrderManagementPanel parent;

    /* ui bits we need to recalc totals */
    private final JTextField tipFld = new JTextField("0.00", 6);
    private final JLabel lblSub  = new JLabel("$0.00");
    private final JLabel lblTax  = new JLabel("$0.00");
    private final JLabel lblPerc = new JLabel("0%");
    private final JLabel lblTot  = new JLabel("$0.00");

    private static final double TAX_RATE = 0.08;

    public PaymentWindow(RestaurantController ctrl,
                         Bill bill,
                         int tableId,
                         OrderManagementPanel parent) {

        super((Frame) SwingUtilities.getWindowAncestor(parent),
                "payment – table " + tableId, true);

        this.ctrl   = ctrl;
        this.bill   = bill;
        this.tableId = tableId;
        this.parent  = parent;

        buildUi();
        recalc();
        setSize(360, 390);
        setLocationRelativeTo(parent);
    }

    /* UI */
    private void buildUi() {
        setLayout(new BorderLayout(10, 10));

        /* bill fields */
        JPanel form = new JPanel(new GridLayout(7, 2, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("bill details"));

        form.add(new JLabel("subtotal:"));
        form.add(lblSub);

        form.add(new JLabel("tax (8%):"));
        form.add(lblTax);

        form.add(new JLabel("total:"));
        form.add(lblTot);

        form.add(new JLabel("tip amount:"));
        form.add(tipFld);

        form.add(new JLabel("tip %:"));
        form.add(lblPerc);

        add(form, BorderLayout.CENTER);

        /* buttons */
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnPay    = new JButton("process payment");
        JButton btnCancel = new JButton("cancel");
        south.add(btnCancel);
        south.add(btnPay);
        add(south, BorderLayout.SOUTH);

        /* listeners */
        tipFld   .addActionListener(e -> recalc());
        btnCancel.addActionListener(e -> dispose());
        btnPay   .addActionListener(e -> doPay());
    }

    /* helpers */
    private void recalc() {
        try {
            double sub = bill.getTotalCost();
            double tax = sub * TAX_RATE;
            double tip = Double.parseDouble(tipFld.getText());

            lblSub .setText(String.format("$%.2f", sub));
            lblTax .setText(String.format("$%.2f", tax));
            lblTot .setText(String.format("$%.2f", sub + tax + tip));
            lblPerc.setText(String.format("%.1f%%", sub == 0 ? 0 : (tip / sub * 100)));
        } catch (NumberFormatException ex) {
            lblPerc.setText("err");
        }
    }

    private void doPay() {
        try {
            double tip = Double.parseDouble(tipFld.getText());
            if (tip < 0) throw new NumberFormatException();

            /* record payment – the model / controller will create a new
               Bill snapshot that includes the tip */
            ctrl.handleCloseTable(tableId, tip);

            parent.refresh();          // update dashboard
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "please enter a valid non-negative tip amount",
                    "input error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
