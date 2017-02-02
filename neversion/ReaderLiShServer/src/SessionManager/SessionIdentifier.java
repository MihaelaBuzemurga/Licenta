package SessionManager;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SessionIdentifier {
	private static SecureRandom random = new SecureRandom();
	
	  public static String nextSessionId() {
	    return new BigInteger(130, random).toString(32);
	  }
}
