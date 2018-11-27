
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
}
