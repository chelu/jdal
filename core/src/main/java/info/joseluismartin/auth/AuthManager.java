package info.joseluismartin.auth;

import info.joseluismartin.dao.UserDao;
import info.joseluismartin.model.User;

/**
 * An Auth Manager that uses AuthStrategy to authorize users
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public class AuthManager implements AuthService {
	
	/** AuthStrategy used to compare passwords */
	private AuthStrategy authStrategy;
	/** UserDAO used to obtain user information */
	private UserDao userDao;
	
	/**
	 * Autenticate a User that was identified by password
	 * @param user the user to autenticate
	 * @param password plain user supplied password
	 * @return true if password is valid 
	 */
	public boolean authUser(User user, String password) {
		return user == null ? false 
				: authStrategy.validate(user.getPassword(), user.getPassword());
	}

	// AuthService Start
	/**
	 * Validate a user
	 * @param username  the username
	 * @param password the supplied user password
	 * @return true if password matchs.
	 */
	public final boolean validate(String username, String password) {
		return authUser(userDao.findByUsername(username), password);
	}
	// AutService End
	
	/**
	 * Getter for AuthStrategy
	 * @return the authStrategy
	 */
	public AuthStrategy getAuthStrategy() {
		return authStrategy;
	}

	/**
	 * Setter for AuthStrategy
	 * @param authStrategy the authStrategy to set
	 */
	public void setAuthStrategy(AuthStrategy authStrategy) {
		this.authStrategy = authStrategy;
	}

	/**
	 * @return the userDao
	 */
	public UserDao getUserDao() {
		return userDao;
	}

	/**
	 * @param userDao the userDao to set
	 */
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}