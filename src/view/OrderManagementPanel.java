package view;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/* screen to seat / close tables and add items + mods */
public class OrderManagementPanel extends JPanel
        implements RestaurantModel.ModelListener { // hook into model events

    private final RestaurantController controller;

    /* selectors & fields */
    private final JComboBox<Integer> tableBox  = new JComboBox<>();
    private final JTextField         guestsTxt = new JTextField(4);
    private final JComboBox<String>  serverBox = new JComboBox<>();
    private final JTextField         tipTxt    = new JTextField(4);

    private final JComboBox<String>  menuBox   = new JComboBox<>();
    private final JComboBox<String>  modBox    = new JComboBox<>();

    private final DefaultTableModel  orderTm =
            new DefaultTableModel(new String[]{"Item","Cost"},0);

    public OrderManagementPanel(RestaurantController c) {
        controller = c;
        buildUI();
        refreshTableList();
        refreshServers();
        refreshMods();          // populate with first item’s mods
        refreshOrder();

        /* register for live updates (e.g. new servers) */
        controller.getModel().addListener(this);
    }

    /* ------------ model listener callback ------------ */
    @Override public void modelChanged() {
        refreshServers();                       // keep server combo in sync
    }

    /*layout*/
    private void buildUI() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        /* top bar */
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        top.add(new JLabel("Table:"));
        tableBox.setRenderer(new TableComboRenderer());
        tableBox.addActionListener(e -> refreshOrder());
        top.add(tableBox);

        top.add(new JLabel("Guests:"));
        top.add(guestsTxt);

        top.add(new JLabel("Server:"));
        top.add(serverBox);

        JButton seatBtn = new JButton("Seat Table");
        seatBtn.addActionListener(e -> seatTable());
        top.add(seatBtn);

        top.add(new JLabel("Tip:"));
        top.add(tipTxt);

        JButton closeBtn = new JButton("Close Table");
        closeBtn.addActionListener(e -> closeTable());
        top.add(closeBtn);
        add(top, BorderLayout.NORTH);

        /* order list */
        add(new JScrollPane(new JTable(orderTm)), BorderLayout.CENTER);

        /* bottom add bar */
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        bot.add(new JLabel("Menu Item:"));
        menuBox.addActionListener(e -> refreshMods());
        bot.add(menuBox);

        bot.add(new JLabel("Mod:"));
        bot.add(modBox);

        JButton addBtn = new JButton("Add to Order");
        addBtn.addActionListener(e -> addItem());
        bot.add(addBtn);
        add(bot, BorderLayout.SOUTH);
    }

    /*refresh helpers*/
    private void refreshTableList() {
        tableBox.removeAllItems();
        controller.getModel().getTables().getTablesInfo()
                .forEach(t -> tableBox.addItem(t.getId()));
        if(tableBox.getItemCount()>0) tableBox.setSelectedIndex(0);

        if(menuBox.getItemCount()==0)
            controller.getModel().getMenu().getAllItems()
                    .forEach(i -> menuBox.addItem(i.getName()));
    }

    /* called from modelChanged + constructor */
    void refreshServers() {
        String prev = (String) serverBox.getSelectedItem();
        serverBox.removeAllItems();
        controller.getModel().getServers().values()
                .forEach(s -> serverBox.addItem(s.getName()));
        if(prev != null) serverBox.setSelectedItem(prev);
        if(serverBox.getItemCount()>0 && serverBox.getSelectedIndex()<0)
            serverBox.setSelectedIndex(0);
    }

    /* rebuild mod selector based on chosen base item */
    private void refreshMods() {
        modBox.removeAllItems();
        String sel = (String) menuBox.getSelectedItem();
        Item base = controller.getModel().getMenu().getAllItems().stream()
                .filter(i -> i.getName().equals(sel)).findFirst().orElse(null);
        if(base==null) return;
        modBox.addItem("None");                       // default
        for(Modification m: base.getModifications())
            modBox.addItem(m.getDescription());
    }

    /* rebuild ticket table for currently selected table */
    private void refreshOrder() {
        orderTm.setRowCount(0);
        Integer id = (Integer) tableBox.getSelectedItem();
        if(id==null) return;
        Table t = controller.getModel().getTables().getTable(id);
        if(t==null||!t.isOccupied()) return;
        for(Item i: t.getItems())
            orderTm.addRow(new Object[]{i.getName(),String.format("$%.2f",i.getTotalCost())});
    }

    /*actions*/
    private void seatTable() {
        try {
            int guests = Integer.parseInt(guestsTxt.getText().trim());
            int id     = (Integer) tableBox.getSelectedItem();
            String srv = (String) serverBox.getSelectedItem();
            if(srv==null) { warn("select a server"); return; }
            if(controller.handleAssignTable(id,guests,srv)){
                info("table "+id+" seated");
                refreshOrder(); tableBox.repaint();
            }
        } catch(NumberFormatException ex){ error("guests must be a number"); }
    }

    private void closeTable() {
        try{
            double tip = Double.parseDouble(tipTxt.getText().trim());
            int id = (Integer) tableBox.getSelectedItem();
            controller.handleCloseTable(id,tip);
            info("table "+id+" closed");
            refreshOrder(); tableBox.repaint();
        } catch(NumberFormatException ex){ error("tip must be a number"); }
    }

    private void addItem() {
        int id = (Integer) tableBox.getSelectedItem();
        Table t = controller.getModel().getTables().getTable(id);
        if(t==null||!t.isOccupied()){ warn("seat table first"); return; }

        String baseName = (String) menuBox.getSelectedItem();
        Item base = controller.getModel().getMenu().getAllItems().stream()
                .filter(i -> i.getName().equals(baseName)).findFirst().orElse(null);
        if(base==null) return;

        Item fresh = new Item(base);                // clone to attach chosen mod
        String modDesc = (String) modBox.getSelectedItem();
        if(modDesc!=null && !modDesc.equals("None")){
            base.getModifications().stream()
                    .filter(m -> m.getDescription().equals(modDesc))
                    .findFirst().ifPresent(fresh::addModification);
        }
        controller.handleAddOrder(id,new ArrayList<>(List.of(fresh)));
        refreshOrder();
    }

    /*utilities*/
    private void warn (String m){ JOptionPane.showMessageDialog(this,m,"Warning",JOptionPane.WARNING_MESSAGE);}
    private void info (String m){ JOptionPane.showMessageDialog(this,m,"Info",   JOptionPane.INFORMATION_MESSAGE);}
    private void error(String m){ JOptionPane.showMessageDialog(this,m,"Error",  JOptionPane.ERROR_MESSAGE);}

    /* colour tables green/ red in combo */
    private class TableComboRenderer extends DefaultListCellRenderer{
        @Override public Component getListCellRendererComponent(JList<?> list,
                                                                Object value,int idx,boolean sel,boolean foc){
            JLabel l=(JLabel)super.getListCellRendererComponent(list,value,idx,sel,foc);
            if(value instanceof Integer id){
                Table t=controller.getModel().getTables().getTable(id);
                l.setForeground((t!=null&&t.isOccupied())?new Color(170,30,30):new Color(0,128,0));
            }
            return l;
        }
    }
}
