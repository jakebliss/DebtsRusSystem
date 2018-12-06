package Accounts;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import CurrDate.CurrDate;
import Customers.Customer;
import Customers.Transaction;
import InterestRates.InterestRates;
import JDBCdriver.JDBCdriver;
import org.apache.commons.lang3.RandomStringUtils;


abstract public class Account {
	private String mID; 
	private double mBalance; 
	private boolean mStatus; 
	protected Date mDate; 
	protected Connection mConn;
	protected Statement mStmt; 
	
	public Account(Connection conn, String accountID) {
		mConn = conn; 
		
		//Get ID from database 
		mID = accountID; 

		mDate = CurrDate.getCurrentDate();
		
		try {
		mStmt = conn.createStatement();
		System.out.println("Connected database successfully..."); 
		String selBal = "SELECT balance FROM accounts WHERE aid = '" + mID + "'"; 
		ResultSet balRs = mStmt.executeQuery(selBal); 
	
    	while (balRs.next()) {
	    	  mBalance = balRs.getDouble("balance");
	    }
    	
    	balRs.close();
    	
		String selStatus = "SELECT status FROM accounts WHERE aid = '" + mID + "'"; 
		ResultSet statusRs = mStmt.executeQuery(selStatus); 
	
		mStatus = false; 
		
    	while (statusRs.next()) {
	    	 if(statusRs.getString("status").equals("Y")) {
	    		 mStatus = true; 
	    	 }
	    }
    	
    	balRs.close();
		} catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		}catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		}
	}
	
	public Account(String oid, boolean status, double balance) {
		mID = oid; 
		mBalance = balance; 
		mStatus = status; 
		mStmt = null; 
		mConn = null; 
	}
	
	public String getID() { return mID; }
	
	public double getBalance() { return mBalance; } 
	
	public boolean getStatus() { return mStatus; }
	
	public void setBalance(double balance) {
		mBalance = balance; 
	}
	
	public void setStatus(boolean status) {
		mStatus = status; 
	}
	
	public void closeAccount() {
		try {
			String close = "UPDATE Accounts A SET A.status = 'N' WHERE A.aid = '" + this.getID() + "'";
			ResultSet updateRs = mStmt.executeQuery(close);
		    updateRs.close(); 
			this.mStatus = false; 
		} catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		}catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		}
	} 

  public static ArrayList<Customer> getOwners(String aid) { // look at customer.java to see what is needed
    	ArrayList<Customer> customers = new ArrayList<Customer>();
    	Statement stmt = null;
    	Connection conn = null;
    	String sql = "";
        
	    return customers;
  }
	public int getSumOfDepositsTransfersAndWires() {
    	ArrayList<Transaction> transactions = Account.getListOfCurrentMonthsTransactions(this.getID());
        int sum = 0;
        
        for(Transaction transaction : transactions) {
        	if(transaction.mType.equals("D") && transaction.mOrgActId == this.getID()) {
        		sum += transaction.mAmount;
        	} else if((transaction.mType.equals("R") || transaction.mType.equals("E")) && transaction.mTargetActId == this.getID()) {
        		sum += transaction.mAmount;
        	}
        }
        return sum;
	}
	
	public static ArrayList<Transaction> getListOfCurrentMonthsTransactions(String aid) {
		Statement stmt = null;
    	Connection conn = null;
    	
    	ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
	        	        
		    String sql = "SELECT * FROM TRANSACTIONS T WHERE T.SOURCEID = '" + aid + "' OR T.DESTID = '" + aid + "'";
	        
            ResultSet rs = stmt.executeQuery(sql);
	        while(rs.next()){
	        	   String tid = rs.getString("TID");
	        	   Date date = rs.getDate("TDATE");
	        	   float amount = rs.getFloat("AMOUNT");
	        	   String type = rs.getString("TYPE");
	        	   String sourceId = rs.getString("SOURCEID");
	        	   String destId = rs.getString("DESTID");
	        	   Transaction transaction = new Transaction(tid, date, amount, type, sourceId, destId); 
		           transactions.add(transaction);
		    }
 	        rs.close();

 	        
 	        return transactions;
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
    	return transactions;
	}
	
	public static float calculateInitialBalance(ArrayList<Transaction> transactions) {
		return 0;
	}
    
	public static ArrayList<String> listClosedAccounts() {
		ArrayList<String> accounts = new ArrayList<String>();
		Statement stmt = null;
    	Connection conn = null;
    	String sql = "";
        
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
	        
	        sql = "SELECT *" +
	              " FROM ACCOUNTS" +
	        	  " WHERE STATUS = 'N'";
	        
	        
            ResultSet rs = stmt.executeQuery(sql);
    	    
    	    while(rs.next()){
 	           //Retrieve by column name
 	           String aid  = rs.getString("AID");

 	           //Display values
 	           System.out.print("aid: " + aid);
 	           
 	           accounts.add(aid);
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
	    return accounts;
	}
	
	public static boolean deleteClosedAccountsAndCustomers() {
		//TODO: Implement
		return true;
	}
    public static boolean addInterest() {
    	if(Account.interestAlreadyAddedThisMonth()) {
    		return false;
    	}
    	
    	ArrayList<String> accounts = Account.getAllOpenAccounts();
    	for(String aid : accounts) {
    		Account.accrueInterest(aid);
    	}
    	
    	return true;
    }
    
    public static float getAverageDailyBalance(String aid) {
    	ArrayList<Transaction> transactions = Account.getListOfCurrentMonthsTransactions(aid);
    	Date currDate = CurrDate.getCurrentDate();
    	
    	for(Transaction transaction : transactions) {
    		if(transaction.mDate.compareTo(currDate) <= 0) {
    			switch(transaction.mType) {
    				case "P": //purchase / payfriend?
    					break;
    				case "O": // topup
    					break;
    				case "L": // Collect
    					break;
    				case "D": // Deposit
    					break;
    				case "W": // Withdraw
    					break;
    				case "T": // Transfer
    					break;
    				case "R": // Wire
    					break;
    				case "A": // Accrue
    					break;
    				case "H": // writeCheck
    					break;
    					
    				default: 
    					break;
    			}
    		}
    	}
    	return (float) 0.0;
    }
    
	// ====================================================================
	// Private Functions
	// ====================================================================
	private static boolean interestAlreadyAddedThisMonth() {
		Statement stmt = null;
    	Connection conn = null;
    	String sql = "";
        
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
	        	        
	        sql = "SELECT *" +
	              " FROM TRANSACTIONS T" +
	        	  " WHERE T.TYPE = 'A'";	        
	        
            ResultSet rs = stmt.executeQuery(sql);
    	    
    	    while(rs.next()){
 	           return true;
 	        }
    	    
 	        rs.close();
 	        
 	        return false;
	        
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
	    
	private static ArrayList<String> getAllOpenAccounts() {
	    ArrayList<String> accounts = new ArrayList<String>();
		Statement stmt = null;
    	Connection conn = null;
    	String sql = "";
        
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
	        
	        sql = "SELECT *" +
	              " FROM ACCOUNTS" +
	        	  " WHERE STATUS = 'Y'";
	        
	        
            ResultSet rs = stmt.executeQuery(sql);
    	    
    	    while(rs.next()){
 	           //Retrieve by column name
 	           String aid  = rs.getString("AID");

 	           //Display values
 	           System.out.print("aid: " + aid);
 	           
 	           accounts.add(aid);
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
	    return accounts;
    }
	
    private static void accrueInterest(String aid) {
    	String accountType = Account.getAccountType(aid);
        float interestRate = InterestRates.getMonthlyInterestRate(accountType);
    	float averageDailyBalance = Account.getAverageDailyBalance(aid);
    	float interest = interestRate*averageDailyBalance;
    	Account.addInterestToBalance(aid, interest);
    }

    private static void addInterestToBalance(String aid, float interest) {
		Statement stmt = null;
    	Connection conn = null;
    	String sql = "";
        
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
	        
	    	// Add to Account Balance
	        
		    String updateSql = "UPDATE Accounts A SET A.balance = A.balance + " + interest + " WHERE A.aid = '" + aid + "'";
		    
		    String transactionID = RandomStringUtils.randomAlphanumeric(16);
			DateFormat mDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		    String insertTrans = "INSERT INTO transactions " + "(tid, amount, tdate, type, sourceid, destid) VALUES (" 
			    		 + "'" + transactionID + "'," + interest + "," + "TO_DATE('" + mDateFormat.format(CurrDate.getCurrentDate())
			    		 + "', 'YYYY/MM/DD')" + "," + "'P'" + ",'0','" + aid + "')";
	        
            ResultSet updateRs = stmt.executeQuery(updateSql);
 	        updateRs.close();

    	    ResultSet insertRs = stmt.executeQuery(insertTrans);
 	        insertRs.close();
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
    } 
    
    private static String getAccountType(String aid) {
		Statement stmt = null;
    	Connection conn = null;
        
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
	        	        
		    String paSql = "SELECT * FROM PKT_ACCOUNTS PA WHERE PA.AID = " + aid;
		    String npaSql = "SELECT * FROM NON_PKT_ACCOUNTS NPA WHER NPA.AID = " + aid;
	        
            ResultSet paRs = stmt.executeQuery(paSql);
	        while(paRs.next()){
		           return "Pocket";
		    }
 	        paRs.close();

    	    ResultSet npaRs = stmt.executeQuery(npaSql);
	        while(npaRs.next()){
	        	   String type = npaRs.getString("Type");
	        	   if(type == "I" ) {
	        		   return "Interest Checking";
	        	   }else if(type == "S") {
	        		   return "Saving";
	        	   } else if(type == "C") {
	        		   return "Student Checking";
	        	   }
		    }
 	        npaRs.close();
 	        
 	        return "";
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
    	return "";
    }
    

}
