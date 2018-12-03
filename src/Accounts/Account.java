package Accounts;
import java.sql.*;

abstract public class Account {
	private String mID; 
	private double mBalance; 
	private boolean mStatus; 
	protected Connection mConn;
	protected Statement mStmt; 
	
	public Account(Connection conn, Statement stmt, double balance) {
		//Get ID from database 
		mID = "'werwer'"; 
		mBalance = balance; 
		mStatus = true; 
		mStmt = stmt; 
		mConn = conn; 
	}
	
	public String getID() { return mID; }
	
	public double getBalance() { return mBalance; } 
	
	public boolean getStatus() { return mStatus; }
	
	public void setBalance(double balance) {
		mBalance = balance; 
	}
	
	public void setStatus(boolean status) {
		mStatus = status; 
	}
	
	public void closeAccount() {
		try {
			String close = "UPDATE Accounts A SET A.status = 'C' WHERE A.aid = '" + this.getID() + "'";
			ResultSet updateRs = mStmt.executeQuery(close);
		    updateRs.close(); 
			this.mStatus = false; 
		} catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		}catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		}

	}
}
