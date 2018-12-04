package Customers;

import java.util.Date;




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
    	//TODO: Delete the list of transactions from each of the accounts, in preparation for a new month of processing.
    	return true;
    }
}
