package view;

import model.RestaurantController;
import model.Server;

import javax.swing.*;
import java.awt.*;

/** Simple CRUD for servers – no “back” button any more. */
public class ServerManagementPanel extends JPanel {

    private final RestaurantController controller;
    private final DefaultListModel<Server> listModel = new DefaultListModel<>();
    private final JList<Server> serverList           = new JList<>(listModel);
    private final JTextField nameField               = new JTextField(18);

    public ServerManagementPanel(RestaurantController c) {
        controller = c;
        buildUI();
        refresh();
    }

    /* ------------------------------------------------------------ */

    private void buildUI() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("Server Management", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        add(title, BorderLayout.NORTH);

        serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(serverList), BorderLayout.CENTER);

        JPanel edit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        edit.add(new JLabel("Name:"));
        edit.add(nameField);
        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            String n = nameField.getText().trim();
            if (!n.isEmpty()) {
                controller.handleAddServer(n);
                nameField.setText("");
                refresh();
            }
        });
        edit.add(addBtn);
        add(edit, BorderLayout.SOUTH);
    }

    public void refresh() {
        listModel.clear();
        controller.getModel().getServers().values().forEach(listModel::addElement);
    }
}
