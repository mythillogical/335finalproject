package view;

import javax.swing.*;
import model.*;
import java.awt.*;
import java.awt.event.*;
//import java.util.List;
import java.util.Map;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.DefaultListModel;

@SuppressWarnings("serial")
public class ServerManagementPanel extends JPanel {
	private RestaurantController controler;
    private JButton backButton;
    private JList<Server> serverList;
    private DefaultListModel<Server> serverListModel;
    private JTextField nameField;
    
    
    public ServerManagementPanel(RestaurantController controler) {
        this.controler = controler;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Create a panel for the top section with a back button and title
        JPanel topPanel = new JPanel(new BorderLayout());
        backButton = new JButton("Back to Main Menu");
        JLabel titleLabel = new JLabel("Server Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Create the main content panel with server list and controls
        JPanel contentPanel = new JPanel(new BorderLayout());
        
        // Server list with scroll pane
        serverListModel = new DefaultListModel<>();
        for (Map.Entry<String, Server> entry : 
        	this.controler.getModel().getServers().entrySet()) {
        	
            serverListModel.addElement(entry.getValue());
        }
        serverList = new JList<>(serverListModel);
        JScrollPane scrollPane = new JScrollPane(serverList);
        
        // Panel for server details and input fields
        JPanel detailsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        nameField = new JTextField(20);
        
        detailsPanel.add(new JLabel("Server Name:"));
        detailsPanel.add(nameField);
        
        // Button panel for actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add Server");
        JButton deleteButton = new JButton("Delete Server");
        
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        
        // Add components to the content panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(detailsPanel, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(inputPanel, BorderLayout.SOUTH);
        
        // Add all components to the server management panel
        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        
        // Set up action listeners for server management buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addServer();
            }
        });
        
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteServer();
            }
        });
        
        // Add list selection listener to update fields when a server is selected
        serverList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Server selectedServer = serverList.getSelectedValue();
                if (selectedServer != null) {
                    nameField.setText(selectedServer.getName());
                }
            }
        });
    }
    
    public JButton getBackButton() {
        return backButton;
    }
    
    private void addServer() {
        String name = nameField.getText().trim();
        
        if (!name.isEmpty()) {
            // Assuming the Server constructor can work with just a name
            Server newServer = new Server(name);
            this.controler.handleAddServer(name);
            serverListModel.addElement(newServer);
            nameField.setText("");
            JOptionPane.showMessageDialog(this, 
                    "Server added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Please enter a server name.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteServer() {
        Server selectedServer = serverList.getSelectedValue();
        if (selectedServer != null) {
            int choice = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this server?", "Confirm Deletion", 
                    JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                this.controler.handleRemoveServer(selectedServer.getName());
                serverListModel.removeElement(selectedServer);
                nameField.setText("");
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Please select a server to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}