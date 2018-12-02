package Accounts;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Testing.TestDriver;
import java.sql.*;
import Customers.Customer;
import Customers.Transaction;

public class NonPocketAccount extends Account{
	private String mType;
	DateFormat mDateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	public NonPocketAccount(Connection conn, Statement stmt, double balance) {
		super(conn, stmt, balance);
		// TODO Auto-generated constructor stub
	}
	
	public NonPocketAccount(String oid, boolean status, double balance) {
		super(oid, status, balance);
	}

	// Add money to the checking or savings account balance.
	public boolean deposit(double amount) {
		if(amount < 0) {
			return false; 
		}
		String sAmount = Double.toString(amount); 
		try{
			 String transactionID = "testid15"; 
			 Date date = new Date();
			 
		     String updateBal = "UPDATE Accounts A SET A.balance = A.balance + " + sAmount + " WHERE A.aid = " + this.getID();
		     String insertTrans = "INSERT INTO transactions " + "(tid, amount, tdate, type, sourceid) VALUES (" 
		    		 + "'" + transactionID + "'," + amount + ","+ "TO_DATE('" + mDateFormat.format(date)
		    		 + "', 'YYYY/MM/DD')" + "," + "'D'" + "," + this.getID() + ")";
		     
		     System.out.println(updateBal);
		     ResultSet updateRs = mStmt.executeQuery(updateBal);
		     ResultSet insertRs = mStmt.executeQuery(insertTrans);
		     updateRs.close();
		     insertRs.close(); 
		  	return true; 
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		      return false; 
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		      return false; 
		   }
	}
	
	// Subtract money from the checking or savings account balance.
	public boolean withdraw(double amount) {
		if(amount < 0) {
			return false; 
		}
		
		String sAmount = Double.toString(amount); 
		try{
			 String transactionID = "testid"; 
			 Date date = new Date();
			 
		     String updateBal = "UPDATE Accounts A SET A.balance = A.balance - " + sAmount + 
		    		 " WHERE A.aid = " + this.getID();
		     String insertTrans = "INSERT INTO transactions " + "(tid, amount, tdate, type, sourceid) VALUES (" 
		    		 + "'" + transactionID + "'," + amount + ","+ "TO_DATE('" + mDateFormat.format(date)
		    		 + "', 'YYYY/MM/DD')" + "," + "'W'" + "," + this.getID() + ")";
		     ResultSet updateRs = mStmt.executeQuery(updateBal);
		     ResultSet insertRs = mStmt.executeQuery(insertTrans); 
		     updateRs.close();
		     insertRs.close(); 
		  	return true; 
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		      return false; 
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		      return false; 
		   }
	}
	
	// Subtract money from one savings or checking account and add it to another. 
	// A transfer can only occur between two accounts that have at least one owner in common. 
	// If the transfer was requested by a customer, she or he must be an owner of both accounts. 
	// Furthermore, the amount to be moved should not exceed $2,000.
	public boolean transfer(double amount, String destID) {
		if (amount < 0 || amount > 2000 || destID.length() < 5) {
			return false; 
		}
		String sAmount = Double.toString(amount); 
		try{
			  String transactionID = "testid20"; 
			  Date date = new Date();
			  
			  // TODO: Verify the accounts belong to same customer. 
			  

		     
		      
			  
		      String dsql = "UPDATE Accounts A SET A.balance = A.balance + " + sAmount + " WHERE A.aid = '" + destID + "'";
		      String wsql = "UPDATE Accounts B SET B.balance = B.balance - " + sAmount + " WHERE B.aid = " + this.getID(); 
		      String insertTrans = "INSERT INTO transactions " + "(tid, amount, tdate, type, sourceid, destid) VALUES (" 
			    		 + "'" + transactionID + "'," + amount + ","+ "TO_DATE('" + mDateFormat.format(date)
			    		 + "', 'YYYY/MM/DD')" + "," + "'T'" + "," + this.getID() + ",'" + destID + "')";
		      
		      System.out.println(srcOG); 
//		      System.out.println(dsql);
//		      System.out.println(wsql);
//		      System.out.println(insertTrans); 
		      

		      
		      ResultSet wrs = mStmt.executeQuery(wsql);
		      wrs.close();
		      
		      ResultSet drs = mStmt.executeQuery(dsql);
		      drs.close();
		      
		      ResultSet insertRs = mStmt.executeQuery(insertTrans); 
		      insertRs.close(); 
		      
		      return true; 
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		      return false; 
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		      return false; 
		   }
	}
	
	// Subtract money from one savings or checking account and add it to another. 
	// The customer that requests this action must be an owner of the account from which the money is subtracted. 
	// There is a 2% fee for this action.
	public boolean wire(double amount, String destID) {
		return true; 
	}
	
	// Add money to the checking or savings account. 
	// The amount added is the monthly interest rate times the average daily balance for the month 
	// (e.g., an account with balance $30 for 10 days and $60 for 20 days in a 30-day month has an 
	// average daily balance of $50, not $45!). Interest is added at the end of each month.
	public boolean accureIntrest() {
		return true; 
	}
	
	// Subtract money from the checking account. Associated with a check transaction is a check number.
	public boolean writeCheck() {
		return true; 
	}
	
	@Override
	public ArrayList<Customer> getOwners() {
		return null;
	}
	
	@Override
	public ArrayList<Transaction> getListOfLastMonthsTransactions() {
		return null;
	}
	
	@Override
	public int calculateInitialBalance(ArrayList<Transaction> transactions) {
		return 0;
	}
	
	@Override
	public int calculateFinalBalance(ArrayList<Transaction> transactions) {
		return 0;
	}
 }
