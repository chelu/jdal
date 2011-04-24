package info.joseluismartin.auth;

import java.security.NoSuchAlgorithmException;

/**
 * Strategy for validate user supplied passords with user stored passwords
 * 
 * @see AuthPlain 
 * @see AuthHashMD5
 * @see AuthManagers
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public interface AuthStrategy {
	
	/**
	 * Validate password.
	 * 
	 * @param suppliedPassword supplied password
	 * @param userPassword user password
	 * @return true if password match.
	 */

	boolean validate(String suppliedPassword, String userPassword);
	/** 
	 * 
	 * @param suppliedPassword
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	String crypt(String suppliedPassword) throws NoSuchAlgorithmException;

}
