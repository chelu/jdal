package info.joseluismartin.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import info.joseluismartin.util.Base64Coder;


/**
 * An AuthStrategy that use HashMD5
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 *
 */
public class AuthHashMD5 implements AuthStrategy {

	/** log */
	private static Log log = LogFactory.getLog(AuthHashMD5.class);
	/**
	 * Test if userPassword is a md5 hash of suppliedPassword
	 * 
	 * @param suppliedPassword password form user
	 * @param userPassword password from db
	 * @return true if passwords match
	 */
	public boolean validate(String suppliedPassword, String userPassword) {
		if (suppliedPassword == null || userPassword == null) {
			return false;
		}

		try {
			String encriptedPassword = crypt(suppliedPassword);

			return userPassword.equals(encriptedPassword);
		} catch (NoSuchAlgorithmException nsae) {
			log.error(nsae);
			return false;
		}
	}
	
	/**
	 * Encript password 
	 * @param suppliedPassword
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public  String crypt(String suppliedPassword)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(suppliedPassword.getBytes());
		String encriptedPassword = String.valueOf(Base64Coder.encode(md.digest()));
		return encriptedPassword;
	}
}
