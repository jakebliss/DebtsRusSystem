package Customers;

import java.sql.*;
import Accounts.Account;
import Accounts.NonPocketAccount;
import Accounts.PocketAccount;
import JDBCdriver.JDBCdriver;

import java.util.*;

public class Customer {

    private String mName, mAddress, mTaxID, mPin;
    private ArrayList <Account> secOwnList; 
    private ArrayList <Account> primOwnList; 
    
    public Customer (Statement stmt, Connection conn, String taxID, String pin) {
    	//Get ID from database 
    	mTaxID = taxID; 
    	mPin  = pin; 
    			
    	try {
    			String selName = "SELECT name, address FROM customers WHERE taxid = '" + mTaxID + "'"; 
    			ResultSet selRs = stmt.executeQuery(selName); 
    		
    	    	while (selRs.next()) {
    		    	  mName = selRs.getString("name");
    		    	  mAddress = selRs.getString("address"); 
    		    }
    	    	
    	    	selRs.close();
    	    	
    			} catch(SQLException se){
    			      //Handle errors for JDBC
    			      se.printStackTrace();
    			}catch(Exception e){
    			      //Handle errors for Class.forName
    			      e.printStackTrace();
    			}
    }
    
    public String getTaxID() { return mTaxID; } 
    
    public static void setPin(String oldPin, String newPin) {
    	//TODO: Update Pin from database for customer with oldPin
    }
    
    public static boolean verifyPin(String pin) {
    	//TODO: Query DB to see if pin has a customer linked to it. And return true if it does.
    	return pin.equals("1234");
    }
    
    // Given a customer, do the following for each account she owns (including
	// accounts which have closed but have not been deleted): 
	//     - generate a list of all transactions which have occurred in the last month. 
	//     - list the names and addresses of all owners of the account. 
    //     - The initial and final account balance is to be included. 
    //     - If the sum of the balances of the
    // accounts of which the customer is the primary owner exceeds $100,000, a message should be included
    // in the statement to warn the customer that the limit of the insurance has been reached.
    
    public ArrayList<String> getMonthlyStatement() {
    	ArrayList<Account> accounts = getAllAssocPrimAccounts();
    	ArrayList<String> monthlyStatement = new ArrayList<String>();
    	int sumOfBalances = 0;
    	
    	for(Account a : primOwnList) {
	    	ArrayList<Customer> customers = a.getOwners();
	    	ArrayList<Transaction> transactions = a.getListOfLastMonthsTransactions();
	    	int initialBalance = a.calculateInitialBalance(transactions);
	    	int finalBalance = a.calculateFinalBalance(transactions);
	    	sumOfBalances += finalBalance;
    	}
    	
    	if(sumOfBalances > 100000) {
    		System.out.println("[Warning] The limit of the insurance has been reached");
    	}

        return monthlyStatement;
    }
    
	// By federal law, any deposits over $10,000
	// for a single customer in one month must be reported to the government. Generate a list of all customers
	// which have a sum of deposits, transfers and wires during the current month, over all owned accounts
	// (active or closed), of over $10,000. (How to handle joint accounts?)    
    public static ArrayList<String> getDTER() {
        ArrayList<String> flaggedCustomers = new ArrayList<String>();
        ArrayList<Customer> customers = getAllCustomers();
        
        for(Customer customer : customers) {
            ArrayList<Account> accounts = customer.getAllAssocPrimAccounts(); 
            int sum = 0;
            
            for(Account account : accounts) {
                sum = account.getSumOfDepositsTransfersAndWires();
            }
            
            if(sum > 10000) {
            	flaggedCustomers.add("Customer Name: " + customer.mName + " TaxID: " + customer.getTaxID());
            }
        }
        
    	return flaggedCustomers;
    }
    
    public static ArrayList<Customer> getAllCustomers() {
    	Statement stmt = null;
    	Connection conn = null;
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
    	    ArrayList<Customer> customers = new ArrayList<Customer>();
    	    String sql = "SELECT C.taxId, C.pin, C.name, C.address" +
    	                 "FROM Customers C";
    	    
    	    ResultSet rs = stmt.executeQuery(sql);
    	    
    	    while(rs.next()){
 	           //Retrieve by column name
 	           String taxId  = rs.getString("taxId");
 	           String pin = rs.getString("pin");
 	           String name = rs.getString("name");
 	           String address = rs.getString("address");

 	           //Display values
 	           System.out.print("taxId: " + taxId);
 	           System.out.print(", pin: " + pin);
 	           System.out.print(", name: " + name);
 	           System.out.print(", address: " + address);
 	           
 	           customers.add(new Customer(taxId, name, address, pin));
 	        }
 	        rs.close();
 	        
 	        return customers;
	    
	    }catch(SQLException se){
	        //Handle errors for JDBC
	        se.printStackTrace();
	     }catch(Exception e){
	        //Handle errors for Class.forName
	        e.printStackTrace();
	     }finally{
	        try{
	           if(conn!=null)
	              conn.close();
	        }catch(SQLException se){
	           se.printStackTrace();
	        }//end finally try
	     }//end try
	    return null;
    }
    
    public ArrayList<Account> getAllAssocPrimAccounts() {
    	ArrayList<Account> primPocketAccounts = getAssocPrimPocketAccounts();
    	ArrayList<Account> primNonPocketAccounts = getAssocPrimNonPocketAccounts();
    	ArrayList<Account> primAccounts = new ArrayList<Account>();
    	
    	primAccounts.addAll(primPocketAccounts);
    	primAccounts.addAll(primNonPocketAccounts);
    	
    	return primAccounts;
    }
    
    public ArrayList<Account> getAssocPrimPocketAccounts() {
    	Statement stmt = null;
    	Connection conn = null;
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
            ArrayList<Account> accounts = new ArrayList<Account>();
 	        String sql = "SELECT O.aid, O.balance, O.status " +
	        		     "FROM Prim_Owns PO, Owner_Groups OG, PKT_Accounts PA, Owns O" +
	        		     "WHERE " + mTaxID + " = PO.taxID" +
	        		     "AND PO.oid = OG.oid" + 
	        		     "AND OG.oid = O.oid" +
	        		     "AND O.aid = PA.aid";
	        
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        while(rs.next()){
	           //Retrieve by column name
	           String oid  = rs.getString("oid");
	           String strStatus = rs.getString("status");
	           double balance = rs.getDouble("balance");

	           //Display values
	           System.out.print("oid: " + oid);
	           System.out.print(", status: " + strStatus);
	           System.out.print(", balance: " + balance);
	           boolean status = true;
	           if(strStatus == "N") {
	        	   status = false;
	           }
	           
	           accounts.add(new PocketAccount(conn, stmt, oid));
	        }
	        rs.close();
	        
	        return accounts;
	        
	     }catch(SQLException se){
	        //Handle errors for JDBC
	        se.printStackTrace();
	     }catch(Exception e){
	        //Handle errors for Class.forName
	        e.printStackTrace();
	     }finally{
	        try{
	           if(conn!=null)
	              conn.close();
	        }catch(SQLException se){
	           se.printStackTrace();
	        }//end finally try
	     }//end try
	    return null;
    }
    
    public ArrayList<Account> getAssocPrimNonPocketAccounts() {
    	Statement stmt = null;
    	Connection conn = null;
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
            ArrayList<Account> accounts = new ArrayList<Account>();
 	        String sql = "SELECT O.aid, O.balance, O.status" +
       		             "FROM Prim_Owns PO, Owner_Groups OG, NON_PKT_Accounts NPA, Owns O" +
       		             "WHERE " + mTaxID + " = PO.taxID" +
       		             "AND PO.oid = OG.oid" + 
       		             "AND OG.oid = O.oid" +
       		             "AND O.aid = NPA.aid";
	        
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        while(rs.next()){
	           //Retrieve by column name
	           String oid  = rs.getString("oid");
	           String strStatus = rs.getString("status");
	           Double balance = rs.getDouble("balance");

	           //Display values
	           System.out.print("oid: " + oid);
	           System.out.print(", status: " + strStatus);
	           System.out.print(", balance: " + balance);
	           boolean status = true;
	           if(strStatus == "N") {
	        	   status = false;
	           }
	           
	           accounts.add(new NonPocketAccount(oid, status, balance));
	        }
	        rs.close();
	        
	        return accounts;
	        
	     }catch(SQLException se){
	        //Handle errors for JDBC
	        se.printStackTrace();
	     }catch(Exception e){
	        //Handle errors for Class.forName
	        e.printStackTrace();
	     }finally{
	        try{
	           if(conn!=null)
	              conn.close();
	        }catch(SQLException se){
	           se.printStackTrace();
	        }//end finally try
	     }//end try
	    return null;
    }
}

