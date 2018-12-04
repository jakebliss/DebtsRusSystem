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
import java.util.Date;

import Accounts.Account;
import Accounts.NonPocketAccount;
import Bank.Bank;
import JDBCdriver.JDBCdriver;

public class Transaction {
	private String mTid;
	public Date mDate;
	public int mAmount;
	public String mType;
	public String mOrgActId;
	public String mTargetActId;
	
	public Transaction(String tid, Date date, int amount, String type, String orgActId, String targetActId) {
		mTid = tid;
		mDate = date;
		mAmount = amount;
		mType = type;
		mOrgActId = orgActId;
		mTargetActId = targetActId;
	}
	
    public static boolean deleteTransactions() {
    	Statement stmt = null;
    	Connection conn = null;
	    try {
	    	Class.forName(JDBCdriver.JDBC_DRIVER);
	    	
	    	conn = DriverManager.getConnection(JDBCdriver.DB_URL, JDBCdriver.USERNAME, JDBCdriver.PASSWORD);
	    	
	        stmt = conn.createStatement();
            
    		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(Bank.getCurrentDate());
    		int month = cal.get(Calendar.MONTH);
    		int year = cal.get(Calendar.YEAR);
            Date boundDate = sdf.parse("01/" + month + "/" + year);

 	        String sql = "DELETE" +
       		             "FROM Transactions T" +
       		             "WHERE T.mDate <= " + boundDate +
       		             "AND PO.oid = OG.oid" + 
       		             "AND OG.oid = O.oid" +
       		             "AND O.aid = NPA.aid";
	        
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
