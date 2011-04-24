package info.joseluismartin.dao;

import info.joseluismartin.model.User;

/**
 * Interface for User DAOs
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface UserDao extends Dao<User, Long> {
	
	/**
	 * Find a User by username
	 * @param username the user name
	 * @return the User that match username
	 */
	User findByUsername(String username);

}
