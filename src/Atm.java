import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;

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
					Atm window = new Atm("");
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
	public Atm(String pin) {
		initialize(pin);
	}

	// ====================================================================
	// Init Contents of Frame
	// ====================================================================
	private void initialize(String pin) {
		frame = new JFrame();
		frame.setBounds(100, 100, 492, 321);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
				
		String[] originAccounts = Customer.getAssocAccounts(pin);
		JComboBox originAccountComboBox = new JComboBox(originAccounts);
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
		
		JButton btnDeposit = new JButton("Deposit");
		btnDeposit.setBounds(6, 19, 117, 29);
		frame.getContentPane().add(btnDeposit);
		
		JButton btnTopup = new JButton("Top-Up");
		btnTopup.setBounds(6, 113, 117, 29);
		frame.getContentPane().add(btnTopup);
		
		JButton btnWithdrawal = new JButton("Withdrawal");
		btnWithdrawal.setBounds(6, 52, 117, 29);
		frame.getContentPane().add(btnWithdrawal);
		
		JButton btnPurchase = new JButton("Purchase");
		btnPurchase.setBounds(6, 83, 117, 29);
		frame.getContentPane().add(btnPurchase);
		
		JButton btnTransfer = new JButton("Transfer");
		btnTransfer.setBounds(6, 165, 117, 29);
		frame.getContentPane().add(btnTransfer);
		
		JButton btnWire = new JButton("Wire");
		btnWire.setBounds(6, 194, 117, 29);
		frame.getContentPane().add(btnWire);
		
		JButton btnPayFriend = new JButton("Pay Friend");
		btnPayFriend.setBounds(6, 241, 117, 29);
		frame.getContentPane().add(btnPayFriend);
	
		
		// ====================================================================
		// Action Listeners
		// ====================================================================
		String originAccount = (String)originAccountComboBox.getSelectedItem();
        String totalAmount = txtEnterAmount.getText();
        String friendID = txtFriendId.getText();
        String targetAccount = txtAccountId.getText();
        
		btnDeposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Account.deposit(originAccount, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
		
		btnTopup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Account.topUp(originAccount, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
		
		btnWithdrawal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Account.withdrawal(originAccount, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
		
		btnPurchase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Account.purchase(originAccount, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
		
		btnTransfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Account.transfer(originAccount, targetAccount, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
		
		btnWire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Account.wire(originAccount, targetAccount, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
		
		btnPayFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Account.payFriend(originAccount, friendId, totalAmount)) {
					//TODO: on success
				} else {
					//TODO: on failure
				}
			}
		});
	}
}
