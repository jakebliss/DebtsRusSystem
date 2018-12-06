package CurrDate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;

import Accounts.Account;
import JDBCdriver.JDBCdriver;

public class CurrDate {
	public static Date getCurrentDate() {
		Statement stmt = null;
    	Connection conn = null;
        Date currDate = null;

	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();

 	        String sql = "SELECT *" +
 	                     "FROM CurrentDate";
	        
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        while(rs.next()){
	           //Retrieve by column name
	           currDate  = rs.getDate("Today");

	           //Display values
	           //System.out.println("Curr Date: " + currDate);

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
		
	public static boolean setCurrentDate(Date newDate) {
		Statement stmt = null;
    	Connection conn = null;
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    	
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();

 	        String sql = "UPDATE CurrentDate " +
 	                     "SET Today = TO_DATE('" + dateFormat.format(newDate) + "', 'YYYY/MM/DD')";
	        
 	        System.out.println(sql);
 	        
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

	public static void checkIfLastDayOfMonth(Date currDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currDate);
		int currDay = cal.get(Calendar.DAY_OF_MONTH);
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		if(currDay == lastDay) {
			Account.addInterest();
		}
    }
}
