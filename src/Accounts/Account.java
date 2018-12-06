package Accounts;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Date;

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
	        	        
		    String sql = "SELECT TID,  TO_CHAR(TO_DATE(TDATE), 'YYYY-MM-DD'), AMOUNT, TYPE, SOURCEID, DESTID FROM TRANSACTIONS T WHERE T.SOURCEID = '" + aid + "' OR T.DESTID = '" + aid + "'";
	       
            ResultSet rs = stmt.executeQuery(sql);
	        while(rs.next()){
	        	   String tid = rs.getString("TID");
	        	   Date date = rs.getDate(2);
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
	
	public static float calculateInitialBalance(String aid, double balance, ArrayList<Transaction> transactions) {
		if(transactions.isEmpty()) {
			return (float) balance;
		}
		
		for(Transaction transaction : transactions) {
			if(transaction.mOrgActId.equals(aid)) {
				switch(transaction.mType) {
    				case "P": //purchase 
        				balance -= transaction.mAmount;
    					break;
    				case "O": // top up
        				balance -= transaction.mAmount;
    					break;
    				case "L": // Collect
        				balance += transaction.mAmount;
    					break;
    				case "D": // Deposit
        				balance -= transaction.mAmount;
    					break;
    				case "W": // Withdraw
        				balance += transaction.mAmount;
    					break;
    				case "T": // Transfer
        				balance += transaction.mAmount;
    					break;
    				case "R": // Wire
        				balance += transaction.mAmount;
    					break;
    				case "A": // Accrue
        				balance -= transaction.mAmount;
    					break;
    				case "H": // writeCheck
        				balance += transaction.mAmount;
    					break;
    				case "F": // payFriend
        				balance += transaction.mAmount;
    					break;

    				default: 
    					break;
				}
			} else if(transaction.mTargetActId.equals(aid)){
				switch(transaction.mType) {
    				case "O": // top up
        				balance -= transaction.mAmount;
    					break;
    				case "L": // Collect
        				balance -= transaction.mAmount;
    					break;
    				case "T": // Transfer
        				balance -= transaction.mAmount;
    					break;
    				case "R": // Wire
        				balance -= transaction.mAmount;
    					break;
    				case "F": // payFriend
        				balance -= transaction.mAmount;
    					break;

    				default: 
    					break;
				}
			}
		}
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
	
	public static void deleteClosedAccounts() {
		Statement stmt = null;
    	Connection conn = null;
        
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
	        	        
	        String sql = "DELETE FROM ACCOUNTS A WHERE A.STATUS = 'N'";	        
	        
            ResultSet rs = stmt.executeQuery(sql);
 	        rs.close();
 	        return;	        
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
	
    public static boolean addInterest() {
    	if(Account.interestAlreadyAddedThisMonth()) {
    		return false;
    	}
    	
    	ArrayList<String> accounts = Account.getAllOpenAccounts();
    	for(String aid : accounts) {
    		double balance = Account.getAccountBalance(aid);
    		Account.accrueInterest(balance, aid);
    	}
    	
    	return true;
    }
    
    public static float getAverageDailyBalance(double balance, String aid) {
    	ArrayList<Transaction> transactions = Account.getListOfCurrentMonthsTransactions(aid);
    	Date currDate = CurrDate.getCurrentDate();
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(currDate);
    	int day = cal.get(Calendar.DAY_OF_MONTH);
    	if(day == 0) {return 0;}
    	int month = cal.get(Calendar.MONTH);
    	int year = cal.get(Calendar.YEAR);
    	
    	float currBalance = Account.calculateInitialBalance(aid, balance, transactions);
    	System.out.println("INIT BALANCE: " + Float.toString(currBalance));
        float totalBalance = 0;
        
    	for(int i = 1; i <= day; i++ ) {

    		float dayBalance = 0;

    		for(Transaction transaction : transactions) {
    			//convert transaction date to get day month and year
    			Calendar transactionCal = Calendar.getInstance();
    			transactionCal.setTime(transaction.mDate);
    	    	int transactionDay = transactionCal.get(Calendar.DAY_OF_MONTH);
    	    	int transactionMonth = transactionCal.get(Calendar.MONTH);
    	    	int transactionYear = transactionCal.get(Calendar.YEAR);
    	    	
    	    	if(year == transactionYear && month == transactionMonth && i == transactionDay) {
    	    		String type = transaction.mType;
        			//System.out.println(transaction.toString());
        			if(transaction.mOrgActId.equals(aid)) {
        				switch(type) {
    	    				case "P": //purchase 
    	        				dayBalance -= transaction.mAmount;
    	    					break;
    	    				case "O": // top up
    	        				dayBalance += transaction.mAmount;
    	    					break;
    	    				case "L": // Collect
    	        				dayBalance -= transaction.mAmount;
    	    					break;
    	    				case "D": // Deposit
    	        				dayBalance += transaction.mAmount;
    	    					break;
    	    				case "W": // Withdraw
    	        				dayBalance -= transaction.mAmount;
    	    					break;
    	    				case "T": // Transfer
    	        				dayBalance -= transaction.mAmount;
    	    					break;
    	    				case "R": // Wire
    	        				dayBalance -= transaction.mAmount;
    	    					break;
    	    				case "A": // Accrue
    	        				dayBalance += transaction.mAmount;
    	    					break;
    	    				case "H": // writeCheck
    	        				dayBalance -= transaction.mAmount;
    	    					break;
    	    				case "F": // payFriend
    	        				dayBalance -= transaction.mAmount;
    	    					break;
    	
    	    				default: 
    	    					break;
        				}
        			} else if(transaction.mTargetActId.equals(aid)){
        				switch(type) {
    	    				case "O": // top up
    	        				dayBalance += transaction.mAmount;
    	    					break;
    	    				case "L": // Collect
    	        				dayBalance += transaction.mAmount;
    	    					break;
    	    				case "T": // Transfer
    	        				dayBalance += transaction.mAmount;
    	    					break;
    	    				case "R": // Wire
    	        				dayBalance += transaction.mAmount;
    	    					break;
    	    				case "F": // payFriend
    	        				dayBalance += transaction.mAmount;
    	    					break;
    	
    	    				default: 
    	    					break;
    					}
        			}
    	    	}
    		}
    		
    		currBalance += dayBalance;
    		totalBalance += currBalance;
    		
//    		System.out.println("CURRENT BALANCE: " + Float.toString(currBalance));
//    		System.out.println("TOTAL BALANCE: " + Float.toString(totalBalance));
    	}
    	
    	return totalBalance/(float)day;
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
	
	private static float getAccountBalance(String aid) {
	    ArrayList<String> accounts = new ArrayList<String>();
		Statement stmt = null;
    	Connection conn = null;
    	String sql = "";
    	float balance = 0;
        
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
	        
	        sql = "SELECT *" +
	              " FROM ACCOUNTS" +
	        	  " WHERE AID = '" + aid + "'";
	        
	        
            ResultSet rs = stmt.executeQuery(sql);
    	    
    	    while(rs.next()){
 	           //Retrieve by column name
 	           balance  = rs.getFloat("BALANCE"); 	           
 	           accounts.add(aid);
 	        }
 	        rs.close();
	        
 	        return balance;
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
	    return 0;
    }
    private static void accrueInterest(Double balance, String aid) {
    	String accountType = Account.getAccountType(aid);
    	System.out.println("AID: " + aid);
        if(accountType == "") {
        	return;
        }
        
        float interestRate = InterestRates.getMonthlyInterestRate(accountType);
    	float averageDailyBalance = Account.getAverageDailyBalance(balance, aid);
    	float interest = interestRate*averageDailyBalance;
    	
    	System.out.println("Average Daily Balance: " + Float.toString(averageDailyBalance));
    	System.out.println("-----");
    	// Account.addInterestToBalance(aid, interest);
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
    	ResultSet paRs = null;
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
	        	        
		    String paSql = "SELECT * FROM PKT_ACCOUNTS PA WHERE PA.AID = '" + aid + "'";
		    String npaSql = "SELECT * FROM NON_PKT_ACCOUNTS NPA WHERE NPA.AID = '" + aid + "'";
	        
            paRs = stmt.executeQuery(paSql);
	        while(paRs.next()){
		           return "Pocket";
		    }
    	    ResultSet npaRs = stmt.executeQuery(npaSql);
	        while(npaRs.next()){
	        	   String type = npaRs.getString("TYPE");
	        	   if(type == null) {
	        		 return "";  
	        	   } else if(type.equals("I")) {
	        		   return "Interest Checking";
	        	   }else if(type.equals("S")) {
	        		   return "Saving";
	        	   } else if(type.equals("C")){
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
	           if(paRs != null)
	    	        paRs.close();
	        }catch(SQLException se){
	           se.printStackTrace();
	        }//end finally try
	     }//end try
    	return "";
    }
    
}
