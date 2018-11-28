
public class Customer {
    private String name, address;
    int taxID, pin;
    
    public static void setPin(String oldPin, String newPin) {
    	//TODO: Update Pin from database for customer with oldPin
    }
    
    public static boolean verifyPin(int pin) {
    	//TODO: Query DB to see if pin has a customer linked to it. And return true if it does.
    	return 1234 == pin;
    }
    
    public static Account[] getAssocAccounts(int pin) {
    	//TODO: Query DB to get list of Associated Accounts for a customer and return said list
    	return null;
    }
    
    public static Transaction[] getMonthlyStatement(String customerId) {
        // TODO: Given a customer, do the following for each account she owns (including
 	   // accounts which have closed but have not been deleted): generate a list of all transactions which have
        // occurred in the last month. This statement should list the names and addresses of all owners of the
        // account. The initial and final account balance is to be included. If the sum of the balances of the
        // accounts of which the customer is the primary owner exceeds $100,000, a message should be included
        // in the statement to warn the customer that the limit of the insurance has been reached.
        return null;
    }
    
    public static Customer[] getDTER() {
    	// TODO: By federal law, any deposits over $10,000
    	// for a single customer in one month must be reported to the government. Generate a list of all customers
    	// which have a sum of deposits, transfers and wires during the current month, over all owned accounts
    	// (active or closed), of over $10,000. (How to handle joint accounts?)
    	return null;
    }
}
