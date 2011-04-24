package info.joseluismartin.beans;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Minimalistic singleton with application context
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */


public class AppCtx {
	
	/** spring application context */
	private static ApplicationContext context = null;
	
	/** 
	 * Search on classpath for context definition files and return the application context
	 * 
	 * @return the ApplicationContext
	 */
	public synchronized static ApplicationContext getInstance() {
		if (context == null) {
			context = new ClassPathXmlApplicationContext(
					new String[] {
							"classpath*:/applicationContext-resources.xml",
							"classpath*:/applicationContext-dao.xml",
							"classpath*:/applicationContext.xml",
							"classpath*:**/applicationContext*.xml" });
		}
		
		return context;
	}
}
