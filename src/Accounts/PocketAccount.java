package Accounts;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PocketAccount extends Account{
	private String mType;
	DateFormat mDateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	public PocketAccount(Connection conn, Statement stmt, double balance) {
		super(conn, stmt, balance);
	}

	// Move a speciﬁed amount of money from the linked checking/savings account to the pocket account.
	public boolean topUp(double amount) {
		// TODO: Query to get linked account
		
		String linkedID = null; 
		Double linkedBal = null; 
		if (amount < 0 || linkedBal - amount < 0) {
			return false; 
		}
		String sAmount = Double.toString(amount); 
		try{
			  String transactionID = "testid20"; 
			  Date date = new Date();
			  
			  // TODO: Verify the accounts belong to same customer. 
			  

		      String dsql = "UPDATE Accounts A SET A.balance = A.balance + " + sAmount + " WHERE A.aid = '" + this.getID() + "'";
		      String wsql = "UPDATE Accounts B SET B.balance = B.balance - " + sAmount + " WHERE B.aid = " + linkedID; 
		      String insertTrans = "INSERT INTO transactions " + "(tid, amount, tdate, type, sourceid, destid) VALUES (" 
			    		 + "'" + transactionID + "'," + amount + ","+ "TO_DATE('" + mDateFormat.format(date)
			    		 + "', 'YYYY/MM/DD')" + "," + "'O'" + "," + linkedID + ",'" + this.getID() + "')";
		      
//		      System.out.println(srcOG); 
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
	
	// Subtract money from the account balance.
	public boolean purchase(double amount) {
		if(amount < 0 || this.getBalance() - amount < 0) {
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
		    		 + "', 'YYYY/MM/DD')" + "," + "'P'" + "," + this.getID() + ")";
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
	
	// Move a speciﬁed amount of money from the pocket account back to the linked checking/savings account, there will be a 3% fee assessed.
	public boolean collect(double amount) {
		// TODO: Query to get linked account
		
		String linkedID = null; 
		Double fee = amount * .03; 
		
		if (amount < 0 || this.getBalance() - amount - fee < 0) {
			return false; 
		}
		String sAmount = Double.toString(amount); 
		try{
			String transactionID = "testid20"; 
			Date date = new Date();
					  
			// TODO: Verify the accounts belong to same customer. 
					  

			String dsql = "UPDATE Accounts A SET A.balance = A.balance + " + sAmount + " WHERE A.aid = '" + linkedID + "'";
			String wsql = "UPDATE Accounts B SET B.balance = B.balance - " + (sAmount + fee) + " WHERE B.aid = " + this.getID(); 
			String insertTrans = "INSERT INTO transactions " + "(tid, amount, tdate, type, sourceid, destid) VALUES (" 
				+ "'" + transactionID + "'," + amount + ","+ "TO_DATE('" + mDateFormat.format(date)
				+ "', 'YYYY/MM/DD')" + "," + "'L'" + "," + this.getID() + ",'" + linkedID + "')";
				      
//			System.out.println(srcOG); 
//			System.out.println(dsql);
//			System.out.println(wsql);
//			System.out.println(insertTrans); 
				      

				      
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
	
	// Move a speciﬁed amount of money from the pocket account to a speciﬁed customer’s pocket account.
	public boolean payFriend(double amount, int friendID) {
		if (amount < 0 || this.getBalance() - amount < 0) {
			return false; 
		}
		String sAmount = Double.toString(amount); 
		try{
			  String transactionID = "testid20"; 
			  Date date = new Date();
			  
			  // TODO: Verify the accounts belong to same customer. 
			  

		      String dsql = "UPDATE Accounts A SET A.balance = A.balance + " + sAmount + " WHERE A.aid = '" + friendID + "'";
		      String wsql = "UPDATE Accounts B SET B.balance = B.balance - " + sAmount + " WHERE B.aid = " + this.getID(); 
		      String insertTrans = "INSERT INTO transactions " + "(tid, amount, tdate, type, sourceid, destid) VALUES (" 
			    		 + "'" + transactionID + "'," + amount + ","+ "TO_DATE('" + mDateFormat.format(date)
			    		 + "', 'YYYY/MM/DD')" + "," + "'P'" + "," + this.getID() + ",'" + friendID + "')";
		      
//		      System.out.println(srcOG); 
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
}
