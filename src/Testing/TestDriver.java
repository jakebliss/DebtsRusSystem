package Testing;
import Accounts.Account;
import Accounts.NonPocketAccount;
import Accounts.PocketAccount;
import Customers.Customer; 
import Verifications.Verification; 

import java.sql.*;

public class TestDriver {
	   // JDBC driver name and database URL
	   static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";  
	   static final String DB_URL = "jdbc:oracle:thin:@cloud-34-133.eci.ucsb.edu:1521:XE";

	   //  Database credentials
	   static final String USERNAME = "zakarybliss";
	   static final String PASSWORD = "password";
	   static Connection conn = null;
	   static Statement stmt = null; 
	   
	   public static void main(String[] args) {
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName(JDBC_DRIVER);

		      //STEP 3: Open a connection
		      System.out.println("Connecting to a selected database...");
		      conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		      stmt = conn.createStatement();
		      System.out.println("Connected database successfully...");
		  
		      //STEP 4: Execute a query
		      System.out.println("Creating statement...");
		      
		      Customer customer = new Customer("GE325DESIEN", "jake", "123 Jane Street", "1234");
		      Verification ver = new Verification(customer, stmt, conn); 
//		      NonPocketAccount npAccount = new NonPocketAccount(conn, stmt, "werwer"); 
//		      if(npAccount.deposit(2000))
//			      	System.out.println("deposit successful");	
		      
		      PocketAccount pAccount = new PocketAccount(conn, stmt, "jkjkjkjk"); 
		      if(ver.isPocketAccount("ejwkdbd"))
		    	  System.out.println("verification successful");
		      else 
		    	  System.out.println("not a pocket account");
		      		    
//		      if(pAccount.payFriend(100, "catcat"))
//		      	System.out.println("update successful");		   
		      	
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   System.out.println("Goodbye!");
		}//end main
}
