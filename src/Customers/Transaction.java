package Customers;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;

import Accounts.Account;
import Accounts.NonPocketAccount;
import CurrDate.CurrDate;
import JDBCdriver.JDBCdriver;

public class Transaction {
	private String mTid;
	public Date mDate;
	public float mAmount;
	public String mType;
	public String mOrgActId;
	public String mTargetActId;
	
	public Transaction(String tid, Date date, float amount, String type, String orgActId, String targetActId) {
		mTid = tid;
		mDate = date;
		mAmount = amount;
		mType = type;
		mOrgActId = orgActId;
		mTargetActId = targetActId;
	}
	
    @Override
	public String toString() {
		return "Transaction [mTid=" + mTid + ", mDate=" + mDate + ", mAmount=" + mAmount + ", mType=" + mType
				+ ", mOrgActId=" + mOrgActId + ", mTargetActId=" + mTargetActId + "]";
	}

	public static boolean deleteTransactions() {
    	Statement stmt = null;
    	Connection conn = null;
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
            
 	        String sql = "DELETE FROM Transactions";
	        
	        ResultSet rs = stmt.executeQuery(sql);
	        
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
