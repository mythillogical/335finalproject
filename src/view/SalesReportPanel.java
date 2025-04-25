package view;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

/* shows quantities, turnover, tips + csv export */
public class SalesReportPanel extends JPanel
        implements RestaurantModel.ModelListener {

    private final RestaurantController controller;
    private final DefaultTableModel tm =
            new DefaultTableModel(new String[]{"Item","Qty"},0);

    private final JLabel lblRev  = new JLabel("$0.00");
    private final JLabel lblTips = new JLabel("$0.00");

    public SalesReportPanel(RestaurantController c){
        controller=c;
        buildUi();
        refresh();
        controller.getModel().addListener(this);
    }
    @Override public void modelChanged(){ refresh(); }

    private void buildUi(){
        setLayout(new BorderLayout(10,10));
        add(new JScrollPane(new JTable(tm)),BorderLayout.CENTER);

        JPanel south=new JPanel(new FlowLayout(FlowLayout.LEFT,12,0));
        south.add(new JLabel("Revenue:")); south.add(lblRev);
        south.add(new JLabel("Tips:"));    south.add(lblTips);

        JButton csv=new JButton("Export CSV"); south.add(csv);
        csv.addActionListener(e->exportCsv());

        add(south,BorderLayout.SOUTH);
    }

    private void refresh(){
        Map<String,Integer> tally=new TreeMap<>();
        double rev=0,tips=0;
        for(Bill b: controller.getModel().getClosedTables()){
            b.getItems().forEach(i-> tally.merge(i.getName(),1,Integer::sum));
            rev  += b.getItemsCost();
            tips += b.getTip();
        }
        tm.setRowCount(0);
        if(tally.isEmpty()) tm.addRow(new Object[]{"<no data>",0});
        else tally.forEach((n,q)-> tm.addRow(new Object[]{n,q}));

        lblRev .setText(String.format("$%.2f",rev));
        lblTips.setText(String.format("$%.2f",tips));
    }
    private void exportCsv(){
        try{
            Path p=Files.createTempFile("sales-",".csv");
            try(FileWriter w=new FileWriter(p.toFile())){
                w.write("Item,Qty\n");
                for(int r=0;r<tm.getRowCount();r++)
                    w.write(tm.getValueAt(r,0)+","+tm.getValueAt(r,1)+"\n");
                w.write("\nRevenue,"+lblRev.getText().substring(1)+"\n");
                w.write("Tips,"   +lblTips.getText().substring(1)+"\n");
            }
            Desktop.getDesktop().open(p.toFile());
        }catch(Exception ignored){}
    }
}