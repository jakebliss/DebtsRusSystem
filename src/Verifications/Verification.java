package Verifications;

import Customers.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Accounts.Account; 

public class Verification {
	protected Connection mConn;
	protected Statement mStmt; 
	
	public Verification (Connection conn) { 
		mConn = conn; 
		try {
			mStmt = conn.createStatement();
			System.out.println("Connected database successfully..."); 
		} catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		}catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		}
	}
	
	public boolean accountOpen (String accountID) {
		try {
			String selStatus = "SELECT status FROM Accounts WHERE aid = '" + accountID + "'";
			ResultSet statusRs = mStmt.executeQuery(selStatus);
			
			System.out.println(selStatus);
			
			String status = ""; 
			
			
	    	while (statusRs.next()) {
	    	  status = statusRs.getString("status");
	    	}
	    	  
	    	statusRs.close(); 
	    	
	    	if(status.equals("Y")) {
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
	
	public boolean verifyTransfer(Account srcAccount, String destAccountID, String taxID) {
		try {
			  String srcOG = "SELECT oid FROM Owns WHERE aid = '" + srcAccount.getID() + "'";
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
		    		  
		    		  String existPrimSrc = "SELECT oid FROM Prim_Owns WHERE taxid = '" + taxID
		    		  	+ "' AND oid = '" + sourceOid + "'"; 
		    		  
		    		  System.out.println(existPrimSrc);
		    		  
				      ResultSet sExistPrimRs = mStmt.executeQuery(existPrimSrc); 
				      
				      while(sExistPrimRs.next()){
		                  valSrc = true; 
				      }
				      
				      sExistPrimRs.close(); 
				      
		    		  String existSrc = "SELECT oid FROM Sec_Owns WHERE taxid = '" + taxID
				    		  	+ "' AND oid = '" + sourceOid + "'"; 
				    		  
				    		  System.out.println(existSrc);
				    		  
						      ResultSet sExistRs = mStmt.executeQuery(existSrc); 
						      
						      while(sExistRs.next()){
				                  valSrc = true; 
						      }
				      
				      sExistRs.close(); 
				      
		    		  String existPrimDest = "SELECT oid FROM Prim_Owns WHERE taxid = '" + taxID
				    		  	+ "' AND oid = '" + destOid + "'"; 
				    		  
				    		  System.out.println(existPrimDest);
				    		  
						      ResultSet dExistPrimRs = mStmt.executeQuery(existPrimDest); 
						      
						      while(dExistPrimRs.next()){
				                  valDest = true; 
						      }
						      
						      dExistPrimRs.close(); 
		    		  
		    		  String existDest = "SELECT oid FROM Sec_Owns WHERE taxid = '" + taxID
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
	
	public boolean isPocketAccount (String accountID) {
		try {
			  String existsStmt = "SELECT aid FROM Pkt_accounts WHERE aid = '" + accountID + "'";
			  
			  System.out.println(existsStmt);
			  
		      ResultSet existRs = mStmt.executeQuery(existsStmt);
		      		      	     		      
		      while(existRs.next()){
                  return true; 
		      }
		      
		      existRs.close();
		      
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
	
	public boolean isNonPocketAccount (String accountID) {
		try {
			  String existsStmt = "SELECT aid FROM Non_pkt_accounts WHERE aid = '" + accountID + "'";
			  
			  System.out.println(existsStmt);
			  
		      ResultSet existRs = mStmt.executeQuery(existsStmt);
		      		      	     		      
		      while(existRs.next()){
                  return true; 
		      }
		      
		      existRs.close();
		      
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
	
	public boolean customerExists (String taxID) {
		try {
			  String existsStmt = "SELECT taxid FROM customers WHERE taxid = '" + taxID + "'";
			  
			  System.out.println(existsStmt);
			  
		      ResultSet existRs = mStmt.executeQuery(existsStmt);
		      		      	     		      
		      while(existRs.next()){
                  return true; 
		      }
		      
		      existRs.close();
		      
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

