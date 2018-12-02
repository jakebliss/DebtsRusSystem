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
	
	public Account(Connection conn, Statement stmt, double balance) {
		//Get ID from database 
		mID = "'123456'"; 
		mBalance = balance; 
		mStatus = true; 
		mStmt = stmt; 
		mConn = conn; 
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
