package view;

import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * In-app menu editor with a user-friendly modification builder.
 *
 *  • “Add Item” form lets you specify name / category / price **and**
 *    build a list of modifications with an inline mini-form.
 *  • Existing items appear in the table with their mods; select a row and
 *    press <Edit Mods…> to tweak the mod list in a dedicated dialog.
 *  • No CSV typing required — everything is point-and-click.
 */
public class MenuEditorPanel extends JPanel
        implements RestaurantModel.ModelListener {

    /* ---------- MVC ---------- */
    private final RestaurantController ctrl;

    /* ---------- main table ---------- */
    private final DefaultTableModel tblModel = new DefaultTableModel(
            new String[]{"Name","Category","Base Cost","Mods"}, 0);

    /* ---------- “add item” widgets ---------- */
    private final JTextField txtName = new JTextField(12);
    private final JTextField txtCat  = new JTextField(8);
    private final JTextField txtCost = new JTextField(6);

    /* ---------- “add modification” widgets ---------- */
    private final JTextField txtModDesc  = new JTextField(10);
    private final JTextField txtModPrice = new JTextField(4);
    private final DefaultListModel<Modification> modModel = new DefaultListModel<>();
    private final JList<Modification>            lstMods  = new JList<>(modModel);

    /* ------------------------------------------------------------------ */
    public MenuEditorPanel(RestaurantController c) {
        ctrl = c;
        buildUi();
        refreshTable();
        ctrl.getModel().addListener(this);
    }
    @Override public void modelChanged() { refreshTable(); }

    /* ======================  UI  ====================== */
    private void buildUi() {
        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(8,8,8,8));

        /* ---------- centre: items table ---------- */
        JTable tbl = new JTable(tblModel);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tbl), BorderLayout.CENTER);

        /* ---------- south: forms & buttons ---------- */
        JPanel south = new JPanel(new BorderLayout(8,8));
        add(south, BorderLayout.SOUTH);

        /* --- item core fields --- */
        JPanel core = new JPanel(new FlowLayout(FlowLayout.LEFT,6,0));
        core.add(new JLabel("Name")); core.add(txtName);
        core.add(new JLabel("Cat"));  core.add(txtCat);
        core.add(new JLabel("Cost")); core.add(txtCost);
        south.add(core, BorderLayout.NORTH);

        /* --- mods mini-form --- */
        JPanel modsPane = new JPanel(new FlowLayout(FlowLayout.LEFT,6,0));
        modsPane.setBorder(BorderFactory.createTitledBorder("Mods"));
        modsPane.add(new JLabel("Desc"));  modsPane.add(txtModDesc);
        modsPane.add(new JLabel("Price")); modsPane.add(txtModPrice);
        JButton btnAddMod = new JButton("Add Mod"); modsPane.add(btnAddMod);
        JButton btnDelMod = new JButton("Remove Selected"); modsPane.add(btnDelMod);
        modsPane.add(new JScrollPane(lstMods));
        south.add(modsPane, BorderLayout.CENTER);

        /* --- action buttons --- */
        JPanel btns = new JPanel(new GridLayout(3,1,0,6));
        JButton btnAddItem  = new JButton("Add Item");
        JButton btnDelItem  = new JButton("Delete Selected");
        JButton btnEditMods = new JButton("Edit Mods…");
        btns.add(btnAddItem); btns.add(btnDelItem); btns.add(btnEditMods);
        south.add(btns, BorderLayout.EAST);

        /* ---------- listeners ---------- */
        btnAddMod.addActionListener(e -> onAddMod());
        btnDelMod.addActionListener(e ->
                lstMods.getSelectedValuesList().forEach(modModel::removeElement));

        btnAddItem.addActionListener(e -> onAddItem());

        btnDelItem.addActionListener(e -> {
            int row = tbl.getSelectedRow();
            if (row < 0) return;
            String name = (String) tblModel.getValueAt(row,0);
            Item victim = ctrl.getModel().getMenu().getAllItems().stream()
                    .filter(i -> i.getName().equals(name))
                    .findFirst().orElse(null);
            if (victim != null) ctrl.getModel().removeMenuItem(victim);
        });

        btnEditMods.addActionListener(e -> {
            int row = tbl.getSelectedRow();
            if (row < 0) return;
            String name = (String) tblModel.getValueAt(row,0);
            Item old = ctrl.getModel().getMenu().getAllItems().stream()
                    .filter(i -> i.getName().equals(name))
                    .findFirst().orElse(null);
            if (old == null) return;

            ModEditorDialog dlg = new ModEditorDialog(
                    SwingUtilities.getWindowAncestor(this), old);
            dlg.setVisible(true);

            List<Modification> newMods = dlg.getResult();
            if (newMods == null) return;      // cancelled

            Item updated = new Item(old.getName(), old.getCategory(),
                    old.getCost(), newMods);
            ctrl.getModel().removeMenuItem(old);
            ctrl.getModel().addMenuItem(updated);
        });
    }

    /* ======================  helpers  ====================== */
    private void onAddMod() {
        try {
            String d = txtModDesc.getText().trim();
            double p = Double.parseDouble(txtModPrice.getText().trim());
            if (d.isEmpty()) return;

            modModel.addElement(new Modification(d, p));
            txtModDesc.setText(""); txtModPrice.setText("");
        } catch (NumberFormatException ignored) { /* bad price */ }
    }

    private void onAddItem() {
        try {
            String name = txtName.getText().trim();
            String cat  = txtCat .getText().trim();
            double cost = Double.parseDouble(txtCost.getText().trim());
            if (name.isEmpty() || cat.isEmpty()) return;

            List<Modification> mods = new ArrayList<>();
            modModel.elements().asIterator().forEachRemaining(mods::add);

            ctrl.getModel().addMenuItem(new Item(name, cat, cost, mods));

            /* reset form */
            txtName.setText(""); txtCat.setText(""); txtCost.setText("");
            modModel.clear();
        } catch (NumberFormatException ignored) {}
    }

    private void refreshTable() {
        tblModel.setRowCount(0);
        ctrl.getModel().getMenu().getAllItems()
                .forEach(i -> tblModel.addRow(new Object[]{
                        i.getName(),
                        i.getCategory(),
                        String.format("$%.2f", i.getCost()),
                        i.modsToCsv()
                }));
    }

    /* ====================================================== */
    /** inner dialog class (no extra source file) */
    private static final class ModEditorDialog extends JDialog {

        private final DefaultListModel<Modification> mdl = new DefaultListModel<>();
        private List<Modification> result = null;   // returned on OK

        ModEditorDialog(Window owner, Item item) {
            super(owner, "Mods – "+item.getName(), ModalityType.APPLICATION_MODAL);
            item.getModifications().forEach(mdl::addElement);
            buildUi();
            setSize(340,280);
            setLocationRelativeTo(owner);
        }
        List<Modification> getResult() { return result; }

        /* ---- ui ---- */
        private void buildUi() {
            setLayout(new BorderLayout(8,8));
            ((JComponent)getContentPane()).setBorder(new EmptyBorder(8,8,8,8));

            JList<Modification> list = new JList<>(mdl);
            add(new JScrollPane(list), BorderLayout.CENTER);

            JTextField desc = new JTextField(10);
            JTextField price = new JTextField(4);
            JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT,6,0));
            form.add(new JLabel("Desc"));  form.add(desc);
            form.add(new JLabel("Price")); form.add(price);
            JButton add = new JButton("Add"); form.add(add);
            JButton del = new JButton("Remove Selected"); form.add(del);
            add(form, BorderLayout.NORTH);

            JButton ok = new JButton("OK");
            add(ok, BorderLayout.SOUTH);

            /* listeners */
            add.addActionListener(e -> {
                try {
                    String d = desc.getText().trim();
                    double p = Double.parseDouble(price.getText().trim());
                    if (d.isEmpty()) return;
                    mdl.addElement(new Modification(d,p));
                    desc.setText(""); price.setText("");
                } catch (NumberFormatException ignored) {}
            });
            del.addActionListener(e ->
                    list.getSelectedValuesList().forEach(mdl::removeElement));
            ok.addActionListener(e -> {
                result = new ArrayList<>();
                mdl.elements().asIterator().forEachRemaining(result::add);
                dispose();
            });
        }
    }
}
