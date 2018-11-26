
public class Customer {
    private String name, address;
    int taxID, pin;
    
    public void SetPin(String oldPin, String newPin) {
    	//TODO: Implement
    }
    
    public boolean VerifyPin(int pin) {
    	return this.pin == pin;
    }
    
    public static Account[] getAssocAccounts(int pin) {
    	return null;
    }
}
