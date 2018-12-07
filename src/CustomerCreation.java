import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Customers.Customer;

public class CustomerCreation {

	private JFrame frame;
	private JTextField textFieldName;
	private JTextField textFieldAddress;
	private static String taxID = null; 
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
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					taxID = args[0]; 
					CustomerCreation window = new CustomerCreation();
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
	public CustomerCreation() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
		//STEP 2: Register JDBC driver
	      Class.forName(JDBC_DRIVER);

	      //STEP 3: Open a connection
	      System.out.println("Connecting to a selected database...");
	      conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
	      stmt = conn.createStatement();
	      System.out.println("Connected database successfully..."); 
		}catch(SQLException se){
		    //Handle errors for JDBC
		    se.printStackTrace();
		}catch(Exception e){
		    //Handle errors for Class.forName
		    e.printStackTrace();
		}
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblPin = new JLabel("Address:");
		lblPin.setBounds(110, 102, 61, 16);
		frame.getContentPane().add(lblPin);
		
		textFieldAddress = new JTextField();
		textFieldAddress.setBounds(171, 97, 130, 26);
		frame.getContentPane().add(textFieldAddress);
		textFieldAddress.setColumns(10);
		
		JLabel lblTaxId = new JLabel("Name:");
		lblTaxId.setBounds(110, 69, 61, 16);
		frame.getContentPane().add(lblTaxId);
		
		textFieldName = new JTextField();
		textFieldName.setBounds(171, 64, 130, 26);
		frame.getContentPane().add(textFieldName);
		textFieldName.setColumns(10);
		
		
		JButton authenticateButton = new JButton("Create Customer");
		authenticateButton.setBounds(159, 135, 117, 29);
		frame.getContentPane().add(authenticateButton);
		
		authenticateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = textFieldName.getText();
				String address = textFieldAddress.getText();
				
				
				try {
					String newCust = "INSERT INTO customers(taxid, name, address, pin) values ('" +
							taxID + "','" + name + "','" + address + "', 1717)"; 
					System.out.println(newCust);
					ResultSet custRs = stmt.executeQuery(newCust); 		
					JOptionPane.showMessageDialog(frame, "Customer Created."); 
			    	custRs.close();
				} catch(SQLException se){
				    //Handle errors for JDBC
				    se.printStackTrace();
				}catch(Exception e){
				    //Handle errors for Class.forName
				    e.printStackTrace();
				} 
			    close();
			}
		});

	}
	
	private void close() {
	}

}
