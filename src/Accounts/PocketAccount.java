package Accounts;

import java.sql.*;

public class PocketAccount extends Account{
	
	public PocketAccount(Connection conn, Statement stmt, double balance) {
		super(conn, stmt, balance);
		
	}

	// Move a speciﬁed amount of money from the linked checking/savings account to the pocket account.
	public boolean topUp(int sourceID, double amount) {
		return true;
	}
	
	// Subtract money from the account balance.
	public boolean purchase(double amount) {
		return true;
	}
	
	// Move a speciﬁed amount of money from the pocket account back to the linked checking/savings account, there will be a 3% fee assessed.
	public boolean collect(double amount) {
		return true; 
	}
	
	// Move a speciﬁed amount of money from the pocket account to a speciﬁed customer’s pocket account.
	public boolean payFriend(double amount, int friendID) {
		return true; 
	}
}
