package view;

import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Dashboard showing Available Tables / In Progress / Recently Closed.
 * Uses card-style panels that open the appropriate dialogs.
 */
public class OrderManagementPanel extends JPanel
        implements RestaurantModel.ModelListener {

    /* mvc handles */
    private final RestaurantController ctrl;

    /* layout panels */
    private final JPanel panAvail  = new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));
    private final JPanel panBusy   = new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));
    private final JPanel panClosed = new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));

    private final JButton btnBack  = new JButton("Back to main");

    public OrderManagementPanel(RestaurantController c){
        ctrl = c;

        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(10,10,10,10));

        add(btnBack, BorderLayout.NORTH);

        JPanel rows = new JPanel(new GridLayout(3,1,0,10));
        rows.add(wrap("Available Tables", panAvail));
        rows.add(wrap("In Progress",      panBusy));
        rows.add(wrap("Recently Closed",  panClosed));
        add(rows, BorderLayout.CENTER);

        refreshTablesPanels();
        ctrl.getModel().addListener(this);
    }

    /* external wiring */
    public JButton getBackButton(){ return btnBack; }

    @Override public void modelChanged(){ refreshTablesPanels(); }

    private void refreshTablesPanels(){
        panAvail .removeAll();
        panBusy  .removeAll();
        panClosed.removeAll();

        Tables tables = ctrl.getModel().getTables();

        /* available (NOT occupied) */
        for (TableInfo info : tables.getAvailable(1)){           // guests arg unused
            Table t = tables.getTable(info.getId());
            if(t!=null && !t.isOccupied())
                panAvail.add(cardForAvailable(t));
        }

        /* busy (occupied) */
        for (Table t : tables.getOccupiedTables())
            panBusy.add(cardForBusy(t));

        /* closed bills */
        for (Bill b : ctrl.getModel().getClosedTables())
            panClosed.add(cardForClosed(b));

        revalidate(); repaint();
    }

    /*card builders */

    private JPanel cardForAvailable(Table t){
        JPanel card = makeCard(new Color(220,255,220));
        card.add(label(t), BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter(){
            @Override public void mouseClicked(MouseEvent e){ seatDialog(t); }
        });
        return card;
    }

    private JPanel cardForBusy(Table t){
        JPanel card = makeCard(new Color(255,255,220));
        card.add(label(t), BorderLayout.NORTH);
        card.add(new JLabel("Server: "+t.getServer().getName()), BorderLayout.CENTER);
        card.add(new JLabel("Guests: "+t.getNumSeated()),         BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter(){
            @Override public void mouseClicked(MouseEvent e){ chooseBusyAction(t); }
        });
        return card;
    }

    private JPanel cardForClosed(Bill b){
        JPanel card = makeCard(new Color(255,220,220));
        card.add(new JLabel("<html><b>Table "
                + b.getServer().getName()+"</b></html>"), BorderLayout.NORTH);
        card.add(new JLabel("Total $" + String.format("%.2f", b.getTotalCost())),
                BorderLayout.CENTER);
        return card;
    }

    /* dialog helpers */

    private void chooseBusyAction(Table t){
        String[] ops = {"Modify order","Process payment"};
        int ch = JOptionPane.showOptionDialog(this,
                "Table "+t.getTableID()+": choose action",
                "Table Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, ops, ops[0]);

        if(ch==0) openOrderWindow(t);
        else if(ch==1) openPaymentWindow(t);
    }

    private void openOrderWindow(Table t){
        new OrderProcessingWindow(ctrl, t.getTableID(), this).setVisible(true);
    }

    private void openPaymentWindow(Table t){
        Bill b = ctrl.getModel().getTables().getBillTable(t.getTableID());
        if(b==null) return;
        new PaymentWindow(ctrl, b, t.getTableID(), this).setVisible(true);
    }

    /* seat-table dialog */
    private void seatDialog(Table t){

        /* (Window,String,ModalityType) */
        JDialog dlg = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                "Seat table " + t.getTableID(),
                Dialog.ModalityType.APPLICATION_MODAL);

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
            String srv = (String)srvBox.getSelectedItem();
            int guests = (Integer)spn.getValue();
            if(ctrl.handleAssignTable(t.getTableID(), guests, srv))
                dlg.dispose();
        });
        dlg.add(ok, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    /* helpers */

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
