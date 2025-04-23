package view;

import model.*;

import javax.swing.*;
import java.awt.*;

/**
 * Top-level application frame.  Presents a toolbar with navigation
 * buttons and lets specialised panels take over the centre when the
 * user selects a task.
 */
public class RestaurantGUI extends JFrame {

    /* ------------------------------------------------------------------
       BACK-END MVC INSTANCES
       ------------------------------------------------------------------ */
    private final RestaurantModel      model       = new RestaurantModel();
    private final RestaurantView       view        = new RestaurantView(this);
    private final RestaurantController controller  = new RestaurantController(model, view);

    /* ------------------------------------------------------------------
       NAVIGATION BUTTONS
       ------------------------------------------------------------------ */
    private final JButton btnServers = new JButton("Server Management");
    private final JButton btnOrders  = new JButton("Order Management");
    private final JButton btnSales   = new JButton("Sales Report");

    /* lazily created panels */
    private ServerManagementPanel serverPane;
    private OrderManagementPanel  orderPane;
    private SalesReportPanel      salesPane;

    public RestaurantGUI() {
        /* Try Aqua for an “Apple” look; ignore if not on macOS */
        try { UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel"); }
        catch (Exception ignored) { UIManager.put("swing.boldMetal", Boolean.FALSE); }
        SwingUtilities.updateComponentTreeUI(this);

        setTitle("Restaurant Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        /* toolbar ------------------------------------------------------- */
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.add(btnServers);
        bar.add(btnOrders);
        bar.add(btnSales);
        getContentPane().add(bar, BorderLayout.NORTH);

        /* button wiring ------------------------------------------------- */
        btnServers.addActionListener(e -> {
            if (serverPane == null)
                serverPane = new ServerManagementPanel(controller, this);
            swapCenter(serverPane);
        });

        btnOrders.addActionListener(e -> {
            if (orderPane == null)
                orderPane = new OrderManagementPanel(controller);
            swapCenter(orderPane);
        });

        btnSales.addActionListener(e -> {
            if (salesPane == null)
                salesPane = new SalesReportPanel(controller);
            salesPane.refresh();
            swapCenter(salesPane);
        });

        /* initial screen: table overview ------------------------------- */
        showMainPanel();
        setVisible(true);
    }

    /** Called by child panels to return to the overview. */
    public void showMainPanel() {
        view.displayTables(model.getTables().getTablesInfo());
        swapCenter(view.getRootPanel());
    }

    /* ------------------------------------------------------------------
       CENTRE-PANEL SWAPPER
       ------------------------------------------------------------------ */
    private void swapCenter(JComponent next) {
        Container cp = getContentPane();
        BorderLayout bl = (BorderLayout) cp.getLayout();

        /* remove whatever is currently in the CENTER (if any) */
        Component old = bl.getLayoutComponent(BorderLayout.CENTER);
        if (old != null) cp.remove(old);

        /* add new component and refresh */
        cp.add(next, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
