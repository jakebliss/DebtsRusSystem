import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;

import Accounts.Account;
import Accounts.NonPocketAccount;
import Accounts.PocketAccount;
import Customers.Customer;
import Verifications.Verification;

public class Atm {

	private JFrame frame;
	private JTextField txtEnterAmount;
	private JTextField txtFriendId;
	private JTextField txtAccountId;
	private Customer mCustomer; 
	
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
					Atm window = new Atm(Integer.parseInt(args[0]), args[1]);
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
		    e.printStackTrace();
	   } 
	   //finally{
//		      //finally block used to close resources
//		      try{
//		         if(stmt!=null)
//		            conn.close();
//		      }catch(SQLException se){
//		      }// do nothing
//		      try{
//		         if(conn!=null)
//		            conn.close();
//		      }catch(SQLException se){
//		         se.printStackTrace();
//		      }//end finally try
//		   }//end try
//		   System.out.println("Goodbye!");
	}

	// ====================================================================
	// Create Application
	// ====================================================================
	public Atm(int pin, String taxID) {
		initialize(pin, taxID);
	}

	// ====================================================================
	// Initialize Contents of Frame
	// ====================================================================
	private void initialize(int pin, String taxID) {
		frame = new JFrame();
		frame.setBounds(100, 100, 492, 321);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		mCustomer = new Customer(stmt, conn, taxID, String.valueOf(pin)); 
				
		ArrayList<String> originAccountIDs = new ArrayList<String>();
		for(Account a : mCustomer.getAllAssocAccounts()) {
			if(!originAccountIDs.contains(a.getID())) {
				originAccountIDs.add(a.getID());
				System.out.println(a.getID());
			}
		}
		JComboBox originAccountComboBox = new JComboBox(originAccountIDs.toArray());

		originAccountComboBox.setBounds(386, 20, 52, 27);
		frame.getContentPane().add(originAccountComboBox);
		
		txtEnterAmount = new JTextField();
		txtEnterAmount.setText("Enter Amount");
		txtEnterAmount.setBounds(148, 19, 130, 26);
		frame.getContentPane().add(txtEnterAmount);
		txtEnterAmount.setColumns(10);
		
		txtFriendId = new JTextField();
		txtFriendId.setText("Friend ID");
		txtFriendId.setBounds(148, 241, 130, 26);
		frame.getContentPane().add(txtFriendId);
		txtFriendId.setColumns(10);
		
		txtAccountId = new JTextField();
		txtAccountId.setText("Account ID");
		txtAccountId.setBounds(148, 165, 130, 26);
		frame.getContentPane().add(txtAccountId);
		txtAccountId.setColumns(10);
		
		// ====================================================================
		// Deposit
		// ====================================================================
		JButton btnDeposit = new JButton("Deposit");
		btnDeposit.setBounds(6, 19, 117, 29);
		frame.getContentPane().add(btnDeposit);
		
		btnDeposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		        Verification verification = new Verification(conn); 
				String accountID = (String) originAccountComboBox.getSelectedItem(); 
				NonPocketAccount npAccount = new NonPocketAccount(conn, accountID); 	
				if (verification.isNonPocketAccount(accountID) && 
						verification.accountOpen(accountID)) {
					
					System.out.println("verified");
					String amount = txtEnterAmount.getText();
					
					if(npAccount.deposit(Double.parseDouble(amount))) {
						JOptionPane.showMessageDialog(frame, "Transaction Successful."); 
					} else {
						JOptionPane.showMessageDialog(frame, "Transaction Failed."); 
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "Action Not Allowed."); 
				}
			}
		});
		
		// ====================================================================
		// Top-Up
		// ====================================================================
		JButton btnTopup = new JButton("Top-Up");
		btnTopup.setBounds(6, 99, 117, 29);
		frame.getContentPane().add(btnTopup);
		
		btnTopup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		        Verification verification = new Verification(conn);
				String accountID = (String) originAccountComboBox.getSelectedItem(); 
				PocketAccount npAccount = new PocketAccount(conn, accountID); 	
				if (verification.isPocketAccount(accountID) && 
						verification.accountOpen(accountID)) {
					
					System.out.println("verified");
					String amount = txtEnterAmount.getText();
					
					if(npAccount.topUp(Double.parseDouble(amount))) {
						JOptionPane.showMessageDialog(frame, "Transaction Successful."); 
					} else {
						JOptionPane.showMessageDialog(frame, "Transaction Failed."); 
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "Action Not Allowed."); 
				}
			}
		});
		
		// ====================================================================
		// Withdrawal
		// ====================================================================
		JButton btnWithdrawal = new JButton("Withdrawal");
		btnWithdrawal.setBounds(6, 46, 117, 29);
		frame.getContentPane().add(btnWithdrawal);
		
		btnWithdrawal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Verification verification = new Verification(conn); 
				String accountID = (String) originAccountComboBox.getSelectedItem(); 
				NonPocketAccount npAccount = new NonPocketAccount(conn, accountID); 	
				if (verification.isNonPocketAccount(accountID) &&
						verification.accountOpen(accountID)) {
					
					System.out.println("verified");
					String amount = txtEnterAmount.getText();
					
					if(npAccount.withdraw(Double.parseDouble(amount))) {
						JOptionPane.showMessageDialog(frame, "Transaction Successful."); 
						System.out.println(npAccount.getBalance());
						
						if(npAccount.getBalance() <= .019) {
							npAccount.closeAccount();
							JOptionPane.showMessageDialog(frame, "Account Closed."); 
						}
					} else {
						JOptionPane.showMessageDialog(frame, "Transaction Failed."); 
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "Action Not Allowed."); 
				}
			}
		});
		// ====================================================================
		// Purchase
		// ====================================================================
		JButton btnPurchase = new JButton("Purchase");
		btnPurchase.setBounds(6, 73, 117, 29);
		frame.getContentPane().add(btnPurchase);
		
		btnPurchase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		        Verification verification = new Verification(conn);
				String accountID = (String) originAccountComboBox.getSelectedItem(); 
				PocketAccount pAccount = new PocketAccount(conn, accountID); 	
        
				if (verification.isPocketAccount(accountID) &&
						verification.accountOpen(accountID)) {
					
					System.out.println("verified");
					String amount = txtEnterAmount.getText();
					
					if(pAccount.purchase(Double.parseDouble(amount))) {
						JOptionPane.showMessageDialog(frame, "Transaction Successful."); 
					} else {
						JOptionPane.showMessageDialog(frame, "Transaction Failed."); 
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "Action Not Allowed."); 
				}
			}
		});
		// ====================================================================
		// Transfer
		// ====================================================================
		JButton btnTransfer = new JButton("Transfer");
		btnTransfer.setBounds(6, 165, 117, 29);
		frame.getContentPane().add(btnTransfer);
		
		btnTransfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		        Verification verification = new Verification(conn);
				String accountID = (String) originAccountComboBox.getSelectedItem(); 
				String destAccountID = txtAccountId.getText();
				NonPocketAccount npAccount = new NonPocketAccount(conn, accountID); 	
				if (verification.isNonPocketAccount(accountID) &&
						verification.isNonPocketAccount(destAccountID) && 
						verification.accountOpen(accountID) && 
						verification.accountOpen(destAccountID) && 
						verification.verifyTransfer(npAccount, destAccountID, taxID)) {
					
					System.out.println("verified");
					String amount = txtEnterAmount.getText();
					
					if(npAccount.transfer(Double.parseDouble(amount), destAccountID)) {
						JOptionPane.showMessageDialog(frame, "Transaction Successful."); 
						
						if(npAccount.getBalance() <= .019) {
							npAccount.closeAccount();
							JOptionPane.showMessageDialog(frame, "Account Closed."); 
						}
					} else {
						JOptionPane.showMessageDialog(frame, "Transaction Failed."); 
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "Action Not Allowed."); 
				}
			}
		});
		
		// ====================================================================
		// Collect
		// ====================================================================
		JButton btnCollect = new JButton("Collect");
		btnCollect.setBounds(6, 127, 117, 29);
		frame.getContentPane().add(btnCollect);
		
		btnCollect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		        Verification verification = new Verification(conn);
				String accountID = (String)originAccountComboBox.getSelectedItem(); 
				PocketAccount npAccount = new PocketAccount(conn, accountID); 	
				
				if (verification.isPocketAccount(accountID) &&
						verification.accountOpen(accountID)) {
					
					System.out.println("verified");
					String amount = txtEnterAmount.getText();
					
					if(npAccount.collect(Double.parseDouble(amount))) {
						JOptionPane.showMessageDialog(frame, "Transaction Successful."); 
					} else {
						JOptionPane.showMessageDialog(frame, "Transaction Failed."); 
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "Action Not Allowed."); 
				}
			}
		});
		// ====================================================================
		// Wire
		// ====================================================================
		JButton btnWire = new JButton("Wire");
		btnWire.setBounds(6, 194, 117, 29);
		frame.getContentPane().add(btnWire);
	
		btnWire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		        Verification verification = new Verification(conn);
				String accountID = (String) originAccountComboBox.getSelectedItem(); 
				String destAccountID = txtAccountId.getText();

				
				NonPocketAccount npAccount = new NonPocketAccount(conn, accountID); 	
				if (verification.isNonPocketAccount(accountID) &&
						verification.isNonPocketAccount(destAccountID) &&
						verification.accountOpen(accountID) &&
						verification.accountOpen(destAccountID)) {
					
					System.out.println("verified");
					String amount = txtEnterAmount.getText();
					
					if(npAccount.wire(Double.parseDouble(amount), destAccountID)) {
						JOptionPane.showMessageDialog(frame, "Transaction Successful."); 
						
						if(npAccount.getBalance() <= .019) {
							npAccount.closeAccount();
							JOptionPane.showMessageDialog(frame, "Account Closed."); 
						}
					} else {
						JOptionPane.showMessageDialog(frame, "Transaction Failed."); 
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "Action Not Allowed."); 
				}
			}
		});
		// ====================================================================
		// Pay Friend
		// ====================================================================
		JButton btnPayFriend = new JButton("Pay Friend");
		btnPayFriend.setBounds(6, 241, 117, 29);
		frame.getContentPane().add(btnPayFriend);
		
		btnPayFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		    Verification verification = new Verification(conn);
				String accountID = (String)originAccountComboBox.getSelectedItem(); 
				String destAccountID = txtFriendId.getText();
				PocketAccount pAccount = new PocketAccount(conn, accountID); 	
				if (verification.isPocketAccount(accountID) &&
						verification.isPocketAccount(destAccountID) && 
						verification.accountOpen(accountID) && 
						verification.accountOpen(destAccountID)) {
					
			    System.out.println("verified");
					String amount = txtEnterAmount.getText();
					
					if(pAccount.payFriend(Double.parseDouble(amount), destAccountID)){
						JOptionPane.showMessageDialog(frame, "Transaction Successful."); 
					} else {
						JOptionPane.showMessageDialog(frame, "Transaction Failed."); 
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "Action Not Allowed."); 
				}
			}
		});
	}
}