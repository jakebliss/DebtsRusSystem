import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JTextField;

import Customers.Customer;

import javax.swing.JButton;

public class Authenticate extends JFrame {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Authenticate window = new Authenticate();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

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
		
		textField = new JTextField();
		textField.setBounds(171, 97, 130, 26);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton authenticateButton = new JButton("Authenticate");
		authenticateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String pin = textField.getText();
				
				boolean authenticated = authenticatePin(Integer.parseInt(pin));
				
			    if(authenticated) {
			    	JOptionPane.showMessageDialog(frame, "Authentication was successful.");
			    	String[] args = {pin};
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
	
	private boolean authenticatePin(int pin) {
		return Customer.verifyPin(pin);
	}
}
