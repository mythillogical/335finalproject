package view;

import model.*;

import javax.swing.*;
import java.awt.*;

/* crud panel for servers */
public class ServerManagementPanel extends JPanel
        implements RestaurantModel.ModelListener {

    private final RestaurantController controller;
    private final DefaultListModel<Server> listModel = new DefaultListModel<>();
    private final JList<Server> serverList = new JList<>(listModel);
    private final JTextField nameField = new JTextField(18);

    public ServerManagementPanel(RestaurantController c){
        controller=c;
        buildUi();
        refresh();
        controller.getModel().addListener(this);
    }
    @Override public void modelChanged(){ refresh(); }

    private void buildUi(){
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(new JLabel("Server Management",SwingConstants.CENTER),BorderLayout.NORTH);
        add(new JScrollPane(serverList),BorderLayout.CENTER);

        JPanel south=new JPanel(new FlowLayout(FlowLayout.LEFT));
        south.add(new JLabel("Name:")); south.add(nameField);

        JButton add=new JButton("Add"); south.add(add);
        add.addActionListener(e->{
            String n=nameField.getText().trim();
            if(!n.isEmpty()){ controller.handleAddServer(n); nameField.setText(""); }
        });

        JButton rem=new JButton("Remove Selected"); south.add(rem);
        rem.addActionListener(e->{
            Server s=serverList.getSelectedValue();
            if(s!=null) controller.handleRemoveServer(s.getName());
        });
        add(south,BorderLayout.SOUTH);
    }
    private void refresh(){
        listModel.clear();
        controller.getModel().getServers().values().forEach(listModel::addElement);
    }
}
