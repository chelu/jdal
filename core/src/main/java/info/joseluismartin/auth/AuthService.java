package info.joseluismartin.auth;

/**
 * An Auth Service to validate Users
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */

public interface AuthService {
	
	/**
	 * Validate a username.
	 * @param username the user name
	 * @param password the supplied user password
	 * @return true if password match
	 */
	boolean validate(String username, String password);

}
