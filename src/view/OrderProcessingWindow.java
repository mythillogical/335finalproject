package view;

import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Per-table order window – lets a server add / remove items for the live ticket.
 */
public class OrderProcessingWindow extends JFrame {

    /* MVC hooks */
    private final RestaurantController ctrl;
    private final int  tableId;
    private final OrderManagementPanel parent;

    /* state */
    private final List<Item> existing = new ArrayList<>();   // items already on the ticket
    private final List<Item> newItems = new ArrayList<>();   // items added in this session

    /* ui bits we need to mutate */
    private final JTextArea txtSummary = new JTextArea();

    public OrderProcessingWindow(RestaurantController c,
                                 int tableId,
                                 OrderManagementPanel parent) {

        this.ctrl    = c;
        this.tableId = tableId;
        this.parent  = parent;

        Bill snap = ctrl.getModel().getTables().getBillTable(tableId);
        if (snap != null) existing.addAll(snap.getItems());

        setTitle("table " + tableId + " – order");
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        buildUi();
        refreshSummary();
    }

    private void buildUi() {

        /* top info bar */
        Table t = ctrl.getModel().getTables().getTable(tableId);

        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 4));
        info.add(new JLabel("server: " +
                (t.getServer() != null ? t.getServer().getName() : "-")));
        info.add(new JLabel("guests: " + t.getNumSeated()));
        add(info, BorderLayout.NORTH);

        /* split pane */
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.70);
        add(split, BorderLayout.CENTER);

        /* left: MENU TABS */
        JTabbedPane tabs = new JTabbedPane();

        model.Menu menu = ctrl.getModel().getMenu();   // fully-qualified to avoid AWT clash
        List<String> cats = menu.getAllItems()
                .stream()
                .map(Item::getCategory)
                .distinct()
                .collect(Collectors.toList());

        for (String cat : cats) {
            JPanel grid = new JPanel(new GridLayout(0, 3, 8, 8));
            grid.setBorder(new EmptyBorder(10, 10, 10, 10));

            for (Item it : menu.getAllItems()) {
                if (it.getCategory().equals(cat))
                    grid.add(makeItemButton(it));
            }
            JScrollPane sc = new JScrollPane(grid);
            sc.getVerticalScrollBar().setUnitIncrement(16);
            tabs.addTab(cat, sc);
        }

        split.setLeftComponent(tabs);

        /* right: ORDER SUMMARY */
        JPanel order = new JPanel(new BorderLayout(0, 10));
        order.setBorder(BorderFactory.createTitledBorder("current order"));

        txtSummary.setEditable(false);
        txtSummary.setFont(new Font("monospaced", Font.PLAIN, 14));
        order.add(new JScrollPane(txtSummary), BorderLayout.CENTER);

        /* buttons */
        JPanel btns = new JPanel(new GridLayout(1, 3, 6, 0));
        JButton btnRemove = new JButton("remove selected");
        JButton btnClear  = new JButton("clear new");
        JButton btnSend   = new JButton("submit order");
        btns.add(btnRemove); btns.add(btnClear); btns.add(btnSend);
        order.add(btns, BorderLayout.SOUTH);

        split.setRightComponent(order);

        /* listeners */
        btnClear .addActionListener(e -> { newItems.clear(); refreshSummary(); });

        btnRemove.addActionListener(e -> {
            String sel = txtSummary.getSelectedText();
            if (sel != null) removeByName(sel.trim());
        });

        btnSend.addActionListener(e -> submitOrder());

        /* bottom close */
        JButton close = new JButton("close");
        close.addActionListener(e -> dispose());
        add(close, BorderLayout.SOUTH);
    }

    /*helpers*/

    private JButton makeItemButton(Item src) {
        JButton b = new JButton("<html><center>" + src.getName() +
                "<br>$" + String.format("%.2f", src.getCost()) + "</center></html>");
        b.addActionListener(e -> {
            newItems.add(src);
            refreshSummary();
        });
        return b;
    }

    private void refreshSummary() {

        StringBuilder sb = new StringBuilder();
        double total = 0;

        if (!existing.isEmpty()) {
            sb.append("EXISTING ITEMS:\n");
            for (Item i : existing) {
                sb.append(String.format("%-28s $%.2f%n", i.getName(), i.getCost()));
                total += i.getCost();
            }
            sb.append('\n');
        }

        if (!newItems.isEmpty()) {
            sb.append("NEW ITEMS:\n");
            for (Item i : newItems) {
                sb.append(String.format("%-28s $%.2f%n", i.getName(), i.getCost()));
                total += i.getCost();
            }
            sb.append('\n');
        }

        sb.append("\nTOTAL: $").append(String.format("%.2f", total));
        txtSummary.setText(sb.toString());
    }

    private void submitOrder() {
        if (newItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "no new items to submit");
            return;
        }
        ctrl.handleAddOrder(tableId, new ArrayList<>(newItems));
        newItems.clear();

        /* refresh local copy of existing items */
        Bill snap = ctrl.getModel().getTables().getBillTable(tableId);
        existing.clear();
        if (snap != null) existing.addAll(snap.getItems());

        refreshSummary();
        parent.refresh();
        JOptionPane.showMessageDialog(this, "order updated");
    }

    /** try to remove by name – new items first, then ask Tables to remove */
    private void removeByName(String name) {

        if (newItems.removeIf(i -> i.getName().equals(name))) {
            refreshSummary();
            return;
        }

        for (Item i : existing) {
            if (i.getName().equals(name)) {
                boolean ok = ctrl.getModel().getTables().removeItemFromTable(tableId, i);
                if (ok) {
                    existing.remove(i);
                    refreshSummary();
                }
                return;
            }
        }
    }
}
