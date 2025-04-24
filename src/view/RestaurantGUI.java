package view;

import model.*;
import javax.swing.*;
import java.awt.*;

public class RestaurantGUI extends JFrame {

    /* mvc */
    private final RestaurantModel  model = new RestaurantModel();
    private final RestaurantView   view  = new RestaurantView(this);
    private final RestaurantController controller =
            new RestaurantController(model, view);

    /* nav buttons (tables first, then menu editor) */
    private final JButton btnTables = new JButton("Tables");
    private final JButton btnMenu   = new JButton("Menu Editor");
    private final JButton btnServ   = new JButton("Server Management");
    private final JButton btnOrder  = new JButton("Order Management");
    private final JButton btnSales  = new JButton("Sales Report");

    /* lazy panels */
    private MenuEditorPanel        menuPane;
    private ServerManagementPanel  servPane;
    private OrderManagementPanel   orderPane;
    private SalesReportPanel       salesPane;

    public RestaurantGUI() {
        setTitle("Restaurant Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000,700);
        setLocationRelativeTo(null);

        /* toolbar */
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.add(btnTables);
        bar.add(btnMenu);
        bar.add(btnServ);
        bar.add(btnOrder);
        bar.add(btnSales);
        add(bar, BorderLayout.NORTH);

        /* actions */
        btnTables.addActionListener(e -> {
            view.displayTables(model.getTables().getTablesInfo());
            swapCenter(view.getRootPanel());
        });

        btnMenu.addActionListener(e -> {
            if (menuPane==null) menuPane = new MenuEditorPanel(controller);
            swapCenter(menuPane);
        });

        btnServ.addActionListener(e -> {
            if (servPane==null) servPane = new ServerManagementPanel(controller);
            swapCenter(servPane);
        });

        btnOrder.addActionListener(e -> {
            if (orderPane==null) orderPane = new OrderManagementPanel(controller);
            swapCenter(orderPane);
        });

        btnSales.addActionListener(e -> {
            if (salesPane==null) salesPane = new SalesReportPanel(controller);
            swapCenter(salesPane);
        });

        /* default */
        view.displayTables(model.getTables().getTablesInfo());
        swapCenter(view.getRootPanel());
        setVisible(true);
    }

    /* helper swaps centre comp */
    private void swapCenter(JComponent next){
        Container cp=getContentPane();
        BorderLayout bl=(BorderLayout)cp.getLayout();
        Component old=bl.getLayoutComponent(BorderLayout.CENTER);
        if(old!=null) cp.remove(old);
        cp.add(next,BorderLayout.CENTER);
        revalidate(); repaint();
    }

    public static void main(String[] a){
        SwingUtilities.invokeLater(RestaurantGUI::new);
    }
}
