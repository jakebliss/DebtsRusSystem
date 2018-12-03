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
	
	public boolean accountOpen (String accountID) {
		try {
			String selStatus = "SELECT status FROM Accounts WHERE aid = '" + accountID + "'";
			ResultSet statusRs = mStmt.executeQuery(selStatus);
			
			String status = ""; 
			
	    	while (statusRs.next()) {
	    	  status = statusRs.getString("status");
	    	}
	    	
	    	if(status.equals("O")) {
	    		return true; 
	    	}
	    	
	    	return false; 
		} catch(SQLException se){
		    //Handle errors for JDBC
		    se.printStackTrace();
		    return false; 
		}catch(Exception e){
		    //Handle errors for Class.forName
		    e.printStackTrace();
		    return false; 
		}
	}
	
	public boolean verifyTransfer(Account srcAccount, String destAccountID) {
		try {
			  // Get the owner group of the accounts
			  //String srcOG = "select 'Hello' X from dual"; 
			  String srcOG = "SELECT oid FROM Owns WHERE aid = " + srcAccount.getID();
			  String destOG = "SELECT oid FROM Owns WHERE aid = '" + destAccountID + "'";
			  
			  System.out.println(srcOG);
			  System.out.println(destOG);
			  
		      ResultSet srcRs = mStmt.executeQuery(srcOG);
		      		      	      
		      String sourceOid = null; 
		      String destOid = null; 
		      
		      //System.out.print("\nx1\n");
		      while(srcRs.next()){
                    sourceOid  = srcRs.getString("oid");
                        //System.out.print("oid: " + oid);
		      }
		      
		      srcRs.close();
		      
		      ResultSet destRs = mStmt.executeQuery(destOG); 
		      
		      while (destRs.next()) {
		    	  	destOid = destRs.getString("oid"); 
		      }
		      
		      destRs.close(); 
		    	  
		      System.out.println(sourceOid);
		      System.out.println(destOid);
		    	  
		    	   
		    	  if(sourceOid != null && destOid != null) {
		    		  boolean valSrc = false; 
		    		  boolean valDest = false; 
		    		  
		    		  String existSrc = "SELECT oid FROM Sec_Owns WHERE taxid = '" + mCustomer.getTaxID()
		    		  	+ "' AND oid = '" + sourceOid + "'"; 
		    		  
		    		  System.out.println(existSrc);
		    		  
				      ResultSet sExistRs = mStmt.executeQuery(existSrc); 
				      
				      while(sExistRs.next()){
		                  valSrc = true; 
				      }
				      
				      sExistRs.close(); 
		    		  
		    		  String existDest = "SELECT oid FROM Sec_Owns WHERE taxid = '" + mCustomer.getTaxID()
		    		  	+ "' AND oid = '" + destOid + "'"; 
		    		  
		    		  System.out.println(existDest);
		    		  
				      ResultSet dExistRs = mStmt.executeQuery(existDest);
				      
				      while(dExistRs.next()){
		                  valDest = true; 
				      }
				      
				      dExistRs.close(); 
				      
				      if(valSrc && valDest) {
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
