package view;

import controller.RestaurantController;
import model.*;

import javax.swing.*;
import java.awt.*;

/* nicer CRUD ui for servers – includes active-table guard */
public class ServerManagementPanel extends JPanel
        implements RestaurantModel.ModelListener {

    private final RestaurantController ctrl;
    private final DefaultListModel<Server> listModel = new DefaultListModel<>();
    private final JList<Server>            lst       = new JList<>(listModel);
    private final JTextField nameTxt = new JTextField(18);

    public ServerManagementPanel(RestaurantController c){
        ctrl = c;
        buildUi(); refresh();
        ctrl.getModel().addListener(this);
    }
    @Override public void modelChanged(){ refresh(); }

    private void buildUi(){
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(new JLabel("server management",SwingConstants.CENTER),BorderLayout.NORTH);
        add(new JScrollPane(lst),BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        south.add(new JLabel("Name:")); south.add(nameTxt);

        JButton addBtn = new JButton("Add"); south.add(addBtn);
        addBtn.addActionListener(e -> {
            String n = nameTxt.getText().trim();
            if(!n.isEmpty()){ ctrl.handleAddServer(n); nameTxt.setText(""); }
        });

        JButton delBtn = new JButton("Remove Selected"); south.add(delBtn);
        delBtn.addActionListener(e -> {
            Server s = lst.getSelectedValue();
            if(s==null) return;
            if(ctrl.checkActiveServer(s.getName())){
                JOptionPane.showMessageDialog(this,
                        "server still has active tables","warning",JOptionPane.WARNING_MESSAGE);
                return;
            }
            ctrl.handleRemoveServer(s.getName());
        });
        add(south,BorderLayout.SOUTH);
    }
    private void refresh(){
        listModel.clear();
        ctrl.getModel().getServers().values().forEach(listModel::addElement);
    }
}
