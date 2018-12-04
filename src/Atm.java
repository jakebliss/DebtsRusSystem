import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;

import Accounts.Account;
import Customers.Customer;

public class Atm {

	private JFrame frame;
	private JTextField txtEnterAmount;
	private JTextField txtFriendId;
	private JTextField txtAccountId;

	// ====================================================================
	// Launch Application
	// ====================================================================
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Atm window = new Atm(args[1]);
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
	public Atm(String taxId) {
		initialize(taxId);
	}

	// ====================================================================
	// Initialize Contents of Frame
	// ====================================================================
	private void initialize(String taxId) {
		frame = new JFrame();
		frame.setBounds(100, 100, 492, 321);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
				
		ArrayList<String> originAccounts = Customer.getAllAssocAccounts(taxId);
		JComboBox originAccountComboBox = new JComboBox(originAccounts.toArray());
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
				String originAccount = (String)originAccountComboBox.getSelectedItem();
		        String totalAmount = txtEnterAmount.getText();
		        String friendId = txtFriendId.getText();
		        String targetAccount = txtAccountId.getText();
				
				if(Account.deposit(originAccount, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
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
				String originAccount = (String)originAccountComboBox.getSelectedItem();
		        String totalAmount = txtEnterAmount.getText();
		        String friendId = txtFriendId.getText();
		        String targetAccount = txtAccountId.getText();
				
				if(Account.topUp(originAccount, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
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
				String originAccount = (String)originAccountComboBox.getSelectedItem();
		        String totalAmount = txtEnterAmount.getText();
				
				if(Account.withdrawal(originAccount, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
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
				String originAccount = (String)originAccountComboBox.getSelectedItem();
		        String totalAmount = txtEnterAmount.getText();
				
				if(Account.purchase(originAccount, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
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
				String originAccount = (String)originAccountComboBox.getSelectedItem();
		        String totalAmount = txtEnterAmount.getText();
		        String targetAccount = txtAccountId.getText();
				
				if(Account.transfer(originAccount, targetAccount, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
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
				String originAccount = (String)originAccountComboBox.getSelectedItem();
		        String totalAmount = txtEnterAmount.getText();

				if(Account.collect(originAccount, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
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
				String originAccount = (String)originAccountComboBox.getSelectedItem();
		        String totalAmount = txtEnterAmount.getText();
		        String targetAccount = txtAccountId.getText();
		        
				if(Account.wire(originAccount, targetAccount, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
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
				String originAccount = (String)originAccountComboBox.getSelectedItem();
		        String totalAmount = txtEnterAmount.getText();
		        String friendId = txtFriendId.getText();
		        
				if(Account.payFriend(originAccount, friendId, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
	}
}