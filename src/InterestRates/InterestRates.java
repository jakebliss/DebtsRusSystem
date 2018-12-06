package InterestRates;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Accounts.Account;
import JDBCdriver.JDBCdriver;

public class InterestRates {
	public static boolean setInterestRate(String accountType, String interestRate) {
		Statement stmt = null;
    	Connection conn = null;
    	String sql = "";
        Float newInterestRate = Float.parseFloat(interestRate);
        
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
	        
	        if(accountType.equals("Interest Checking") ) {
	 	        accountType = "INTEREST_CHECKING";
	        }else if(accountType.equals("Student Checking") ) {
	 	        accountType = "STUDENT_CHECKING";
	        }else if(accountType.equals("Saving") ) {
	 	        accountType = "SAVINGS";
	        }else if(accountType.equals("Pocket") ) {
	 	        accountType = "POCKET";
	        }
	        
	        sql = "UPDATE INTERESTRATES" +
	              " SET " + accountType + " = " + interestRate;
	        
	        ResultSet rs = stmt.executeQuery(sql);
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

	public static float getInterestRate(String accountType) {
		Statement stmt = null;
    	Connection conn = null;
    	String sql = "";
        Float interestRate = (float) 0;

	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();

	        if(accountType.equals("Interest Checking") ) {
	 	        accountType = "INTEREST_CHECKING";
	        }else if(accountType.equals("Student Checking") ) {
	 	        accountType = "STUDENT_CHECKING";
	        }else if(accountType.equals("Saving") ) {
	 	        accountType = "SAVINGS";
	        }else if(accountType.equals("Pocket") ) {
	 	        accountType = "POCKET";
	        }
	        
 	        sql = "SELECT * FROM INTERESTRATES";
 	        
	        ResultSet rs = stmt.executeQuery(sql);
	       	        
	        while(rs.next()){
		           //Retrieve by column name
		           interestRate  = rs.getFloat(accountType);

		           //Display values
		           //System.out.println("Interest Rate for " + accountType + ": " + interestRate);
		    }
		    rs.close();
	        
	        return interestRate;
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
		return interestRate;
	}
	
	public static float getMonthlyInterestRate(String accountType) {
		return (float) (InterestRates.getInterestRate(accountType)/((float) 12*1.0));
    }

}
