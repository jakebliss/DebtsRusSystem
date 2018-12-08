package Customers;

import java.sql.*;
import java.sql.Date;

import Accounts.Account;
import Accounts.NonPocketAccount;
import Accounts.PocketAccount;
import CurrDate.CurrDate;
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
	    	    conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);

	            stmt = conn.createStatement();

    			String selName = "SELECT name, address FROM customers WHERE taxid = '" + mTaxID + "'"; 
    			System.out.println(selName);
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
    
    public Customer (String taxId, String address, String name) {
    	this.mName = name;
    	this.mAddress = address;
    	this.mTaxID = taxId;
    }
    public String getTaxID() { return mTaxID; } 
    
    public static void setPin(String oldPin, String newPin) {
    	//TODO: Update Pin from database for customer with oldPin
    }
    
    public static boolean verifyUser(String pin, String taxId) {
    	
    	Statement stmt = null;
    	Connection conn = null;
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
    	    String sql = "SELECT * FROM CUSTOMERS C WHERE C.PIN = '" + pin + "' AND C.TAXID = '" + taxId + "'";
    	    ResultSet rs = stmt.executeQuery(sql);
    	    
    	    boolean customerExists = false;
    	    
    	    while(rs.next()){
 	           customerExists = true;
 	        }
 	        rs.close();
 	        
 	        return customerExists;
	    
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
	    return false;
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
    	ArrayList<Account> accounts = Customer.getAllAssocPrimAccounts(taxId);
    	ArrayList<String> monthlyStatement = new ArrayList<String>();
    	double sumOfBalances = 0;
    	
    	Date currDate = CurrDate.getCurrentDate();
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(currDate);
    	int day = cal.get(Calendar.DAY_OF_MONTH);
    	int month = cal.get(Calendar.MONTH);
    	int year = cal.get(Calendar.YEAR);
    	
    	for(Account account : accounts) {
	    	ArrayList<Customer> customers = Account.getOwners(account.getID());
	    	ArrayList<Transaction> transactions = Account.getListOfCurrentMonthsTransactions(account.getID());
	    	float initialBalance = Account.calculateInitialBalance(account.getID(), account.getBalance(), transactions);
	    	double finalBalance = account.getBalance();
	    	sumOfBalances += finalBalance;
	    	
	    	monthlyStatement.add(account.getID());
	    	monthlyStatement.add("Transactions: \n");
	    	for(Transaction transaction : transactions) {
	    		Calendar transactionCal = Calendar.getInstance();
    			transactionCal.setTime(transaction.mDate);
    	    	int transactionDay = transactionCal.get(Calendar.DAY_OF_MONTH);
    	    	int transactionMonth = transactionCal.get(Calendar.MONTH);
    	    	int transactionYear = transactionCal.get(Calendar.YEAR);
    	    	
    	    	if(year == transactionYear && month == transactionMonth && day >= transactionDay) {
	    		    monthlyStatement.add("\t" + transaction.toString());
    	    	}
	    	}

	    	monthlyStatement.add("Owners: \n");
	    	for(Customer customer : customers) {
	    		monthlyStatement.add("\tName: " + customer.mName + " Address: " + customer.mAddress);
	    	}
	    	
	    	monthlyStatement.add("InitialBalance: " + Float.toString(initialBalance));
	    	monthlyStatement.add("FinalBalance: " + Double.toString(finalBalance));
    	}

    	if(sumOfBalances > 100000) {
    		monthlyStatement.add("[Warning] The limit of the insurance has been reached");
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
        	System.out.println("AID MAN:" + customer.getTaxID());
            ArrayList<Account> accounts = Customer.getAllAssocAccountsArrayList(customer.mTaxID); 
            int sum = 0;

            for(Account account : accounts) {
            	System.out.println(account.getID());
                sum += account.getSumOfDepositsTransfersAndWires();
                System.out.println("SUM: ");
                System.out.println(sum);
            }
            
            if(sum > 10000) {
            	flaggedCustomers.add("Customer Name: " + customer.mName + " TaxID: " + customer.getTaxID());
            }
        }
        System.out.println("done");
    	return flaggedCustomers;
    }
    
    public static ArrayList<String> getCustomerReport(String taxId) {
        ArrayList<Account> accounts = Customer.getAllAssocAccountsArrayList(taxId);
        ArrayList<String> lines = new ArrayList<String>();
        
        for(Account account : accounts) {
        	String line = "Account: " + account.getID() + " Status: " + account.getStatus();
        	lines.add(line);
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
    	    String sql = "SELECT * FROM CUSTOMERS";
    	    
    	    ResultSet rs = stmt.executeQuery(sql);
    	    System.out.println(sql);
    	    while(rs.next()){
 	           //Retrieve by column name
 	           String taxId  = rs.getString("taxId");
 	           String pin = rs.getString("pin");
 	           String name = rs.getString("name");
 	           String address = rs.getString("address");
 	          
 	           customers.add(new Customer(taxId, address, name));
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
    	ArrayList<Account> primAccounts = getAllPrimAccounts();
    	ArrayList<Account> coOwnAccounts = getAllAssocSecAccounts();
    	ArrayList<Account> allAccounts = new ArrayList<Account>();
    	
    	allAccounts.addAll(primAccounts);
    	allAccounts.addAll(coOwnAccounts);
    	
    	return allAccounts;
    }
    
//    public static ArrayList<String> getAllAssocAccounts(String taxId) {
//    	ArrayList<Account> primAccounts = getAllAssocPrimAccounts(taxId);
//    	ArrayList<Account> coOwnAccounts = getAllAssocCoOwnAccounts(taxId);
//    	ArrayList<Account> allAccounts = new ArrayList<Account>();
//    	
//    	allAccounts.addAll(primAccounts);
//    	allAccounts.addAll(coOwnAccounts);
//    	
//    	ArrayList<String> accounts = new ArrayList<String>();
//    	for(Account account : allAccounts) {
//    		accounts.add(account.getID());
//    	}
//    	
//    	return accounts;
//    }
    
    public static ArrayList<Account> getAllAssocAccountsArrayList(String taxId) {
    	ArrayList<Account> primAccounts = getAllAssocPrimAccounts(taxId);
    	ArrayList<Account> coOwnAccounts = getAllAssocCoOwnAccounts(taxId);
    	ArrayList<Account> allAccounts = new ArrayList<Account>();
    	
    	allAccounts.addAll(primAccounts);
    	allAccounts.addAll(coOwnAccounts);

    	return allAccounts;
    }
    
    public ArrayList<Account> getAllAssocAccounts(String taxId) {
    	ArrayList<Account> primAccounts = getAllPrimAccounts();
    	ArrayList<Account> coOwnAccounts = getAllAssocSecAccounts();
    	ArrayList<Account> allAccounts = new ArrayList<Account>();
    	
    	allAccounts.addAll(primAccounts);
    	allAccounts.addAll(coOwnAccounts);

    	return allAccounts;
    }

    public static ArrayList<Account> getAllAssocPrimAccounts(String taxId) {
    	Statement stmt = null;
    	Connection conn = null;
    	
    	Customer customer = new Customer(stmt, conn, taxId);
    	ArrayList<Account> primPocketAccounts = customer.getAssocPrimPocketAccounts();
    	ArrayList<Account> primNonPocketAccounts = customer.getAssocPrimNonPocketAccounts();
    	ArrayList<Account> primAccounts = new ArrayList<Account>();
    	
    	primAccounts.addAll(primPocketAccounts);
    	primAccounts.addAll(primNonPocketAccounts);
    	
    	return primAccounts;
    }
    
    public ArrayList<Account> getAllPrimAccounts() {
    	Statement stmt = null;
    	Connection conn = null;
    	
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
 	        String sql = "SELECT PA.aid " +
	        		     "FROM Sec_Owns SO, Owner_Groups OG, PKT_Accounts PA, Owns O " +
	        		     "WHERE '" + taxId + "' = SO.taxID " +
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
    
    public static ArrayList<Account> getAssocCoOwnNonPocketAccounts(String taxId) {
    	Statement stmt = null;
    	Connection conn = null;
    	try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
            ArrayList<Account> accounts = new ArrayList<Account>();
 	        String sql = "SELECT NPA.aid " +
       		     "FROM Sec_Owns SO, Owner_Groups OG, Non_pkt_accounts NPA, Owns O " +
       		     "WHERE '" + taxId + "' = SO.taxID " +
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


