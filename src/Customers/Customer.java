package Customers;

import java.sql.*;
import Accounts.Account;
import Accounts.NonPocketAccount;
import Accounts.PocketAccount;
import JDBCdriver.JDBCdriver;

import java.util.*;

public class Customer {

    private String mName, mAddress, mTaxID, mPin;
    
    public Customer (String taxID, String name, String address, String pin) {
    	mName = name; 
    	mAddress = address; 
    	mTaxID = taxID; 
    	mPin = pin; 
    }
    
    public String getTaxID() { return mTaxID; } 
    
    public static void setPin(String oldPin, String newPin) {
    	//TODO: Update Pin from database for customer with oldPin
    }
    
    public static boolean verifyUser(String pin, String taxId) {
    	//TODO: Query DB to see if pin has a customer linked to it. And return true if it does.
    	int enteredPin = Integer.parseInt(pin);
    	
    	return enteredPin == 1234 & taxId.equals("3920sdfadf");
    }
    
    // Given a customer, do the following for each account she owns (including
	// accounts which have closed but have not been deleted): 
	//     - generate a list of all transactions which have occurred in the last month. 
	//     - list the names and addresses of all owners of the account. 
    //     - The initial and final account balance is to be included. 
    //     - If the sum of the balances of the
    // accounts of which the customer is the primary owner exceeds $100,000, a message should be included
    // in the statement to warn the customer that the limit of the insurance has been reached.
    
    public static ArrayList<String> getMonthlyStatement(String taxId) {
    	ArrayList<Account> accounts = getAllAssocPrimAccounts(taxId);
    	ArrayList<String> monthlyStatement = new ArrayList<String>();
    	double sumOfBalances = 0;
    	
    	for(Account account : accounts) {
	    	ArrayList<Customer> customers = Account.getOwners(account.getID());
	    	ArrayList<Transaction> transactions = Account.getListOfCurrentMonthsTransactions(account.getID());
	    	float initialBalance = Account.calculateInitialBalance(transactions);
	    	double finalBalance = account.getBalance();
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
            ArrayList<Account> accounts = getAllAssocPrimAccounts(customer.getTaxID()); 
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
    
    public static ArrayList<String> getCustomerReport(String taxId) {
        ArrayList<Account> accounts = Customer.getAllAssocAccountsArrayList(taxId);
        ArrayList<String> lines = new ArrayList<String>();
        
        for(Account account : accounts) {
        	String line = "Account: " + account.getID() + " Status: " + account.getStatus();
        }
        
        return lines;
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
    public static ArrayList<String> getAllAssocAccounts(String taxId) {
    	ArrayList<Account> primAccounts = getAllAssocPrimAccounts(taxId);
    	ArrayList<Account> coOwnAccounts = getAllAssocCoOwnAccounts(taxId);
    	ArrayList<Account> allAccounts = new ArrayList<Account>();
    	
    	allAccounts.addAll(primAccounts);
    	allAccounts.addAll(coOwnAccounts);
    	
    	ArrayList<String> accounts = new ArrayList<String>();
    	for(Account account : allAccounts) {
    		accounts.add(account.getID());
    	}
    	
    	return accounts;
    }
    
    public static ArrayList<Account> getAllAssocAccountsArrayList(String taxId) {
    	ArrayList<Account> primAccounts = getAllAssocPrimAccounts(taxId);
    	ArrayList<Account> coOwnAccounts = getAllAssocCoOwnAccounts(taxId);
    	ArrayList<Account> allAccounts = new ArrayList<Account>();
    	
    	allAccounts.addAll(primAccounts);
    	allAccounts.addAll(coOwnAccounts);

    	return allAccounts;
    }

    public static ArrayList<Account> getAllAssocPrimAccounts(String taxId) {
    	ArrayList<Account> primPocketAccounts = getAssocPrimPocketAccounts(taxId);
    	ArrayList<Account> primNonPocketAccounts = getAssocPrimNonPocketAccounts(taxId);
    	ArrayList<Account> primAccounts = new ArrayList<Account>();
    	
    	primAccounts.addAll(primPocketAccounts);
    	primAccounts.addAll(primNonPocketAccounts);
    	
    	return primAccounts;
    }
    
    public static ArrayList<Account> getAssocPrimPocketAccounts(String taxId) {
    	Statement stmt = null;
    	Connection conn = null;
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
            ArrayList<Account> accounts = new ArrayList<Account>();
 	        String sql = "SELECT O.aid, O.balance, O.status" +
	        		     "FROM Prim_Owns PO, Owner_Groups OG, PKT_Accounts PA, Owns O" +
	        		     "WHERE " + taxId + " = PO.taxID" +
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
	           
	           accounts.add(new PocketAccount(oid, status, balance));
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
    
    public static ArrayList<Account> getAssocPrimNonPocketAccounts(String taxId) {
    	Statement stmt = null;
    	Connection conn = null;
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
            ArrayList<Account> accounts = new ArrayList<Account>();
 	        String sql = "SELECT O.aid, O.balance, O.status" +
       		             "FROM Prim_Owns PO, Owner_Groups OG, NON_PKT_Accounts NPA, Owns O" +
       		             "WHERE " + taxId + " = PO.taxID" +
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
    
    public static ArrayList<Account> getAllAssocCoOwnAccounts(String taxId){
    	ArrayList<Account> coOwnPocketAccounts = getAssocCoOwnPocketAccounts(taxId);
    	ArrayList<Account> coOwnNonPocketAccounts = getAssocCoOwnNonPocketAccounts(taxId);
    	ArrayList<Account> coOwnAccounts = new ArrayList<Account>();
    	
    	coOwnAccounts.addAll(coOwnPocketAccounts);
    	coOwnAccounts.addAll(coOwnNonPocketAccounts);
    	
    	return coOwnAccounts;
    }
    public static ArrayList<Account> getAssocCoOwnPocketAccounts(String taxId) {
    	Statement stmt = null;
    	Connection conn = null;
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
            ArrayList<Account> accounts = new ArrayList<Account>();
 	        String sql = "SELECT O.aid, O.balance, O.status" +
	        		     "FROM CO_Owns CO, Owner_Groups OG, PKT_Accounts PA, Owns O" +
	        		     "WHERE " + taxId + " = CO.taxID" +
	        		     "AND CO.oid = OG.oid" + 
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
	           
	           accounts.add(new PocketAccount(oid, status, balance));
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
    public static ArrayList<Account> getAssocCoOwnNonPocketAccounts(String taxId) {
    	Statement stmt = null;
    	Connection conn = null;
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
            ArrayList<Account> accounts = new ArrayList<Account>();
 	        String sql = "SELECT O.aid, O.balance, O.status" +
       		             "FROM CO_Owns CO, Owner_Groups OG, NON_PKT_Accounts NPA, Owns O" +
       		             "WHERE " + taxId + " = CO.taxID" +
       		             "AND CO.oid = OG.oid" + 
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

