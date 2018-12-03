package Verifications;

import Customers.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Accounts.Account; 

public class Verification {
	Customer mCustomer; 
	protected Connection mConn;
	protected Statement mStmt; 
	
	public Verification (Customer customer, Statement stmt, Connection conn) {
		mCustomer = customer; 
		mStmt = stmt; 
		mConn = conn; 
	}
	
	public boolean verifyTransfer(Account srcAccount, String destAccountID) {
		try {
			  // Get the owner group of the accounts
			  String srcOG = "select 'Hello' X from dual"; 
			  //String srcOG = "SELECT oid FROM Owns WHERE aid = " + srcAccount.getID();
			  String destOG = "SELECT oid FROM Owns WHERE aid = '" + destAccountID + "'";
			  
			  System.out.println(srcOG);
			  System.out.println(destOG);
			  
		      ResultSet srcRs = mStmt.executeQuery(srcOG);
		      ResultSet destRs = mStmt.executeQuery(destOG); 
		      
		      
		      	      
		      String sourceOid = null; 
		      String destOid = null; 
		      
		      
		    	  while (srcRs.next()) {
		    		  System.out.println(srcRs.getString("X"));
		    		  sourceOid = srcRs.getString("oid");
		    	  }
		    	  while (destRs.next()) {
		    		  destOid = destRs.getString("oid"); 
		    	  }
		    	  srcRs.close(); 
		    	  destRs.close(); 
		    	  
		    	  
		    	   
		    	  if(sourceOid != null && destOid != null) {
		    		  String existSrc = "SELECT oid FROM Sec_Owns WHERE taxid = " + mCustomer.getTaxID()
		    		  	+ " AND oid = " + sourceOid; 
		    		  
		    		  System.out.println(existSrc);
		    		  
				      ResultSet sExistRs = mStmt.executeQuery(existSrc); 
				      sExistRs.close(); 
		    		  
		    		  String existDest = "SELECT oid FROM Sec_Owns WHERE taxid = " + mCustomer.getTaxID()
		    		  	+ " AND oid = " + destOid; 
		    		  
		    		  System.out.println(existDest);
		    		  
				      ResultSet dExistRs = mStmt.executeQuery(existDest); 
				      dExistRs.close(); 
				      
				      if(sExistRs != null && dExistRs != null) {
				    	  return true; 
				      }
			      } 
		}catch(SQLException se){
		    //Handle errors for JDBC
		    se.printStackTrace();
		    return false; 
		}catch(Exception e){
		    //Handle errors for Class.forName
		    e.printStackTrace();
		    return false; 
		}
		return false; 
	}
}
