package Accounts;

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
	
	public NonPocketAccount(Connection conn, String accountID) {
		super(conn, accountID); 
		
		try {
			String selType = "SELECT type FROM Non_pkt_accounts WHERE aid = '" + this.getID() + "'"; 
			ResultSet typeRs = mStmt.executeQuery(selType); 
			
		    while (typeRs.next()) {
		    	mType = typeRs.getString("type");
			}
		    
		    typeRs.close();
		} catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		}catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		} 
	}
	
	public NonPocketAccount(String oid, boolean status, double balance) {
		super(oid, status, balance);
	}

	// Add money to the checking or savings account balance.
	// Precondition: Account is open
	public boolean deposit(double amount) {
		if(amount < 0) {
			return false; 
		}
		String sAmount = Double.toString(amount); 
		try{
			 String transactionID = "testid34"; 
			 Date date = new Date();
			 
		     String updateBal = "UPDATE Accounts A SET A.balance = A.balance + " + sAmount + " WHERE A.aid = '" + this.getID() + "'";
		     String insertTrans = "INSERT INTO transactions " + "(tid, amount, tdate, type, sourceid) VALUES (" 
		    		 + "'" + transactionID + "'," + amount + ","+ "TO_DATE('" + mDateFormat.format(date)
		    		 + "', 'YYYY/MM/DD')" + "," + "'D'" + ",'" + this.getID() + "')";
		     
		     System.out.println(updateBal);
		     System.out.println(insertTrans);
		     
		     ResultSet updateRs = mStmt.executeQuery(updateBal);
		     updateRs.close();
		     
		     mStmt = mConn.createStatement();
		     
		     ResultSet insertRs = mStmt.executeQuery(insertTrans);
		     System.out.println("inside try");
		     insertRs.close(); 
		    
		     this.setBalance(this.getBalance() + amount);
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
	// Precondition: Account is open
	public boolean withdraw(double amount) {
		if(amount < 0 || this.getBalance() - amount < 0) {
			return false; 
		}
		
		String sAmount = Double.toString(amount); 
		try{
			 String transactionID = "testid"; 
			 Date date = new Date();
			 
		     String updateBal = "UPDATE Accounts A SET A.balance = A.balance - " + sAmount + 
		    		 " WHERE A.aid = '" + this.getID() + "'";
		     String insertTrans = "INSERT INTO transactions " + "(tid, amount, tdate, type, sourceid) VALUES (" 
		    		 + "'" + transactionID + "'," + amount + ","+ "TO_DATE('" + mDateFormat.format(date)
		    		 + "', 'YYYY/MM/DD')" + "," + "'W'" + ",'" + this.getID() + "')";
		     
		     System.out.println(updateBal);
		     System.out.println(insertTrans);
		     
		     ResultSet updateRs = mStmt.executeQuery(updateBal);
		     updateRs.close();
		     
		     ResultSet insertRs = mStmt.executeQuery(insertTrans); 
		     insertRs.close(); 
		     
		     this.setBalance(this.getBalance() - amount);
		     System.out.println(this.getBalance());
		     
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
	// Preconditions: Account is open, owner has verified already 
	public boolean transfer(double amount, String destID) {
		if (amount < 0 || amount > 2000 || destID.length() < 5 || this.getBalance() - amount < 0) {
			return false; 
		}
		String sAmount = Double.toString(amount); 
		try{
			  String transactionID = "testid20"; 
			  Date date = new Date();
			  

		      String dsql = "UPDATE Accounts A SET A.balance = A.balance + " + sAmount + " WHERE A.aid = '" + destID + "'";
		      String wsql = "UPDATE Accounts B SET B.balance = B.balance - " + sAmount + " WHERE B.aid = '" + this.getID() + "'"; 
		      String insertTrans = "INSERT INTO transactions " + "(tid, amount, tdate, type, sourceid, destid) VALUES (" 
			    		 + "'" + transactionID + "'," + amount + ","+ "TO_DATE('" + mDateFormat.format(date)
			    		 + "', 'YYYY/MM/DD')" + "," + "'T'" + ",'" + this.getID() + "','" + destID + "')";
		      
		      System.out.println(dsql);
		      System.out.println(wsql);
		      System.out.println(insertTrans); 
		      
		      ResultSet wrs = mStmt.executeQuery(wsql);
		      wrs.close();
		      
		      ResultSet drs = mStmt.executeQuery(dsql);
		      drs.close();
		      
		      ResultSet insertRs = mStmt.executeQuery(insertTrans); 
		      insertRs.close(); 
		      
		      this.setBalance(this.getBalance() - amount); 
		      
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
	// Precondition: Accounts are open, owner verified already
	public boolean wire(double amount, String destID) {
		double fee = amount * .02; 
		if (amount < 0 || this.getBalance() - amount - fee < 0) {
			return false; 
		}
		String sAmount = Double.toString(amount); 
		String sAdjustedAmount = Double.toString(amount + fee); 
		try{
			  String transactionID = "testid21"; 
			  Date date = new Date();
			  
			  // TODO: Verify the accounts belong to same customer. 			  

		      String dsql = "UPDATE Accounts A SET A.balance = A.balance + " + sAmount + " WHERE A.aid = '" + destID + "'";
		      String wsql = "UPDATE Accounts B SET B.balance = B.balance - " + sAdjustedAmount + " WHERE B.aid = '" + this.getID() + "'"; 
		      String insertTrans = "INSERT INTO transactions " + "(tid, amount, tdate, type, sourceid, destid) VALUES (" 
			    		 + "'" + transactionID + "'," + amount + ","+ "TO_DATE('" + mDateFormat.format(date)
			    		 + "', 'YYYY/MM/DD')" + "," + "'R'" + ",'" + this.getID() + "','" + destID + "')";
		      
//		      System.out.println(dsql);
		      System.out.println(wsql);
		      System.out.println(insertTrans); 	
		      
		      ResultSet wrs = mStmt.executeQuery(wsql);
		      wrs.close();
		      
		      ResultSet drs = mStmt.executeQuery(dsql);
		      drs.close();
		      
		      ResultSet insertRs = mStmt.executeQuery(insertTrans); 
		      insertRs.close(); 
		      
		      this.setBalance(this.getBalance() - amount - fee); 
		      
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
	
	// Subtract money from the checking account. Associated with a check transaction is a check number.
	// Preconditions: Account is open
	public boolean writeCheck(double amount) {
		if(amount < 0 || !this.mType.equals("C") || this.getBalance() - amount < 0) {
			return false; 
		}
		
		String checkNum = "test"; 
		
		String sAmount = Double.toString(amount); 
		try{
			 String transactionID = "testid"; 
			 Date date = new Date();
			 
			 // TODO: associate check number with transaction
			 
		     String updateBal = "UPDATE Accounts A SET A.balance = A.balance - " + sAmount + 
		    		 " WHERE A.aid = '" + this.getID() + "'";
		     String insertTrans = "INSERT INTO transactions " + "(tid, amount, tdate, type, sourceid, destid) VALUES (" 
		    		 + "'" + transactionID + "'," + amount + ","+ "TO_DATE('" + mDateFormat.format(date)
		    		 + "', 'YYYY/MM/DD')" + "," + "'H'" + ",'" + this.getID() + "','" + checkNum + "')";
		     
		     ResultSet updateRs = mStmt.executeQuery(updateBal);
		     updateRs.close();
		     
		     ResultSet insertRs = mStmt.executeQuery(insertTrans); 		     
		     insertRs.close(); 
		     
		     this.setBalance(this.getBalance() - amount); 
		     
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
 }
