package view;

//PaymentWindow.java
import javax.swing.*;

import model.Bill;
import model.Item;
import model.RestaurantController;

import java.awt.*;

public class PaymentWindow extends JFrame {
	OrderManagementPanel parent;
	private Bill bill;
	private RestaurantController controller;
	private JRadioButton splitEvenlyOption;
	private JRadioButton splitByGuestOption;
	private JLabel subtotalLabel;
	private JLabel taxLabel;
	private JLabel totalLabel;
	private JTextField tipAmountField;
	private JLabel tipPercentLabel;
	private JLabel finalTotalLabel;

	private static final double TAX_RATE = 0.08; // 8% tax

	public PaymentWindow(OrderManagementPanel parent, RestaurantController controller, Bill bill) {
		this.parent = parent;
		this.controller = controller;
		this.bill = bill;

		setTitle("Process Payment - Table " + bill.getTableNum());
		setSize(400, 400);
		setLocationRelativeTo(parent);

		initializeUI();
		updateTotals();
	}

	private void initializeUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// North Panel - Split options
		JPanel northPanel = new JPanel(new GridLayout(3, 1));
		northPanel.setBorder(BorderFactory.createTitledBorder("Payment Options"));

		JLabel splitLabel = new JLabel("How would you like to split the bill?");
		northPanel.add(splitLabel);

		splitEvenlyOption = new JRadioButton("Split evenly between all guests");
		splitEvenlyOption.setSelected(true);
		northPanel.add(splitEvenlyOption);

		splitByGuestOption = new JRadioButton("Split by individual guest orders");
		northPanel.add(splitByGuestOption);

		ButtonGroup splitGroup = new ButtonGroup();
		splitGroup.add(splitEvenlyOption);
		splitGroup.add(splitByGuestOption);

		mainPanel.add(northPanel, BorderLayout.NORTH);

		// Center Panel - Bill details
		JPanel centerPanel = new JPanel(new GridLayout(7, 2, 5, 5));
		centerPanel.setBorder(BorderFactory.createTitledBorder("Bill Details"));

		centerPanel.add(new JLabel("Subtotal:"));
		subtotalLabel = new JLabel("$0.00");
		centerPanel.add(subtotalLabel);

		centerPanel.add(new JLabel("Tax (" + (TAX_RATE * 100) + "%):"));
		taxLabel = new JLabel("$0.00");
		centerPanel.add(taxLabel);

		centerPanel.add(new JLabel("Total:"));
		totalLabel = new JLabel("$0.00");
		centerPanel.add(totalLabel);

		centerPanel.add(new JLabel("Tip Amount:"));
		tipAmountField = new JTextField("0.00");
		tipAmountField.addActionListener(e -> updateTipPercentage());
		centerPanel.add(tipAmountField);

		centerPanel.add(new JLabel("Tip Percentage:"));
		tipPercentLabel = new JLabel("0%");
		centerPanel.add(tipPercentLabel);

		centerPanel.add(new JLabel("Final Total:"));
		finalTotalLabel = new JLabel("$0.00");
		centerPanel.add(finalTotalLabel);

		mainPanel.add(centerPanel, BorderLayout.CENTER);

		// South Panel - Payment actions
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton processButton = new JButton("Process Payment");
		processButton.addActionListener(e -> processPayment());
		southPanel.add(processButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> dispose());
		southPanel.add(cancelButton);

		mainPanel.add(southPanel, BorderLayout.SOUTH);

		add(mainPanel);
	}

	private void updateTotals() {
		double subtotal = bill.getTotalCost();
		double tax = subtotal * TAX_RATE;
		double total = subtotal + tax;

		subtotalLabel.setText("$" + String.format("%.2f", subtotal));
		taxLabel.setText("$" + String.format("%.2f", tax));
		totalLabel.setText("$" + String.format("%.2f", total));

		updateTipPercentage();
	}

	private void updateTipPercentage() {
		try {
			double subtotal = bill.getTotalCost();
			double tipAmount = Double.parseDouble(tipAmountField.getText());
			double tipPercentage = (subtotal > 0) ? (tipAmount / subtotal) * 100 : 0;

			tipPercentLabel.setText(String.format("%.1f%%", tipPercentage));

			double tax = subtotal * TAX_RATE;
			finalTotalLabel.setText("$" + String.format("%.2f", subtotal + tax + tipAmount));

		} catch (NumberFormatException e) {
			tipPercentLabel.setText("Invalid");
		}
	}

	private void processPayment() {
	    try {
	        double tipAmount = Double.parseDouble(tipAmountField.getText());

	        if (tipAmount < 0) {
	            JOptionPane.showMessageDialog(this, "Tip amount cannot be negative.");
	            return;
	        }
	        
	        bill.addTips(tipAmount);

	        if (splitByGuestOption.isSelected()) {
	            // Show per-guest breakdown
	            StringBuilder breakdown = new StringBuilder("Payment by guest:\n\n");

	            // Calculate costs
	            double subtotal = bill.getTotalCost();
	            double tax = subtotal * TAX_RATE;
	            int numPeople = bill.getPeople();
	            
	            // Split evenly among all guests
	            double subtotalPerGuest = subtotal / numPeople;
	            double taxPerGuest = tax / numPeople;
	            double tipPerGuest = tipAmount / numPeople;
	            double totalPerGuest = subtotalPerGuest + taxPerGuest + tipPerGuest;

	            // Show breakdown for each guest
	            for (int i = 1; i <= numPeople; i++) {
	                breakdown.append("Guest ").append(i).append(":\n");
	                breakdown.append(" Subtotal: $").append(String.format("%.2f", subtotalPerGuest)).append("\n");
	                breakdown.append(" Tax: $").append(String.format("%.2f", taxPerGuest)).append("\n");
	                breakdown.append(" Tip: $").append(String.format("%.2f", tipPerGuest)).append("\n");
	                breakdown.append(" Total: $").append(String.format("%.2f", totalPerGuest)).append("\n\n");
	            }

	            JOptionPane.showMessageDialog(this, breakdown.toString(), "Payment Breakdown",
	                    JOptionPane.INFORMATION_MESSAGE);
	        }

	        // Close this dialog
	        bill.setPaid(true);
	        dispose();

	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(this, "Please enter a valid tip amount.");
	    }
	}
}
