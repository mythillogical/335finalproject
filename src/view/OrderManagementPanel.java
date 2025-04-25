package view;

import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Dashboard showing Available / In-Progress / Recently-Closed tables. */
public class OrderManagementPanel extends JPanel
        implements RestaurantModel.ModelListener {

    /* mvc */
    private final RestaurantController ctrl;

    /* strips */
    private final JPanel panAvail  = new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));
    private final JPanel panBusy   = new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));
    private final JPanel panClosed = new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));

    public OrderManagementPanel(RestaurantController c){
        ctrl = c;

        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(10,10,10,10));

        JPanel rows = new JPanel(new GridLayout(3,1,0,10));
        rows.add(wrap("Available Tables", panAvail));
        rows.add(wrap("In Progress",      panBusy ));
        rows.add(wrap("Recently Closed",  panClosed));
        add(rows,BorderLayout.CENTER);

        refreshTablesPanels();
        ctrl.getModel().addListener(this);
    }

    @Override public void modelChanged(){ refreshTablesPanels(); }

    /* ---------- strip rebuild ---------- */
    private void refreshTablesPanels(){
        panAvail.removeAll();
        panBusy .removeAll();
        panClosed.removeAll();

        Tables tbls = ctrl.getModel().getTables();

        for(TableInfo ti: tbls.getAvailable(1)){
            Table t = tbls.getTable(ti.getId());
            if(t != null && !t.isOccupied()) panAvail.add(cardForAvailable(t));
        }
        for(Table t : tbls.getOccupiedTables()) panBusy.add(cardForBusy(t));
        for(Bill b : ctrl.getModel().getClosedTables()) panClosed.add(cardForClosed(b));

        revalidate(); repaint();
    }

    /* ---------- cards ---------- */
    private JPanel cardForAvailable(Table t){
        JPanel cd = makeCard(new Color(220,255,220));
        cd.add(label(t), BorderLayout.CENTER);
        cd.addMouseListener(new MouseAdapter(){
            @Override public void mouseClicked(MouseEvent e){ seatDialog(t); }
        });
        return cd;
    }

    private JPanel cardForBusy(Table t){
        JPanel cd = makeCard(new Color(255,255,220));
        cd.add(label(t), BorderLayout.NORTH);
        cd.add(new JLabel("Server: "+t.getServer().getName()), BorderLayout.CENTER);
        cd.add(new JLabel("Guests: "+t.getNumSeated()),         BorderLayout.SOUTH);
        cd.addMouseListener(new MouseAdapter(){
            @Override public void mouseClicked(MouseEvent e){ chooseBusyAction(t); }
        });
        return cd;
    }

    private JPanel cardForClosed(Bill b){
        JPanel cd = makeCard(new Color(255,220,220));
        cd.add(new JLabel("<html><b>Table "+ b.getServer()+"</b></html>"),
                BorderLayout.NORTH);
        cd.add(new JLabel("Total $"+String.format("%.2f", b.getTotalCost())),
                BorderLayout.CENTER);
        return cd;
    }

    /* ---------- busy-table dialog ---------- */
    private void chooseBusyAction(Table t){
        boolean empty = t.getItems().isEmpty();
        String first  = empty ? "Create order" : "Modify order";
        String[] ops  = { first, "Process payment" };

        int ch = JOptionPane.showOptionDialog(this,
                "Table "+t.getTableID()+": choose action",
                "Table Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, ops, ops[0]);

        if(ch == 0) openOrderWindow(t);
        else if(ch == 1) openPaymentWindow(t);
    }

    private void openOrderWindow(Table t){
        new OrderProcessingWindow(ctrl, t.getTableID(), this).setVisible(true);
    }
    private void openPaymentWindow(Table t){
        Bill b = ctrl.getModel().getTables().getBillTable(t.getTableID());
        if(b != null) new PaymentWindow(ctrl, b, t.getTableID(), this).setVisible(true);
    }

    /* ---------- seat-party dialog ---------- */
    private void seatDialog(Table t){

        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Seat table "+t.getTableID(), Dialog.ModalityType.APPLICATION_MODAL);

        dlg.setLayout(new BorderLayout(10,10));
        dlg.setSize(320,190);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(3,2,8,8));
        form.setBorder(new EmptyBorder(10,10,10,10));

        form.add(new JLabel("Server:"));
        JComboBox<String> srvBox = new JComboBox<>();
        ctrl.getModel().getServers().values()
                .forEach(s -> srvBox.addItem(s.getName()));
        form.add(srvBox);

        form.add(new JLabel("Guests:"));
        JSpinner spn = new JSpinner(
                new SpinnerNumberModel(1,1,t.getCapacity(),1));
        form.add(spn);

        form.add(new JLabel("Capacity:"));
        form.add(new JLabel(""+t.getCapacity()));

        dlg.add(form, BorderLayout.CENTER);

        JButton ok = new JButton("Seat");
        ok.addActionListener(e -> {
            String srv   = (String) srvBox.getSelectedItem();
            int guests   = (Integer) spn.getValue();
            if(ctrl.handleAssignTable(t.getTableID(), guests, srv))
                dlg.dispose();
        });
        dlg.add(ok, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    /* ---------- helpers ---------- */
    private static JPanel makeCard(Color bg){
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(170,80));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(6,6,6,6)));
        p.setBackground(bg);
        return p;
    }
    private static JLabel label(Table t){
        return new JLabel("<html><b>Table "+t.getTableID()+"</b></html>",
                SwingConstants.CENTER);
    }
    private static JPanel wrap(String title, JPanel body){
        JPanel w = new JPanel(new BorderLayout());
        w.setBorder(BorderFactory.createTitledBorder(title));
        w.add(new JScrollPane(body));
        return w;
    }

    /* external refresh from children */
    public void refresh(){ refreshTablesPanels(); }
}
