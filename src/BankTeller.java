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
							System.out.println("success");
							if(npAccount.getBalance() <= .019) {
								npAccount.closeAccount();
								System.out.println("Account Closed");
							}
						} else {
							System.out.println("failure");
						}
					}
					else {
						System.out.println("Not allowed");
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
				Vector<String> closedAccounts = new Vector<String>(); 
				try {
					String selClosed = "SELECT aid FROM accounts WHERE status = 'N'"; 
					
					System.out.println(selClosed);
					ResultSet closedRs = stmt.executeQuery(selClosed); 
					
			    	while (closedRs.next()) {
				    	  closedAccounts.add(closedRs.getString("aid"));
				    }
			    	
			    	closedRs.close();
			    	
				} catch(SQLException se){
				      //Handle errors for JDBC
				      se.printStackTrace();
				}catch(Exception e){
				      //Handle errors for Class.forName
				      e.printStackTrace();
				}
				
				if(!closedAccounts.isEmpty()){
//					Display Data
//					model.addRow(closedAccounts);
//					table.setModel(model);
				} else {
					System.out.println("No closed accounts");
					
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
		
//		// ====================================================================
//		// Generate Monthly Statement
//		// ====================================================================
//		btnGenerateMonthlyStatement = new JButton("Generate Monthly Statement");
//		btnGenerateMonthlyStatement.setBounds(375, 22, 222, 29);
//		frame.getContentPane().add(btnGenerateMonthlyStatement);
//		
		txtCustomerId = new JTextField();
		txtCustomerId.setText("Customer Id");
		txtCustomerId.setBounds(743, 22, 130, 26);
		frame.getContentPane().add(txtCustomerId);
		txtCustomerId.setColumns(10);
		
// 		btnGenerateMonthlyStatement.addActionListener(new ActionListener() {
// 			public void actionPerformed(ActionEvent arg0) {
// 				String taxId = txtCustomerId.getText();

// 				DefaultTableModel model = (DefaultTableModel) table.getModel();
// 				model.setRowCount(0);
				
// 				ArrayList<String> monthlyStatement = Customer.getMonthlyStatement(taxId);
// 				if(monthlyStatement != null){
// 					model.addRow(new Vector<String>(monthlyStatement));
// 					table.setModel(model); 
// 				} else {
// 					System.out.println("GetMonthlyStatement is null");
// 				}
// 			}
// 		});
		
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
//		btnCreateAccount = new JButton("Create Account");
//		btnCreateAccount.setBounds(39, 116, 148, 29);
//		frame.getContentPane().add(btnCreateAccount);
//		
//		String [] type = {"Checking", "Savings", "Pocket"}; 
//		comboBoxAccountType = new JComboBox(type);
//		comboBoxAccountType.setBounds(223, 117, 52, 27);
//		frame.getContentPane().add(comboBoxAccountType);
//		
//		txtBankName = new JTextField();
//		txtBankName.setText("Bank Name");
//		txtBankName.setBounds(224, 157, 130, 26);
//		frame.getContentPane().add(txtBankName);
//		txtBankName.setColumns(10);
//		
//		txtInitialBalance = new JTextField();
//		txtInitialBalance.setText("Initial Balance");
//		txtInitialBalance.setBounds(224, 195, 130, 26);
//		frame.getContentPane().add(txtInitialBalance);
//		txtInitialBalance.setColumns(10);
//		
//		txtOwners = new JTextField();
//		txtOwners.setText("Owners");
//		txtOwners.setBounds(224, 233, 130, 26);
//		frame.getContentPane().add(txtOwners);
//		txtOwners.setColumns(10);
//		
//		txtLinked = new JTextField();
//		txtLinked.setText("Linked Account");
//		txtLinked.setBounds(224, 273, 130, 26);
//		frame.getContentPane().add(txtLinked);
//		txtLinked.setColumns(10);
//		btnCreateAccount.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				String accountType = (String)comboBoxAccountType.getSelectedItem();
//				String bankName = txtBankName.getText();
//				String initialBalance = txtInitialBalance.getText();
//				String owners = txtOwners.getText();
//				String linked = txtLinked.getText(); 
//				List<String> ownersList = Arrays.asList(owners.split(","));
//				
//				if(Integer.parseInt(initialBalance) > 0) {
//				try {
//					stmt = conn.createStatement();
					
//					String makeAcct = "INSERT INTO accounts(aid,balance,status) values "
//							+ "('test','" + initialBalance + "','O')"; 
//					ResultSet acctRs = stmt.executeQuery(makeAcct); 		    	
//			    	acctRs.close();
			    	
//			    	if(!accountType.equals("Pocket")) {
//			    		String typeChar = ""; 
//			    		if(accountType.equals("Checking")) {
//			    			typeChar = "C"; 
//			    		} else {
//			    			typeChar = "S"; 
//			    		}
//						String setType = "INSERT INTO Non_pkt_accounts(aid,type) values "
//								+ "('test','" + typeChar + "')"; 
//						ResultSet typeRs = stmt.executeQuery(setType); 		    	
//				    	acctRs.close();			    	
//			    	} else {
//						String setType = "INSERT INTO Pkt_accounts(aid) values "
//								+ "('test')"; 
//						ResultSet typeRs = stmt.executeQuery(setType); 		    	
//				    	typeRs.close();
//				    	
//				    	String linkAcct = "INSERT INTO Linked_to (pid, lid) VALUES"
//				    			+ " ('test','" + linked + "')"; 
//						ResultSet linkRs = stmt.executeQuery(linkAcct); 		    	
//				    	linkRs.close();
//			    	}
//			    	
//		    		//Create Owner Group 
//		    		String makeOG = "INSERT INTO Owner_groups(oid) values ('replace')";
//					ResultSet makeRs = stmt.executeQuery(makeOG); 		    	
//			    	makeRs.close();
//			    	
//		    		String setAcct = "INSERT INTO Owns(aid, oid) values ('test', 'replace')"; 
//					ResultSet acctGroupRs = stmt.executeQuery(setAcct); 		    	
//			    	acctGroupRs.close(); 
			    	
//			    	
//		    		String setPrimOwner = "INSERT INTO Prim_Owns(taxid, oid) values ('" + ownersList.get(0) +"', 'replace')"; 
//					ResultSet ownerPrimRs= stmt.executeQuery(setPrimOwner); 
//			    	ownerPrimRs.close();
//			    	
//			    	ownersList.remove(0);
//			    	
//			    	for(String o : ownersList) {
//			    		String secOwner = "INSERT INTO Sec_Owns(taxid, oid) values ('" + o +"', 'replace')"; 
//						ResultSet ownerRs= stmt.executeQuery(secOwner); 		    	
//				    	ownerRs.close(); 
//			    	}
//					
//					} catch(SQLException se){
//					      //Handle errors for JDBC
//					      se.printStackTrace();
//					}catch(Exception e){
//					      //Handle errors for Class.forName
//					      e.printStackTrace();
//					}
//			}
//			}
//		});
//		
		// ====================================================================
		// Delete Closed Accounts and Customers
		// ====================================================================
		btnDeleteClosedAccountsAndCustomers = new JButton("Delete Closed Accounts and Customers");
		btnDeleteClosedAccountsAndCustomers.setBounds(0, 394, 302, 29);
		frame.getContentPane().add(btnDeleteClosedAccountsAndCustomers);
		
		btnDeleteClosedAccountsAndCustomers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
//					String del = "Delete from accounts where status = 'N'";
//					ResultSet delRs = stmt.executeQuery(del);
					
//					System.out.println(del);
					
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
