package Accounts;

public class NonPocketAccount extends AccountNew{
	private String mType;
	
	public NonPocketAccount(double balance) {
		super(balance);
		// TODO Auto-generated constructor stub
	}

	// Add money to the checking or savings account balance.
	public boolean deposit(double amount) {
		return true; 
	}
	
	// Subtract money from the checking or savings account balance.
	public boolean withdraw(double amount) {
		return true; 
	}
	
	// Subtract money from one savings or checking account and add it to another. 
	// A transfer can only occur between two accounts that have at least one owner in common. 
	// If the transfer was requested by a customer, she or he must be an owner of both accounts. 
	// Furthermore, the amount to be moved should not exceed $2,000.
	public boolean transfer(double amount, String destID) {
		return true; 
	}
	
	// Subtract money from one savings or checking account and add it to another. 
	// The customer that requests this action must be an owner of the account from which the money is subtracted. 
	// There is a 2% fee for this action.
	public boolean wire(double amount, String destID) {
		return true; 
	}
	
	// Add money to the checking or savings account. 
	// The amount added is the monthly interest rate times the average daily balance for the month 
	// (e.g., an account with balance $30 for 10 days and $60 for 20 days in a 30-day month has an 
	// average daily balance of $50, not $45!). Interest is added at the end of each month.
	public boolean accureIntrest() {
		return true; 
	}
	
	// Subtract money from the checking account. Associated with a check transaction is a check number.
	public boolean writeCheck() {
		return true; 
	}
 }
