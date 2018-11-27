import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class BankTeller {

	private JFrame frame;
	private JTextField txtAccountId;
	private JTextField txtCustomerId;
	private JTable table;
	private JButton btnNewButton;
	private JButton btnCustomerReport;
	private JButton btnAddInterest;
	private JButton btnCreateAccount, btnSubmitCheckTransaction;
	private JTextField textField_3;
	private JButton btnDeleteClosedAccountsAndCustomers, btnGenerateMonthlyStatement;
	private JButton btnDeleteTransactions, btnListClosedAccount;
	private JTable table_5;
	private JTextField textField;
	private JTextField txtAmount;
	private JTextField txtInterest;

	// ====================================================================
	// Launch Application
	// ====================================================================
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BankTeller window = new BankTeller();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// ====================================================================
	// Create Application
	// ====================================================================
	public BankTeller() {
		initialize();
	}

	// ====================================================================
	// Initialize Contents of Frame
	// ====================================================================
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 879, 451);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// ====================================================================
		// Enter Check Transaction
		// ====================================================================
		btnSubmitCheckTransaction = new JButton("Submit Check Transaction");
		btnSubmitCheckTransaction.setBounds(17, 22, 202, 29);
		frame.getContentPane().add(btnSubmitCheckTransaction);
		
		txtAccountId = new JTextField();
		txtAccountId.setText("Account Id");
		txtAccountId.setBounds(224, 22, 130, 26);
		frame.getContentPane().add(txtAccountId);
		txtAccountId.setColumns(10);
		
		txtAmount = new JTextField();
		txtAmount.setText("Total Amount");
		txtAmount.setBounds(224, 60, 130, 26);
		frame.getContentPane().add(txtAmount);
		txtAmount.setColumns(10);
		
		String originAccount = txtAccountId.getText();
		String totalAmount = txtAmount.getText();
		btnSubmitCheckTransaction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Account.submitCheckTransaction(originAccount, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
		
		
		// ====================================================================
		// Generate Monthly Statement
		// ====================================================================
		btnGenerateMonthlyStatement = new JButton("Generate Monthly Statement");
		btnGenerateMonthlyStatement.setBounds(475, 157, 222, 29);
		frame.getContentPane().add(btnGenerateMonthlyStatement);
		
		txtCustomerId = new JTextField();
		txtCustomerId.setText("Customer Id");
		txtCustomerId.setBounds(716, 22, 130, 26);
		frame.getContentPane().add(txtCustomerId);
		txtCustomerId.setColumns(10);
		
		// ====================================================================
		// List Closed Accounts
		// ====================================================================
		btnListClosedAccount = new JButton("List Closed Accounts");
		btnListClosedAccount.setBounds(537, 75, 177, 29);
		frame.getContentPane().add(btnListClosedAccount);
		
		// ====================================================================
		// Generate DTER
		// ====================================================================
		btnNewButton = new JButton("Generate DTER");
		btnNewButton.setBounds(565, 116, 117, 29);
		frame.getContentPane().add(btnNewButton);
		
		// ====================================================================
		// Customer Report
		// ====================================================================
		btnCustomerReport = new JButton("Customer Report");
		btnCustomerReport.setBounds(537, 22, 177, 29);
		frame.getContentPane().add(btnCustomerReport);
		
		
		table_5 = new JTable();
		table_5.setBounds(632, 261, 1, 1);
		frame.getContentPane().add(table_5);
		
		// ====================================================================
		// Add Interest
		// ====================================================================
		btnAddInterest = new JButton("Add Interest");
		btnAddInterest.setBounds(64, 261, 117, 29);
		frame.getContentPane().add(btnAddInterest);
		
		btnAddInterest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Account.deleteClosedAccountsAndCustomers()) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
		
		txtInterest = new JTextField();
		txtInterest.setText("Interest");
		txtInterest.setBounds(224, 261, 130, 26);
		frame.getContentPane().add(txtInterest);
		txtInterest.setColumns(10);
	    
		String interest = txtInterest.getText();
		btnAddInterest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Account.addInterest(interest)) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
		
		// ====================================================================
		// Create Account
		// ====================================================================
		btnCreateAccount = new JButton("Create Account");
		btnCreateAccount.setBounds(39, 116, 148, 29);
		frame.getContentPane().add(btnCreateAccount);
		
		textField_3 = new JTextField();
		textField_3.setBounds(224, 116, 130, 26);
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		textField = new JTextField();
		textField.setBounds(224, 157, 130, 26);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		// ====================================================================
		// Delete Closed Accounts and Customers
		// ====================================================================
		btnDeleteClosedAccountsAndCustomers = new JButton("Delete Closed Accounts and Customers");
		btnDeleteClosedAccountsAndCustomers.setBounds(0, 394, 302, 29);
		frame.getContentPane().add(btnDeleteClosedAccountsAndCustomers);
		
		btnDeleteClosedAccountsAndCustomers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Account.deleteClosedAccountsAndCustomers()) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
		
		// ====================================================================
		// Delete Transactions
		// ====================================================================
		btnDeleteTransactions = new JButton("Delete Transactions");
		btnDeleteTransactions.setBounds(39, 353, 202, 29);
		frame.getContentPane().add(btnDeleteTransactions);	
		
		btnDeleteTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Transaction.deleteTransactions()) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
	}
}
