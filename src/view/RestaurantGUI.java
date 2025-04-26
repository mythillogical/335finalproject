package view;

import controller.RestaurantController;
import model.*;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window for the Restaurant Management System.
 * 
 * Sets up the overall layout, navigation toolbar, and manages switching between panels.
 * 
 * Links the model, view, and controller, and starts the program interface.
 * 
 * Author: Michael B, Michael D, Asif R, Mohammed A
 * Course: CSc 335 - Object-Oriented Programming and Design
 * Semester: Spring 2025
 */
public class RestaurantGUI extends JFrame {

    /* mvc */
    private final RestaurantModel  model;
    private final RestaurantView   view;
    private final RestaurantController controller;

    /* toolbar buttons */
    private final JButton btnTables  = new JButton("Tables");
    private final JButton btnMenu    = new JButton("Menu Editor");
    private final JButton btnServers = new JButton("Server Management");
    private final JButton btnOrders  = new JButton("Order Management");
    private final JButton btnSales   = new JButton("Sales Report");

    /* lazy panels */
    private MenuEditorPanel        menuPane;
    private ServerManagementPanel  serverPane;
    private OrderManagementPanel   orderPane;
    private SalesReportPanel       salesPane;

    public RestaurantGUI() {

        setTitle("Restaurant Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Initialize model and load persistent state
        model = new RestaurantModel();
        model.loadServers();
        model.loadClosedBills();

        view = new RestaurantView(this);
        controller = new RestaurantController(model, view);

        // Add shutdown hook to save state on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            model.saveServers();
            model.saveClosedBills();
        }));
        
        /* toolbar */
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.add(btnTables);
        bar.add(btnMenu);
        bar.add(btnServers);
        bar.add(btnOrders);
        bar.add(btnSales);
        add(bar, BorderLayout.NORTH);

        /* button actions */
        btnTables.addActionListener(e -> {
            view.displayTables(model.getTables().getTablesInfo());
            swapCenter(view.getRootPanel());
        });

        btnMenu.addActionListener(e -> {
            if (menuPane == null) menuPane = new MenuEditorPanel(controller);
            swapCenter(menuPane);
        });

        btnServers.addActionListener(e -> {
            if (serverPane == null) serverPane = new ServerManagementPanel(controller);
            swapCenter(serverPane);
        });

        btnOrders.addActionListener(e -> {
            if (orderPane == null) orderPane = new OrderManagementPanel(controller);
            swapCenter(orderPane);
        });

        btnSales.addActionListener(e -> {
            if (salesPane == null) salesPane = new SalesReportPanel(controller);
            swapCenter(salesPane);
        });

        /* default view */
        view.displayTables(model.getTables().getTablesInfo());
        swapCenter(view.getRootPanel());
        setVisible(true);
    }

    /* helper to swap centre component */
    private void swapCenter(JComponent next){
        Container cp = getContentPane();
        BorderLayout bl = (BorderLayout) cp.getLayout();
        Component old = bl.getLayoutComponent(BorderLayout.CENTER);
        if (old != null) cp.remove(old);
        cp.add(next, BorderLayout.CENTER);
        revalidate(); repaint();
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(RestaurantGUI::new);
    }
}
