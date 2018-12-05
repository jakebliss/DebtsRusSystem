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
	
	public Account(Connection conn, String accountID) {
		mConn = conn; 
		
		//Get ID from database 
		mID = accountID; 
		
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
}
