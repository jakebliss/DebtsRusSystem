package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import JDBCdriver.JDBCdriver;

public class Bank {
	public static Date getCurrentDate() {
		Statement stmt = null;
    	Connection conn = null;
        Date currDate = null;

	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();

 	        String sql = "SELECT B.currentDate" +
 	                     "FROM BANK";
	        
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        while(rs.next()){
	           //Retrieve by column name
	           currDate  = rs.getDate("currDate");

	           //Display values
	           System.out.print("Curr Date: " + currDate);

	        }
	        rs.close();
	        
	        return currDate;
	        
	     }catch(SQLException se){
	        //Handle errors for JDBC
	        se.printStackTrace();
	     }catch(Exception e){
	        //Handle errors for Class.forName
	        e.printStackTrace();
	     }finally{
	        try{
	           if(conn!=null)
	              conn.close();
	        }catch(SQLException se){
	           se.printStackTrace();
	        }//end finally try
	     }//end try
		return currDate;
	}
	
	public static boolean setDate(Date newDate) {
		Statement stmt = null;
    	Connection conn = null;

	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();

 	        String sql = "UPDATE BANK" +
 	                     "SET currDate = " + newDate;
	        
	        stmt.executeQuery(sql);
	        
	        return true;
	        
	     }catch(SQLException se){
	        //Handle errors for JDBC
	        se.printStackTrace();
	     }catch(Exception e){
	        //Handle errors for Class.forName
	        e.printStackTrace();
	     }finally{
	        try{
	           if(conn!=null)
	              conn.close();
	        }catch(SQLException se){
	           se.printStackTrace();
	        }//end finally try
	     }//end try
		return false;
	}
	
	public static boolean setInterestRate(String bankAccountType, String interestRate) {
		Statement stmt = null;
    	Connection conn = null;
    	String sql = "";
        Float newInterestRate = Float.parseFloat(interestRate);
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();

	        if(bankAccountType.equals("Interest Checking") ) {
	 	        sql = "UPDATE BANK" +
	 	 	          "SET ICINTEREST = " + newInterestRate;
	        }else if(bankAccountType.equals("Student Checking") ) {
	 	        sql = "UPDATE BANK" +
	 	 	          "SET SCINTEREST = " + newInterestRate;
	        }else if(bankAccountType.equals("Saving") ) {
	 	        sql = "UPDATE BANK" +
		 	 	      "SET SINTEREST = " + newInterestRate;	
	        }else if(bankAccountType.equals("Pocket") ) {
	 	        sql = "UPDATE BANK" +
		 	 	      "SET PINTEREST = " + newInterestRate;	
	        }
	        
	        stmt.executeQuery(sql);
	        
	        return true;
	        
	     }catch(SQLException se){
	        //Handle errors for JDBC
	        se.printStackTrace();
	     }catch(Exception e){
	        //Handle errors for Class.forName
	        e.printStackTrace();
	     }finally{
	        try{
	           if(conn!=null)
	              conn.close();
	        }catch(SQLException se){
	           se.printStackTrace();
	        }//end finally try
	     }//end try
		return false;
	}

	public static boolean getInterestRate(String bankAccountType) {
		Statement stmt = null;
    	Connection conn = null;
    	String sql = "";
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();

	        String accountType = "";
	        if(bankAccountType.equals("Interest Checking") ) {
	 	        accountType = "ICINTEREST";
	        }else if(bankAccountType.equals("Student Checking") ) {
	 	        accountType = "SCINTEREST";
	        }else if(bankAccountType.equals("Saving") ) {
	 	        accountType = "SINTEREST";
	        }else if(bankAccountType.equals("Pocket") ) {
	 	        accountType = "PINTEREST";
	        }
	        
 	        sql = "SELECT B." + accountType +
	 	 	      "FROM Bank B";
 	        
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        Float interestRate = (float) 0;
	        
	        while(rs.next()){
		           //Retrieve by column name
		           interestRate  = rs.getFloat(accountType);

		           //Display values
		           System.out.print("Interest Rate for " + bankAccountType + ": " + interestRate);
		        }
		    rs.close();
	        
	        return true;
	     }catch(SQLException se){
	        //Handle errors for JDBC
	        se.printStackTrace();
	     }catch(Exception e){
	        //Handle errors for Class.forName
	        e.printStackTrace();
	     }finally{
	        try{
	           if(conn!=null)
	              conn.close();
	        }catch(SQLException se){
	           se.printStackTrace();
	        }//end finally try
	     }//end try
		return false;
	}
}
