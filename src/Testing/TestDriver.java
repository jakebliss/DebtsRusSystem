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
		      
		      Customer customer = new Customer("GE325DESIEN", "jake", "123 Jane Street", 1234);
		      Verification ver = new Verification(customer, stmt, conn); 
		      NonPocketAccount npAccount = new NonPocketAccount(conn, stmt, 100); 
		      if(ver.verifyTransfer(npAccount, "werwer"))
		    	  System.out.println("verification successful");
		    
//		      if(npAccount.transfer(500, "werwer")); 
//		      	System.out.println("update successful");
		      
		      	
//		      //STEP 5: Extract data from result set
//		      while(rs.next()){
//		         //Retrieve by column name
//		         String cid  = rs.getString("cid");
//		         String cname = rs.getString("cname");
//		         String city = rs.getString("city");
//		         double discount = rs.getDouble("discount");
//
//		         //Display values
//		         System.out.print("cid: " + cid);
//		         System.out.print(", cname: " + cname);
//		         System.out.print(", city: " + city);
//		         System.out.println(", discount: " + discount);
//		      }
//		      rs.close();
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
