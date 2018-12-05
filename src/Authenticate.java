import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JTextField;

import Customers.Customer;

import javax.swing.JButton;

public class Authenticate extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTextField textFieldPin;
	private JTextField textFieldTaxID;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Authenticate window = new Authenticate();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Authenticate() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblPin = new JLabel("PIN:");
		lblPin.setBounds(128, 102, 61, 16);
		frame.getContentPane().add(lblPin);
		
		textFieldPin = new JTextField();
		textFieldPin.setBounds(171, 97, 130, 26);
		frame.getContentPane().add(textFieldPin);
		textFieldPin.setColumns(10);
		
		JLabel lblTaxId = new JLabel("Tax ID:");
		lblTaxId.setBounds(110, 69, 61, 16);
		frame.getContentPane().add(lblTaxId);
		
		textFieldTaxID = new JTextField();
		textFieldTaxID.setBounds(171, 64, 130, 26);
		frame.getContentPane().add(textFieldTaxID);
		textFieldTaxID.setColumns(10);
		
		JButton authenticateButton = new JButton("Authenticate");
		authenticateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String pin = textFieldPin.getText();
				String taxId = textFieldTaxID.getText();
				
				boolean authenticated = Customer.verifyUser(pin, taxId);
				
			    if(authenticated) {
			    	JOptionPane.showMessageDialog(frame, "Authentication was successful.");
			    	String[] args = {pin, taxId};
			    	Atm.main(args);
			    	close();
			    } else {
			    	JOptionPane.showMessageDialog(frame, "Pin is invalid. Please try again or contact customer support.");
			    }
			}
		});
		authenticateButton.setBounds(159, 135, 117, 29);
		frame.getContentPane().add(authenticateButton);
	}
	
	private void close() {
		this.setVisible(false);
		this.dispose();
	}
}
