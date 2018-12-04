package Accounts;
import java.sql.*;
import java.util.*;
import Customers.Customer;
import Customers.Transaction;

abstract public class Account {
	private String mID; 
	private double mBalance; 
	private boolean mStatus; 
	protected Connection mConn;
	protected Statement mStmt; 
	
	public Account(Connection conn, Statement stmt, String accountID) {
		mStmt = stmt; 
		mConn = conn; 
		
		//Get ID from database 
		mID = accountID; 
		
		try {
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
	    	 if(statusRs.getString("status").equals("O")) {
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
			String close = "UPDATE Accounts A SET A.status = 'C' WHERE A.aid = '" + this.getID() + "'";
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

	public ArrayList<Customer> getOwners() {
		return null;
	}

	public int getSumOfDepositsTransfersAndWires() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public ArrayList<Transaction> getListOfLastMonthsTransactions() {
		return null;
	}
	
	public int calculateInitialBalance(ArrayList<Transaction> transactions) {
		return 0;
	}
	
	public int calculateFinalBalance(ArrayList<Transaction> transactions) {
		return 0;
	}
	
	public static float getMonthlyInterstRate(String type) {
		if(type == "Savings") {
			
		} else if (type == "Checking") {
			
		}
		
		return 0;
    }
    
    public static boolean addInterest() {
    	if(interestAlreadyAddedThisMonth()) {
    		return false;
    	}
    	
    	ArrayList<Account> accounts = getAllOpenAccounts();
    	for(Account account : accounts) {
    		account.accrueInterest();
    	}
    	
    	return true;
    }
    
    
	// ====================================================================
	// Private Functions
	// ====================================================================
    private static boolean interestAlreadyAddedThisMonth() {
    	return false;
    }
    
    private static ArrayList<Account> getAllOpenAccounts() {
    	ArrayList<Account> accounts = new ArrayList<Account>();
    	
    	return accounts;
    }
    
    private boolean accrueInterest() {
    	float interestRate = getMonthlyInterestRate();
    	float averageDailyBalance = this.getAvarageDailyBalance();
    	float interest = interestRate*averageDailyBalance;
    	this.addToBalance(interest);
    }
    
    private void addToBalance(float interest) {
    	// Add to Account Balance
    	// Add Transaction To DB
    }
    
}
