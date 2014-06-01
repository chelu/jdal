package auth;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;

import org.bouncycastle.util.encoders.Base64;
import org.easymock.EasyMock;
import org.jdal.auth.AuthHashMD5;
import org.jdal.auth.AuthManager;
import org.jdal.auth.AuthPlain;
import org.jdal.auth.AuthStrategy;
import org.jdal.dao.UserDao;
import org.jdal.model.DefaultUser;
import org.jdal.model.User;

/**
 * Test info.joseluismartin.auth package
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class AuthTest extends TestCase {
	/** test username */
	private static final String USERNAME = "test username";
	/** test supplied password */
	private static final String SUPPLIED_PASS = "supplied password";
	/** test stored password */
	private static final String STORED_PASS = "stored password";
	// create mocks
	/** user dao mock */
	private UserDao userDao = EasyMock.createMock(UserDao.class);
	/** auth strategy mock */
	private AuthStrategy authStrategy = EasyMock.createMock(AuthStrategy.class);
	
	/** 
	 * Test auth manager
	 */
	public void notestAuthManagerValidate() {
		
		AuthManager authManager = newAuthManager();
		reset(userDao);
		reset(authStrategy);
		assertFalse(authManager.validate(null, null));
		// record messages 
		expect(userDao.findByUsername(USERNAME)).andReturn(newTestUser());
		expect(authStrategy.validate(SUPPLIED_PASS, STORED_PASS)).andReturn(true);
		// sets mocks in replay state
		replay(authStrategy);
		replay(userDao);
		// and send message...
		boolean valid = authManager.validate(USERNAME, SUPPLIED_PASS);
		// test that colaborators receive all messages
		verify(authStrategy);
		verify(userDao);
		assertTrue(valid);
	}
		
	/**
	 * Test Auth plain
	 */
	public void testAuthPlain()  {
		AuthPlain auth = new AuthPlain();
		assertTrue(auth.validate(SUPPLIED_PASS, SUPPLIED_PASS));
		assertFalse(auth.validate(SUPPLIED_PASS, STORED_PASS));
		assertFalse(auth.validate(null, null));
	}
	
	/**
	 * Test auth hash md5
	 */
	public void testAuthHashMD5() {
		AuthHashMD5 auth = new AuthHashMD5();
		try {
			assertTrue(auth.validate(SUPPLIED_PASS, hashmd5(SUPPLIED_PASS)));
		}
		catch (NoSuchAlgorithmException e) {
			fail(e.getMessage());
		}
		assertFalse(auth.validate(SUPPLIED_PASS, STORED_PASS));
		assertFalse(auth.validate(null, null));
	}
	/**
	 * 
	 * @param asuppliedPass pass
	 * @return base64 encode of hash md5
	 * @throws NoSuchAlgorithmException from MessageDigester
	 */
	private String hashmd5(
			String suppliedPassword) throws NoSuchAlgorithmException   {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(suppliedPassword.getBytes());
		String encriptedPassword = null;
		try {
			encriptedPassword = new String(Base64.encode(md.digest()), "ASCII");
		} catch (UnsupportedEncodingException e) {}
		
		return encriptedPassword;
	}

	/**
	 * 
	 * @return new test user
	 */
	private User newTestUser() {
		DefaultUser user = new DefaultUser();
		user.setPassword(STORED_PASS);
		user.setUsername(USERNAME);
		return user;
		
	}

	/**
	 * 
	 * @return new auth manager
	 */
	private AuthManager newAuthManager() {
		AuthManager authManager = new AuthManager();
		reset(authStrategy);
		authManager.setAuthStrategy(authStrategy);
		reset(userDao);
		authManager.setUserDao(userDao);
		
		return authManager;
	}

}
