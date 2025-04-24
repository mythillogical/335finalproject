package view;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/* edit menu.csv in-app */
public class MenuEditorPanel extends JPanel implements RestaurantModel.ModelListener {

    private final RestaurantController controller;
    private final DefaultTableModel tm = new DefaultTableModel(
            new String[]{"Name","Category","Base Cost"},0);

    private final JTextField nameTxt = new JTextField(12);
    private final JTextField catTxt  = new JTextField(8);
    private final JTextField costTxt = new JTextField(6);

    public MenuEditorPanel(RestaurantController c){
        controller=c;
        buildUi();
        refreshTable();
        controller.getModel().addListener(this);
    }
    @Override public void modelChanged(){ refreshTable(); }

    /*ui */
    private void buildUi(){
        setLayout(new BorderLayout(8,8));
        add(new JScrollPane(new JTable(tm)),BorderLayout.CENTER);

        JPanel south=new JPanel(new FlowLayout(FlowLayout.LEFT,6,0));
        south.add(new JLabel("Name"));
        south.add(nameTxt);
        south.add(new JLabel("Cat"));
        south.add(catTxt);
        south.add(new JLabel("Cost"));
        south.add(costTxt);

        JButton add=new JButton("Add"); south.add(add);
        add.addActionListener(e->doAdd());

        JButton rem=new JButton("Remove Selected"); south.add(rem);
        rem.addActionListener(e->doRemove());

        add(south,BorderLayout.SOUTH);
    }

    private void refreshTable(){
        tm.setRowCount(0);
        controller.getModel().getMenu().getAllItems()
                .forEach(i-> tm.addRow(new Object[]{
                        i.getName(), i.getCategory(),
                        String.format("$%.2f",i.getCost())}));
    }

    private void doAdd(){
        try{
            String n=nameTxt.getText().trim();
            String c=catTxt.getText().trim();
            double cost=Double.parseDouble(costTxt.getText().trim());
            if(n.isEmpty()||c.isEmpty()) return;
            controller.getModel().addMenuItem(new Item(n,c,cost));
            nameTxt.setText(""); catTxt.setText(""); costTxt.setText("");
        }catch(NumberFormatException ignored){}
    }
    private void doRemove(){
        int sel=((JTable)((JScrollPane)getComponent(0)).getViewport().getView())
                .getSelectedRow();
        if(sel<0) return;
        String n=(String)tm.getValueAt(sel,0);
        Item it=controller.getModel().getMenu().getAllItems().stream()
                .filter(x->x.getName().equals(n)).findFirst().orElse(null);
        if(it!=null) controller.getModel().removeMenuItem(it);
    }
}
