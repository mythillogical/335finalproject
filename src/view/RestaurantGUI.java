package view;

import model.*;
import javax.swing.*;
import java.awt.*;

public class RestaurantGUI extends JFrame {

    // mvc components
    private final RestaurantModel model      = new RestaurantModel();
    private final RestaurantView  view       = new RestaurantView(this);
    private final RestaurantController controller = new RestaurantController(model, view);

    // navigation buttons
    private final JButton btnTables  = new JButton("tables");
    private final JButton btnServers = new JButton("server management");
    private final JButton btnOrders  = new JButton("order management");
    private final JButton btnSales   = new JButton("sales report");

    // lazy panels
    private OrderManagementPanel  orderPane;
    private ServerManagementPanel serverPane;
    private SalesReportPanel      salesPane;

    public RestaurantGUI() {
        setTitle("restaurant management system");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // top toolbar
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.add(btnTables);
        bar.add(btnServers);
        bar.add(btnOrders);
        bar.add(btnSales);
        add(bar, BorderLayout.NORTH);

        // show tables view
        btnTables.addActionListener(e -> {
            view.displayTables(model.getTables().getTablesInfo());
            swapCenter(view.getRootPanel());
        });

        // show server management
        btnServers.addActionListener(e -> {
            if (serverPane == null) serverPane = new ServerManagementPanel(controller);
            serverPane.refresh();
            swapCenter(serverPane);
        });

        // show order management
        btnOrders.addActionListener(e -> {
            if (orderPane == null) orderPane = new OrderManagementPanel(controller);
            orderPane.refreshServers();
            swapCenter(orderPane);
        });

        // show sales report
        btnSales.addActionListener(e -> {
            if (salesPane == null) salesPane = new SalesReportPanel(controller);
            salesPane.refresh();
            swapCenter(salesPane);
        });

        // default to tables overview
        view.displayTables(model.getTables().getTablesInfo());
        setVisible(true);
    }

    // replace center component
    private void swapCenter(JComponent next) {
        Container cp = getContentPane();
        BorderLayout bl = (BorderLayout) cp.getLayout();
        Component old = bl.getLayoutComponent(BorderLayout.CENTER);
        if (old != null) cp.remove(old);
        cp.add(next, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RestaurantGUI::new);
    }
}