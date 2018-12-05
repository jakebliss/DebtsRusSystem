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
    
    public Customer (Statement stmt, Connection conn, String taxID) {
    	//Get ID from database 
    	mTaxID = taxID; 
    	mPin  = null; 
    			
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
    	
    	for(Account a : this.getAllAssocPrimAccounts()) {
	    	ArrayList<Customer> customers = a.getOwners();
	    	ArrayList<Transaction> transactions = a.getListOfLastMonthsTransactions();
	    	int initialBalance = a.calculateInitialBalance(transactions);
	    	int finalBalance = a.calculateFinalBalance(transactions);
	    	sumOfBalances += finalBalance;
    	}
    	System.out.println("after for");
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
    	    String sql = "SELECT C.taxId, C.pin, C.name, C.address " +
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
 	           
 	           customers.add(new Customer(stmt, conn, taxId, pin));
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
    
    public ArrayList<Account> getAllAssocAccounts() {
    	ArrayList<Account> primAccounts = getAllAssocPrimAccounts();
    	ArrayList<Account> secAccounts = getAllAssocSecAccounts();
    	ArrayList<Account> allAccounts = new ArrayList<Account>();
    	
    	allAccounts.addAll(primAccounts);
    	allAccounts.addAll(secAccounts);
    	
    	return allAccounts;
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
 	        String sql = "SELECT PA.aid " +
	        		     "FROM Prim_Owns PO, Owner_Groups OG, PKT_Accounts PA, Owns O " +
	        		     "WHERE '" + mTaxID + "' = PO.taxID " +
	        		     "AND PO.oid = OG.oid " + 
	        		     "AND OG.oid = O.oid " +
	        		     "AND O.aid = PA.aid";
 	        
 	        System.out.println(sql);
	        
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        while(rs.next()){
	           //Retrieve by column name
	           String aid  = rs.getString("aid");

	           //Display values
	           System.out.print("aid: " + aid);
	           
	           accounts.add(new PocketAccount(conn, aid));
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
 	        String sql = "SELECT NPA.aid " +
       		     "FROM Prim_Owns PO, Owner_Groups OG, Non_pkt_accounts NPA, Owns O " +
       		     "WHERE '" + mTaxID + "' = PO.taxID " +
       		     "AND PO.oid = OG.oid " + 
       		     "AND OG.oid = O.oid " +
       		     "AND O.aid = NPA.aid";
 	        
 	        System.out.println(sql);
	        
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        while(rs.next()){
	           //Retrieve by column name
	           String aid  = rs.getString("aid");

	           //Display values
	           System.out.print("aid: " + aid);
	           
	           accounts.add(new NonPocketAccount(conn, aid));
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
    
    public ArrayList<Account> getAllAssocSecAccounts() {
    	ArrayList<Account> secPocketAccounts = getAssocSecPocketAccounts();
    	ArrayList<Account> secNonPocketAccounts = getAssocSecNonPocketAccounts();
    	ArrayList<Account> secAccounts = new ArrayList<Account>();
    	
    	secAccounts.addAll(secPocketAccounts);
    	secAccounts.addAll(secNonPocketAccounts);
    	
    	return secAccounts;
    }
    
    public ArrayList<Account> getAssocSecPocketAccounts() {
    	Statement stmt = null;
    	Connection conn = null;
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
            ArrayList<Account> accounts = new ArrayList<Account>();
 	        String sql = "SELECT PA.aid " +
	        		     "FROM Sec_Owns SO, Owner_Groups OG, PKT_Accounts PA, Owns O " +
	        		     "WHERE '" + mTaxID + "' = SO.taxID " +
	        		     "AND SO.oid = OG.oid " + 
	        		     "AND OG.oid = O.oid " +
	        		     "AND O.aid = PA.aid";
 	        
 	        System.out.println(sql);
	        
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        while(rs.next()){
	           //Retrieve by column name
	           String aid  = rs.getString("aid");

	           //Display values
	           System.out.print("aid: " + aid);
	           
	           accounts.add(new PocketAccount(conn, aid));
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
    
    public ArrayList<Account> getAssocSecNonPocketAccounts() {
    	Statement stmt = null;
    	Connection conn = null;
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
            ArrayList<Account> accounts = new ArrayList<Account>();
 	        String sql = "SELECT NPA.aid " +
       		     "FROM Sec_Owns SO, Owner_Groups OG, Non_pkt_accounts NPA, Owns O " +
       		     "WHERE '" + mTaxID + "' = SO.taxID " +
       		     "AND SO.oid = OG.oid " + 
       		     "AND OG.oid = O.oid " +
       		     "AND O.aid = NPA.aid";
 	        
 	        System.out.println(sql);
	        
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        while(rs.next()){
	           //Retrieve by column name
	           String aid  = rs.getString("aid");

	           //Display values
	           System.out.print("aid: " + aid);
	           
	           accounts.add(new NonPocketAccount(conn, aid));
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


