package view;

import model.RestaurantController;
import model.Server;

import javax.swing.*;
import java.awt.*;

/**
 * CRUD panel for servers – add / rename / remove.  Relies on the
 * RestaurantController for back-end changes; updates its list each time
 * the model mutates.
 */
public class ServerManagementPanel extends JPanel {

    private final RestaurantController controller;
    private final RestaurantGUI host;

    private final DefaultListModel<Server> listModel = new DefaultListModel<>();
    private final JList<Server> serverList = new JList<>(listModel);
    private final JTextField nameField = new JTextField(18);

    public ServerManagementPanel(RestaurantController controller, RestaurantGUI host) {
        this.controller = controller;
        this.host = host;
        buildUI();
        refresh();
    }

    // ---------------------------------------------------------------------
    private void buildUI() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        /* top bar */
        JButton back = new JButton("← Back");
        back.addActionListener(e -> host.showMainPanel());
        JLabel title = new JLabel("Server Management", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        JPanel top = new JPanel(new BorderLayout());
        top.add(back, BorderLayout.WEST);
        top.add(title, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        /* centre list */
        serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(serverList), BorderLayout.CENTER);

        /* bottom editor */
        JPanel edit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        edit.add(new JLabel("Name:"));
        edit.add(nameField);
        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            String n = nameField.getText().trim();
            if (n.isEmpty()) return;
            controller.handleAddServer(n);
            nameField.setText("");
            refresh();
        });
        edit.add(addBtn);
        add(edit, BorderLayout.SOUTH);
    }

    /* pull fresh list from model */
    void refresh() {
        listModel.clear();
        listModel.addAll(controller.getModel().getServers().values());
    }
}
