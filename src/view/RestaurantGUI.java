package view;

import model.*;

import javax.swing.*;
import java.awt.*;

public class RestaurantGUI extends JFrame {

    /* -------------------------------------------------------------- */
    private final RestaurantModel model = new RestaurantModel();
    private final RestaurantView  view  = new RestaurantView(this);
    private final RestaurantController controller =
            new RestaurantController(model, view);

    /* navigation buttons */
    private final JButton btnTables  = new JButton("Tables");
    private final JButton btnServers = new JButton("Server Management");
    private final JButton btnOrders  = new JButton("Order Management");
    private final JButton btnSales   = new JButton("Sales Report");

    /* lazily created screens */
    private OrderManagementPanel  orderPane;
    private ServerManagementPanel serverPane;
    private SalesReportPanel      salesPane;

    /* -------------------------------------------------------------- */
    public RestaurantGUI() {
        setTitle("Restaurant Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        /* toolbar --------------------------------------------------- */
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.add(btnTables);
        bar.add(btnServers);
        bar.add(btnOrders);
        bar.add(btnSales);
        add(bar, BorderLayout.NORTH);

        /* buttons --------------------------------------------------- */
        btnTables.addActionListener(e -> {
            view.displayTables(model.getTables().getTablesInfo());
            swapCenter(view.getRootPanel());      // <── FIX
        });

        btnServers.addActionListener(e -> {
            if (serverPane == null)
                serverPane = new ServerManagementPanel(controller);
            serverPane.refresh();
            swapCenter(serverPane);
        });

        btnOrders.addActionListener(e -> {
            if (orderPane == null)
                orderPane = new OrderManagementPanel(controller);
            orderPane.refreshServers();
            swapCenter(orderPane);
        });

        btnSales.addActionListener(e -> {
            if (salesPane == null)
                salesPane = new SalesReportPanel(controller);
            salesPane.refresh();
            swapCenter(salesPane);
        });

        /* start on overview ---------------------------------------- */
        view.displayTables(model.getTables().getTablesInfo());
        setVisible(true);
    }

    /* helper: replace whatever is in BorderLayout.CENTER */
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
