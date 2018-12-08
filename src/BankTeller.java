import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.RandomStringUtils;

import Accounts.Account;
import Accounts.NonPocketAccount;
import Accounts.PocketAccount;
import Customers.Customer;
import Verifications.Verification;
import CurrDate.CurrDate;
import Customers.Customer;
import Customers.Transaction;
import InterestRates.InterestRates;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
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
	private JTextField txtLinked; 
	private JComboBox comboBoxAccountType;
    private JLabel label;
	private JTextField txtNewInterestRate;
	private JTextField txtTaxid;
	private JTextField txtName;
	private JTextField txtAddress;
	private JButton btnSetDate;
	
	// ====================================================================
	// Initialize DB
	// ====================================================================

	 // JDBC driver name and database URL
	   static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";  
	   static final String DB_URL = "jdbc:oracle:thin:@cloud-34-133.eci.ucsb.edu:1521:XE";

	   //  Database credentials
	   static final String USERNAME = "zakarybliss";
	   static final String PASSWORD = "password";
	   static Connection conn = null;
	   static Statement stmt = null; 
	   private JTable table_1;
	   private JTable table_2;

	// ====================================================================
	// Launch Application
	// ====================================================================
	public static void main(String[] args) {
		 try{
		      //STEP 2: Register JDBC driver
		      Class.forName(JDBC_DRIVER);

		      //STEP 3: Open a connection
		      System.out.println("Connecting to a selected database...");
		      conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		      stmt = conn.createStatement();
		      System.out.println("Connected database successfully..."); 
		 
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
		 } catch(SQLException se){
			    //Handle errors for JDBC
			    se.printStackTrace();
		   }catch(Exception e){
			    //Handle errors for Class.forName

		   }
//		   } finally{
//			      //finally block used to close resources
//			      try{
//			         if(stmt!=null)
//			            conn.close();
//			      }catch(SQLException se){
//			      }// do nothing
//			      try{
//			         if(conn!=null)
//			            conn.close();
//			      }catch(SQLException se){
//			         se.printStackTrace();
//			      }//end finally try
//			   }//end try
//			   System.out.println("Goodbye!");
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
				
				 Verification verification = new Verification(conn);
					String accountID = txtAccountId.getText(); 
					NonPocketAccount npAccount = new NonPocketAccount(conn, accountID); 	
					if (verification.isNonPocketAccount(accountID) && 
							verification.accountOpen(accountID)) {
						
						System.out.println("verified");
						String amount = txtAmount.getText();
						
						if(npAccount.writeCheck(Double.parseDouble(amount))){
							JOptionPane.showMessageDialog(frame, "Transaction Successful."); 
							if(npAccount.getBalance() <= .019) {
								npAccount.closeAccount();
								JOptionPane.showMessageDialog(frame, "Account Closed."); 
							}
						} else {
							JOptionPane.showMessageDialog(frame, "Transaction Failed.");;
						}
					}
					else {
						JOptionPane.showMessageDialog(frame, "Action Not Allowed."); 
					}
			}
		});
			
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(404, 182, 469, 322);
		frame.getContentPane().add(scrollPane);
		
		// ====================================================================
		// List Closed Accounts
		// ====================================================================
		btnListClosedAccounts = new JButton("List Closed Accounts");
		btnListClosedAccounts.setBounds(567, 63, 177, 29);
		frame.getContentPane().add(btnListClosedAccounts);
		
		btnListClosedAccounts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				ArrayList<String> closedAccounts = new ArrayList<String>(); 
 				
 				DefaultTableModel model = new DefaultTableModel(); 
 				JTable table = new JTable(model); 

 				model.addColumn("Closed Accounts"); 
				
				try {
					String selClosed = "SELECT aid FROM accounts WHERE status = 'N'"; 
					
					ResultSet closedRs = stmt.executeQuery(selClosed); 
					
			    	while (closedRs.next()) {
				    	  closedAccounts.add(closedRs.getString("aid"));
				    }
			    	
			    	closedRs.close();
			    	
					if(!closedAccounts.isEmpty()){
		 				for(String line : closedAccounts) {
		 					System.out.println(line);
		 					Object[] temp = {line};
		 					model.addRow(temp);
		 				}
		 
						scrollPane.setViewportView(table);
					} else {
						System.out.println("No closed accounts");
					}
			    	
				} catch(SQLException se){
				      //Handle errors for JDBC
				      se.printStackTrace();
				}catch(Exception e){
				      //Handle errors for Class.forName
				      e.printStackTrace();
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
				ArrayList<String> dter = Customer.getDTER();
 				
 				DefaultTableModel model = new DefaultTableModel(); 
 				JTable table = new JTable(model); 

 				model.addColumn("DTER"); 

 				for(String line : dter) {
 					System.out.println(line);
 					Object[] temp = {line};
 					model.addRow(temp);
 				}
 
				scrollPane.setViewportView(table);
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
 				ArrayList<String> monthlyStatement = Customer.getMonthlyStatement(taxId);
 				
 				DefaultTableModel model = new DefaultTableModel(); 
 				JTable table = new JTable(model); 

 				model.addColumn("Monthly Statement"); 
 				
 				for(String line : monthlyStatement) {
 					System.out.println(line);
 					Object[] temp = {line};
 					model.addRow(temp);
 				}
 
				scrollPane.setViewportView(table);
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
				ArrayList<String> accounts = Customer.getCustomerReport(taxId);

 				DefaultTableModel model = new DefaultTableModel(); 
 				JTable table = new JTable(model); 

 				model.addColumn("Customer Report"); 
				
				if(accounts != null){
					System.out.println("CUSTOMER REPORTING...");
	 				for(String line : accounts) {
	 					System.out.println(line);
	 					Object[] temp = {line};
	 					model.addRow(temp);
	 				}
	 
					scrollPane.setViewportView(table);
				} else {
					System.out.println("Customer Report is null");
				}
			}
		});
		
		// ====================================================================
		// Add Interest
		// ====================================================================
		btnAddInterest = new JButton("Add Interest");
		btnAddInterest.setBounds(22, 121, 117, 29);
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
		btnCreateAccount.setBounds(39, 400, 148, 29);
		frame.getContentPane().add(btnCreateAccount);
		
		String [] type = {"Intrest-Checking", "Student-Checking", "Savings", "Pocket"}; 
		comboBoxAccountType = new JComboBox(type);
		comboBoxAccountType.setBounds(223, 400, 52, 27);
		frame.getContentPane().add(comboBoxAccountType);
		
		txtBankName = new JTextField();
		txtBankName.setText("Bank Name");
		txtBankName.setBounds(224, 440, 130, 26);
		frame.getContentPane().add(txtBankName);
		txtBankName.setColumns(10);
		
		txtInitialBalance = new JTextField();
		txtInitialBalance.setText("Initial Balance");
		txtInitialBalance.setBounds(224, 480, 130, 26);
		frame.getContentPane().add(txtInitialBalance);
		txtInitialBalance.setColumns(10);
		
		txtOwners = new JTextField();
		txtOwners.setText("Owners");
		txtOwners.setBounds(224, 520, 130, 26);
		frame.getContentPane().add(txtOwners);
		txtOwners.setColumns(10);
		
		txtLinked = new JTextField();
		txtLinked.setText("Linked Account");
		txtLinked.setBounds(224, 560, 130, 26);
		frame.getContentPane().add(txtLinked);
		txtLinked.setColumns(10);
		btnCreateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String accountType = (String)comboBoxAccountType.getSelectedItem();
				String bankName = txtBankName.getText();
				String initialBalance = txtInitialBalance.getText();
				String owners = txtOwners.getText();
				String linked = txtLinked.getText(); 
				List<String> ownersList = Arrays.asList(owners.split(","));
				
				if(Integer.parseInt(initialBalance) > 0) {
				try {
					stmt = conn.createStatement();
					String accountID = RandomStringUtils.randomAlphanumeric(16);
					 
					String makeAcct = "INSERT INTO accounts(aid,balance,status) values "
							+ "('" + accountID + "','" + initialBalance + "','Y')"; 
					ResultSet acctRs = stmt.executeQuery(makeAcct); 		    	
			    	acctRs.close();
			    	
			    	if(!accountType.equals("Pocket")) {
			    		String typeChar = ""; 
			    		if(accountType.equals("Intrest-Checking")) {
			    			typeChar = "I"; 
			    		} else if (accountType.equals("Student-Checking")) {
			    			typeChar = "C"; 
			    		}
			    		else {
			    			typeChar = "S"; 
			    		}
						String setType = "INSERT INTO Non_pkt_accounts(aid,type) values "
								+ "('" + accountID + "','" + typeChar + "')"; 
						ResultSet typeRs = stmt.executeQuery(setType); 		    	
				    	acctRs.close();			    	
			    	} else {
						String setType = "INSERT INTO Pkt_accounts(aid) values "
								+ "('" + accountID + "')"; 
						ResultSet typeRs = stmt.executeQuery(setType); 		    	
				    	typeRs.close();
				    	
				    	String linkAcct = "INSERT INTO Linked_to (pid, lid) VALUES"
				    			+ "('" + accountID + "','" + linked + "')"; 
						ResultSet linkRs = stmt.executeQuery(linkAcct); 		    	
				    	linkRs.close();
			    	}
			    	
		    		//Create Owner Group 
			    	String ownerGroupID = RandomStringUtils.randomAlphanumeric(16);
		    		String makeOG = "INSERT INTO Owner_groups(oid) values ('" + ownerGroupID + "')";
					ResultSet makeRs = stmt.executeQuery(makeOG); 		    	
			    	makeRs.close();
			    	
		    		String setAcct = "INSERT INTO Owns(aid, oid) values ('" + accountID + "','" + ownerGroupID + "')"; 
					ResultSet acctGroupRs = stmt.executeQuery(setAcct); 		    	
			    	acctGroupRs.close(); 
			    	
			    	
			    	Verification verify = new Verification(conn); 
			    
			    	if(!verify.customerExists(ownersList.get(0))) {
			    		String[] args = {ownersList.get(0)}; 
			    		CustomerCreation.main(args);  
			    	}
			    	
		    		String setPrimOwner = "INSERT INTO Prim_Owns(taxid, oid) values ('" + ownersList.get(0) + "','" + ownerGroupID + "')";  
					ResultSet ownerPrimRs= stmt.executeQuery(setPrimOwner); 
			    	ownerPrimRs.close();
			    	
			    	System.out.println(ownersList.size());
		
			    	
			    	for(int i = 1; i < ownersList.size(); i++) {
			    		String o = ownersList.get(i); 
				    	if(!verify.customerExists(o)) {
				    		String[] arg = {o}; 
				    		CustomerCreation.main(arg);  
				    	}    		
			    		String secOwner = "INSERT INTO Sec_Owns(taxid, oid) values ('" + o + "','" + ownerGroupID + "')";
						ResultSet ownerRs= stmt.executeQuery(secOwner); 		    	
				    	ownerRs.close(); 
			    	}					
					} catch(SQLException se){
					      //Handle errors for JDBC
					      se.printStackTrace();
					}catch(Exception e){
					      //Handle errors for Class.forName
					      e.printStackTrace();
					}
			}
			}
		});
		
		// ====================================================================
		// Delete Closed Accounts and Customers
		// ====================================================================
		btnDeleteClosedAccountsAndCustomers = new JButton("Delete Closed Accounts and Customers");
		btnDeleteClosedAccountsAndCustomers.setBounds(17, 203, 302, 29);
		frame.getContentPane().add(btnDeleteClosedAccountsAndCustomers);
		
		btnDeleteClosedAccountsAndCustomers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
                    
					
					ArrayList<Customer> customers = Customer.getAllCustomers(); 
					
					for(Customer c : customers) {
						String taxID = c.getTaxID(); 
						if(c.getAllAssocAccounts().isEmpty()) {
							String delCust = "Delete customers where taxID = '" + taxID + "'";
							ResultSet delCustRs = stmt.executeQuery(delCust);
							delCustRs.close();
						}
					}
					
					System.out.println("customers deleted");
				} catch(SQLException se){
				    //Handle errors for JDBC
				    se.printStackTrace();
				 
				}catch(Exception e){
				    //Handle errors for Class.forName
				    e.printStackTrace();
				}
			}
		});
    
		// ====================================================================
		// Delete Transactions
		// ====================================================================
		btnDeleteTransactions = new JButton("Delete Transactions");
		btnDeleteTransactions.setBounds(17, 162, 202, 29);
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
		comboBoxBankAccountType.setBounds(165, 285, 189, 27);
		frame.getContentPane().add(comboBoxBankAccountType);
		
		txtNewInterestRate = new JTextField();
		txtNewInterestRate.setText("New Interest Rate");
		txtNewInterestRate.setBounds(23, 284, 130, 26);
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
		btnChangeInterestRate.setBounds(83, 322, 165, 29);
		frame.getContentPane().add(btnChangeInterestRate);
		
		// ====================================================================
		// Set Date
		// ====================================================================
		
		CurrDate.checkIfLastDayOfMonth(CurrDate.getCurrentDate());

		JDateChooser dateChooser = new JDateChooser(CurrDate.getCurrentDate());
		dateChooser.setBounds(34, 640, 119, 26);
		frame.getContentPane().add(dateChooser);
		
		btnSetDate = new JButton("Set Date");
		btnSetDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Date newDate = dateChooser.getDate();
				
				if(CurrDate.setCurrentDate(convertUtilToSql(newDate))) {
					CurrDate.checkIfLastDayOfMonth(convertUtilToSql(newDate));
				} else {
					// on fail
				}
				
			}
		});
		btnSetDate.setBounds(165, 640, 117, 29);
		frame.getContentPane().add(btnSetDate);		
	}
	
	private java.sql.Date convertUtilToSql(java.util.Date uDate) {
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}
}
