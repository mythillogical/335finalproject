package view;

import controller.RestaurantController;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Sub-window for processing a new order for a specific table.
 * 
 * Allows item selection and finalization of the order.
 * 
 * Tied to the Order Management Panel.
 * 
 * Author: Michael B, Michael D, Asif R, Mohammed A
 * Course: CSc 335 - Object-Oriented Programming and Design
 * Semester: Spring 2025
 */
public class OrderProcessingWindow extends JFrame {

    /* mvc */
    private final RestaurantController ctrl;
    private final int tableId;
    private final OrderManagementPanel parent;

    /* state */
    private final List<Item> existing = new ArrayList<>();
    private final List<Item> newItems = new ArrayList<>();

    /* ui models */
    private final DefaultListModel<Item> orderModel = new DefaultListModel<>();
    private final JList<Item>            lstOrder   = new JList<>(orderModel);
    private final JLabel                 lblTotal   = new JLabel("Total: $0.00");

    public OrderProcessingWindow(RestaurantController c, int tableId,
                                 OrderManagementPanel parent) {

        this.ctrl    = c;
        this.tableId = tableId;
        this.parent  = parent;

        Bill snap = ctrl.getModel().getTables().getBillTable(tableId);
        if (snap != null) existing.addAll(snap.getItems());

        setTitle("table " + tableId + " – order");
        setSize(920, 620);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        buildUi();
        refreshOrderList();
    }

    private void buildUi() {

        /* Info bar */
        Table t = ctrl.getModel().getTables().getTable(tableId);
        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 4));
        info.add(new JLabel("server: " +
                (t.getServer() != null ? t.getServer().getName() : "-")));
        info.add(new JLabel("guests: " + t.getNumSeated()));
        add(info, BorderLayout.NORTH);

        /* Split: menu (L) | order (R) */
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.5);                // 50 / 50 by default
        add(split, BorderLayout.CENTER);

        split.setLeftComponent(buildMenuTabs());
        split.setRightComponent(buildOrderPane());

        /* bottom close */
        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());
        add(close, BorderLayout.SOUTH);
    }

    /* Menu (left) */
    private JComponent buildMenuTabs() {

        model.Menu menu = ctrl.getModel().getMenu();   // fq-name to dodge java.awt.Menu
        JTabbedPane tabs = new JTabbedPane();

        List<String> cats = menu.getAllItems().stream()
                .map(Item::getCategory).distinct().collect(Collectors.toList());

        for (String cat : cats) {
            JPanel grid = new JPanel(new GridLayout(0, 3, 8, 8));
            grid.setBorder(new EmptyBorder(10, 10, 10, 10));

            for (Item it : menu.getAllItems())
                if (it.getCategory().equals(cat))
                    grid.add(makeItemButton(it));

            JScrollPane sc = new JScrollPane(grid);
            sc.getVerticalScrollBar().setUnitIncrement(16);
            tabs.addTab(cat, sc);
        }
        return tabs;
    }

    private JButton makeItemButton(Item base) {
        JButton b = new JButton("<html><center>" + base.getName() +
                "<br>$" + String.format("%.2f", base.getCost()) + "</center></html>");
        b.addActionListener(e -> {
            Item customised = chooseModsGui(base, /*initial*/ null);
            newItems.add(customised);
            refreshOrderList();
        });
        return b;
    }

    /* Order pane (right) */
    private JComponent buildOrderPane() {

        /* list renderer */
        lstOrder.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstOrder.setFont(new Font("monospaced", Font.PLAIN, 14));
        lstOrder.setCellRenderer((JList<? extends Item> list, Item value,
                                  int index, boolean sel, boolean focus) -> {
            JLabel l = new JLabel(String.format("%s  $%.2f",
                    value.getName(), value.getTotalCost()));
            l.setOpaque(true);
            l.setBackground(sel ? list.getSelectionBackground() : list.getBackground());
            l.setForeground(sel ? list.getSelectionForeground() : list.getForeground());
            return l;
        });

        JScrollPane scOrder = new JScrollPane(lstOrder);

        /* buttons row */
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton bRemove = new JButton("Remove Selected");
        JButton bEdit   = new JButton("Edit Mods");
        JButton bClear  = new JButton("Clear New");
        JButton bSend   = new JButton("Submit Order");
        btns.add(bRemove); btns.add(bEdit); btns.add(bClear); btns.add(bSend);

        /* footer (total + buttons) */
        JPanel footer = new JPanel(new BorderLayout());
        footer.add(lblTotal, BorderLayout.WEST);
        footer.add(btns,     BorderLayout.EAST);

        /* vertical split – list top, buttons bottom */
        JSplitPane vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                scOrder, footer);
        vSplit.setResizeWeight(0.9);
        vSplit.setDividerSize(6);

        /* button actions */
        bRemove.addActionListener(e -> doRemove());
        bEdit  .addActionListener(e -> doEditMods());
        bClear .addActionListener(e -> { newItems.clear(); refreshOrderList(); });
        bSend  .addActionListener(e -> doSubmit());

        return vSplit;
    }

    /* helpers */
    private void refreshOrderList() {

        orderModel.clear();
        existing.forEach(orderModel::addElement);
        newItems .forEach(orderModel::addElement);

        double tot = 0;
        Enumeration<Item> en = orderModel.elements();
        while (en.hasMoreElements()) tot += en.nextElement().getTotalCost();
        lblTotal.setText("Total: $" + String.format("%.2f", tot));
    }

    private void doRemove() {
        Item sel = lstOrder.getSelectedValue();
        if (sel == null) return;

        if (newItems.remove(sel)) { refreshOrderList(); return; }

        if (existing.contains(sel)) {
            if (ctrl.getModel().getTables()
                    .removeItemFromTable(tableId, sel)) {
                existing.remove(sel); refreshOrderList();
            }
        }
    }

    private void doEditMods() {

        Item sel = lstOrder.getSelectedValue();
        if (sel == null) return;

        /* #edit-mods */
        /* locate the *menu* version of this item so we can offer the full
           list of available modifications (the selected instance only
           remembers the mods already chosen).                             */
        Item menuBase = ctrl.getModel().getMenu().getAllItems().stream()
                .filter(it -> it.getName().equals(sel.getName()))
                .findFirst().orElse(sel);

        Item updated = chooseModsGui(menuBase, sel.getModifications());
        if (updated == sel) return;           // cancelled

        if (newItems.remove(sel))   newItems.add(updated);
        else {
            existing.remove(sel);
            existing.add(updated);
        }
        refreshOrderList();
    }

    private void doSubmit() {
        if (newItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No new items to submit"); return;
        }
        ctrl.handleAddOrder(tableId, new ArrayList<>(newItems));
        newItems.clear();

        Bill snap = ctrl.getModel().getTables().getBillTable(tableId);
        existing.clear();
        if (snap != null) existing.addAll(snap.getItems());

        refreshOrderList(); parent.refresh();
        JOptionPane.showMessageDialog(this, "Order updated");
    }

    /* modification selector*/
    /**
     * Menu-style dialog: left = grid of mod buttons, right = list of chosen;
     * supports add / remove before confirming.
     *
     * @param base     item whose mods to show
     * @param initial  mods pre-selected (edit case) or null
     * @return         customised Item (or original if cancelled)
     */
    private Item chooseModsGui(Item base, List<Modification> initial) {

        List<Modification> all = base.getModifications();
        if (all.isEmpty()) return base;

        /* chosen list model & ui */
        DefaultListModel<Modification> mdlChosen = new DefaultListModel<>();
        JList<Modification> lstChosen = new JList<>(mdlChosen);
        lstChosen.setVisibleRowCount(8);

        /* map each mod to its button so we can toggle enable/disable */
        Map<Modification, JButton> btnMap = new HashMap<>();

        JPanel grid = new JPanel(new GridLayout(0, 2, 6, 6));
        grid.setBorder(new EmptyBorder(6, 6, 6, 6));

        for (Modification m : all) {
            JButton b = new JButton(String.format("%s ($%.2f)",
                    m.getDescription(), m.getPrice()));
            btnMap.put(m, b);
            b.addActionListener(e -> {
                mdlChosen.addElement(m);
                b.setEnabled(false);
            });
            grid.add(b);
        }

        JButton bRemove = new JButton("Remove Selected");
        bRemove.addActionListener(e -> {
            for (Modification m : lstChosen.getSelectedValuesList()) {
                mdlChosen.removeElement(m);
                btnMap.get(m).setEnabled(true);
            }
        });

        /* pre-select if editing */
        if (initial != null) {
            for (Modification m : initial) {
                mdlChosen.addElement(m);
                JButton b = btnMap.get(m);
                if (b != null) b.setEnabled(false);
            }
        }

        /* layout dialog */
        JPanel right = new JPanel(new BorderLayout(6, 4));
        right.add(new JScrollPane(lstChosen), BorderLayout.CENTER);
        right.add(bRemove, BorderLayout.SOUTH);

        JPanel body = new JPanel(new BorderLayout(10, 0));
        body.add(grid,  BorderLayout.CENTER);
        body.add(right, BorderLayout.EAST);

        int ok = JOptionPane.showConfirmDialog(this, body,
                "Choose Modifications", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (ok != JOptionPane.OK_OPTION) return base;

        List<Modification> chosen = new ArrayList<>();
        mdlChosen.elements().asIterator().forEachRemaining(chosen::add);

        return new Item(base.getName(), base.getCategory(),
                base.getCost(), chosen);
    }
}
