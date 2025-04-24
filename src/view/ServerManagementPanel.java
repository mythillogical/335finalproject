package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

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


/* crud panel for servers */
public class ServerManagementPanel extends JPanel
        implements RestaurantModel.ModelListener {

    private final RestaurantController controller;
    private final DefaultListModel<Server> listModel = new DefaultListModel<>();
    private final JList<Server> serverList = new JList<>(listModel);
    private final JTextField nameField = new JTextField(18);

    public ServerManagementPanel(RestaurantController c){
        controller=c;
        buildUi();
        refresh();
        controller.getModel().addListener(this);
    }
    @Override public void modelChanged(){ refresh(); }

    private void buildUi(){
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(new JLabel("Server Management",SwingConstants.CENTER),BorderLayout.NORTH);
        add(new JScrollPane(serverList),BorderLayout.CENTER);

        JPanel south=new JPanel(new FlowLayout(FlowLayout.LEFT));
        south.add(new JLabel("Name:")); south.add(nameField);

        JButton add=new JButton("Add"); south.add(add);
        add.addActionListener(e->{
            String n=nameField.getText().trim();
            if(!n.isEmpty()){ controller.handleAddServer(n); nameField.setText(""); }

        });

        JButton rem=new JButton("Remove Selected"); south.add(rem);
        rem.addActionListener(e->{
            Server s=serverList.getSelectedValue();
            if(s!=null) controller.handleRemoveServer(s.getName());
        });
        add(south,BorderLayout.SOUTH);
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
        	if (!this.controler.checkAtiveServer(selectedServer.getName())) {
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
                        "This server has active table", "Selection Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Please select a server to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
        }

    private void refresh(){
        listModel.clear();
        controller.getModel().getServers().values().forEach(listModel::addElement);

    }
}
