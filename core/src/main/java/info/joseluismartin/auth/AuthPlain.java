package info.joseluismartin.auth;

/**
 * An Auth Strategy that use plain passwords.
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public class AuthPlain implements AuthStrategy {

	/**
	 * Compare  supplied password wiht stored user password
	 * @param suppliedPassword the supplied password
	 * @param userPassword the user password
	 * @return true if both passwords are equals and not null
	 */
	public boolean validate(String suppliedPassword, String userPassword) {
		if (suppliedPassword == null || userPassword == null) {
			return false;
		}
		
		return suppliedPassword.equals(userPassword);
	}

	/**
	 * 
	 */
	public String crypt(String suppliedPassword) {
		return suppliedPassword;
	}

}
