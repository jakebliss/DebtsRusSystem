import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Accounts.Account;
import CurrDate.CurrDate;
import Customers.Customer;
import Customers.Transaction;
import InterestRates.InterestRates;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Button;
import com.toedter.calendar.JDateChooser;

public class BankTeller {

	private JFrame frame;
	private JTextField txtAccountId;
	private JTextField txtCustomerId;
	private JTable table;
	private JButton btnGenerateDTER;
	private JButton btnCustomerReport;
	private JButton btnAddInterest;
	private JButton btnCreateAccount, btnSubmitCheckTransaction;
	private JButton btnDeleteClosedAccountsAndCustomers, btnGenerateMonthlyStatement;
	private JButton btnDeleteTransactions, btnListClosedAccounts;
	private JTextField txtBankName;
	private JTextField txtAmount;
	private JTextField txtInitialBalance;
	private JTextField txtOwners;
	private JComboBox comboBoxAccountType;
	private JLabel label;
	private JTextField txtNewInterestRate;
	private JTextField txtTaxid;
	private JTextField txtName;
	private JTextField txtAddress;
	private JButton btnSetDate;

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
		frame.setBounds(100, 100, 879, 865);
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
		
		btnSubmitCheckTransaction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String originAccount = txtAccountId.getText();
				String totalAmount = txtAmount.getText();
				
				if(Account.checkTransaction(originAccount, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
		
		// Output Table for List Closed Accounts, Generate DTER, and Customer Report
		table = new JTable();
		table.setBounds(632, 261, 1, 1);
		frame.getContentPane().add(table);		
		// ====================================================================
		// List Closed Accounts
		// ====================================================================
		btnListClosedAccounts = new JButton("List Closed Accounts");
		btnListClosedAccounts.setBounds(567, 63, 177, 29);
		frame.getContentPane().add(btnListClosedAccounts);
		
		btnListClosedAccounts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.setRowCount(0);
		
				ArrayList<String> accounts = Account.listClosedAccounts();
				if(accounts != null){
					//TODO: populate table with accounts
				} else {
					//TODO: on failure
				}
			}
		});
		
		// ====================================================================
		// Generate DTER
		// ====================================================================
		btnGenerateDTER = new JButton("Generate DTER");
		btnGenerateDTER.setBounds(627, 104, 117, 29);
		frame.getContentPane().add(btnGenerateDTER);
		
		btnGenerateDTER.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.setRowCount(0);
				
				ArrayList<String> dter = Customer.getDTER();
				if(dter != null){
					model.addRow(new Vector<String>(dter));
					table.setModel(model);
				} else {
					System.out.println("GetMonthlyStatement is null");
				}
			}
		});
		
		// ====================================================================
		// Generate Monthly Statement
		// ====================================================================
		btnGenerateMonthlyStatement = new JButton("Generate Monthly Statement");
		btnGenerateMonthlyStatement.setBounds(375, 22, 222, 29);
		frame.getContentPane().add(btnGenerateMonthlyStatement);
		
		txtCustomerId = new JTextField();
		txtCustomerId.setText("Customer Id");
		txtCustomerId.setBounds(743, 22, 130, 26);
		frame.getContentPane().add(txtCustomerId);
		txtCustomerId.setColumns(10);
		
		btnGenerateMonthlyStatement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String taxId = txtCustomerId.getText();

				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.setRowCount(0);
				
				ArrayList<String> monthlyStatement = Customer.getMonthlyStatement(taxId);
				if(monthlyStatement != null){
					model.addRow(new Vector<String>(monthlyStatement));
					table.setModel(model); 
				} else {
					System.out.println("GetMonthlyStatement is null");
				}
			}
		});
		
		// ====================================================================
		// Customer Report
		// ====================================================================
		btnCustomerReport = new JButton("Customer Report");
		btnCustomerReport.setBounds(596, 22, 148, 29);
		frame.getContentPane().add(btnCustomerReport);
		
		btnCustomerReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String taxId = txtCustomerId.getText();

				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.setRowCount(0);
				
				ArrayList<String> accounts = Customer.getCustomerReport(taxId);
				if(accounts != null){
					//TODO: populate table with accounts
				} else {
					//TODO: on failure
				}
			}
		});
		
		// ====================================================================
		// Add Interest
		// ====================================================================
		btnAddInterest = new JButton("Add Interest");
		btnAddInterest.setBounds(82, 312, 117, 29);
		frame.getContentPane().add(btnAddInterest);
		
		btnAddInterest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Account.addInterest()) {
					// on success
				} else {
			    	JOptionPane.showMessageDialog(frame, "[Warning] Add interest only allowed Once a month.");
				}
			}
		});
		
		// ====================================================================
		// Create Account
		// ====================================================================
		btnCreateAccount = new JButton("Create Account");
		btnCreateAccount.setBounds(39, 116, 148, 29);
		frame.getContentPane().add(btnCreateAccount);
		
		comboBoxAccountType = new JComboBox();
		comboBoxAccountType.setBounds(223, 117, 52, 27);
		frame.getContentPane().add(comboBoxAccountType);
		
		txtBankName = new JTextField();
		txtBankName.setText("Bank Name");
		txtBankName.setBounds(224, 157, 130, 26);
		frame.getContentPane().add(txtBankName);
		txtBankName.setColumns(10);
		
		txtInitialBalance = new JTextField();
		txtInitialBalance.setText("Initial Balance");
		txtInitialBalance.setBounds(224, 187, 130, 26);
		frame.getContentPane().add(txtInitialBalance);
		txtInitialBalance.setColumns(10);
		
		txtOwners = new JTextField();
		txtOwners.setText("Owners");
		txtOwners.setBounds(224, 215, 130, 26);
		frame.getContentPane().add(txtOwners);
		txtOwners.setColumns(10);
		
		btnCreateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String accountType = (String)comboBoxAccountType.getSelectedItem();
				String bankName = txtBankName.getText();
				String initialBalance = txtInitialBalance.getText();
				String owners = txtOwners.getText();
				
				if(Account.create(accountType, bankName, initialBalance, owners)) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
		
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
		
		label = new JLabel("");
		label.setBounds(331, 477, 61, 16);
		frame.getContentPane().add(label);
		
		btnDeleteTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Transaction.deleteTransactions()) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
		// ====================================================================
		// Change Interest
		// ====================================================================
		String[] BankAccountTypes = {"Interest Checking", "Student Checking", "Saving", "Pocket"};
		JComboBox comboBoxBankAccountType = new JComboBox(BankAccountTypes);
		comboBoxBankAccountType.setBounds(165, 477, 52, 27);
		frame.getContentPane().add(comboBoxBankAccountType);
		
		txtNewInterestRate = new JTextField();
		txtNewInterestRate.setText("New Interest Rate");
		txtNewInterestRate.setBounds(23, 477, 130, 26);
		frame.getContentPane().add(txtNewInterestRate);
		txtNewInterestRate.setColumns(10);
		
		JButton btnChangeInterestRate = new JButton("Change Interest Rate");
		btnChangeInterestRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String bankAccountType = (String) comboBoxBankAccountType.getSelectedItem();
				String interestRate = txtNewInterestRate.getText();
				
				if(InterestRates.setInterestRate(bankAccountType, interestRate)) {
					// on success
				} else {
					// on fail
				}
				
			}
		});
		btnChangeInterestRate.setBounds(227, 477, 165, 29);
		frame.getContentPane().add(btnChangeInterestRate);
		

		
		// ====================================================================
		// Customer Creation
		// ====================================================================
		JLabel lblCustomerCreation = new JLabel("Customer Creation");
		lblCustomerCreation.setBounds(23, 557, 164, 16);
		frame.getContentPane().add(lblCustomerCreation);
		
		txtTaxid = new JTextField();
		txtTaxid.setText("taxID");
		txtTaxid.setBounds(39, 597, 130, 26);
		frame.getContentPane().add(txtTaxid);
		txtTaxid.setColumns(10);
		
		txtName = new JTextField();
		txtName.setText("Name");
		txtName.setBounds(39, 635, 130, 26);
		frame.getContentPane().add(txtName);
		txtName.setColumns(10);
		
		txtAddress = new JTextField();
		txtAddress.setText("Address");
		txtAddress.setBounds(39, 673, 130, 26);
		frame.getContentPane().add(txtAddress);
		txtAddress.setColumns(10);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String taxId = txtTaxid.getText();
				String name = txtName.getText();
				String address = txtAddress.getText();
				
				if(Customer.create(taxId, name, address)) {
					// on success
				} else {
					// on fail
				}
				
			}
		});
		btnCreate.setBounds(49, 711, 117, 29);
		frame.getContentPane().add(btnCreate);
		
		// ====================================================================
		// Set Date
		// ====================================================================
		Date currDate = CurrDate.getCurrentDate();
		CurrDate.checkIfLastDayOfMonth(currDate);

		JDateChooser dateChooser = new JDateChooser(currDate);
		dateChooser.setBounds(17, 799, 119, 26);
		frame.getContentPane().add(dateChooser);
		
		btnSetDate = new JButton("Set Date");
		btnSetDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Date newDate = dateChooser.getDate();
				
				if(CurrDate.setCurrentDate(newDate)) {
					CurrDate.checkIfLastDayOfMonth(newDate);
				} else {
					// on fail
				}
				
			}
		});
		btnSetDate.setBounds(148, 799, 117, 29);
		frame.getContentPane().add(btnSetDate);
		
	}
}
