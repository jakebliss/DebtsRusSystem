package Accounts;

abstract public class AccountNew {
	private int mID; 
	private double mBalance; 
	private boolean mStatus; 
	
	public AccountNew(double balance) {
		//Get ID from database 
		mBalance = balance; 
		mStatus = true; 
	}
	
	public int getID() { return mID; }
	
	public double getBalance() { return mBalance; } 
	
	public boolean getStatus() { return mStatus; }
	
	public void setBalance(double balance) {
		mBalance = balance; 
	}
	
	public void setStatus(boolean status) {
		mStatus = status; 
	}
}
