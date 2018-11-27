
public class Account {
	public static boolean create(String accountType, String bankName, String initialAmount, String owners) {
		//TODO: Given an account type and other necessary information (e.g. owners, initial balance),
		//      create a new account with the specified characteristics. Note that this operation may introduce new
		//      customers to the bank. You may consider a create customer operation, but as far as the bank operations
		//      are concerned, customer creation is a part of account creation.
		return true;
	}
    public static boolean deposit(String originAccount, String totalAmount) {
        //TODO: Make call to change in DB
    	return true;
    }
    
    public static boolean topUp(String originAccount, String totalAmount) {
    	//TODO: Make call to change DB
    	return true;
    }
    
   public static boolean withdrawal(String originAccount, String totalAmount) {
       //TODO: Make call to change DB
	   return true;
   }
   
   public static boolean purchase(String originAccount, String totalAmount) {
       //TODO: Make call to change DB
	   return true;
   }
   
   public static boolean transfer(String originAccount, String targetAccount, String totalAmount) {
       //TODO: Make call to change DB
	   return true;
   }
   
   public static boolean wire(String originAccount, String targetAccount, String totalAmount) {
       //TODO: Make call to change DB
	   return true;
   }
   
   public static boolean payFriend(String originAccount, String friendId, String totalAmount) {
       //TODO: Make call to change DB
	   return true;
   }
   
   public static boolean deleteClosedAccountsAndCustomers() {
	   //TODO: Remove from the database all closed accounts and remove all customers who do not own any accounts (because their accounts have closed).
	   return true;
   }
   
   public static boolean addInterest(String interest) {
	   //TODO:For all open accounts, add the appropriate amount of monthly interest to the balance. There
	   //     should be a record in your database that interest has been added this month. So a repeated “Add Interest”
	   //     transaction would report a warning and do nothing else.
	   return true;
   }
}
