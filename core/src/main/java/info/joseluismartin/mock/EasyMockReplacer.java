package info.joseluismartin.mock;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.EasyMock;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * A BeanFactoryPostProcessor that replaces configured interfaces with 
 * EasyMock Proxys for Testing.
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 *
 */
@SuppressWarnings("unchecked")
public class EasyMockReplacer implements BeanFactoryPostProcessor {
	
	/** List of replaced classes */
	private List<Class> replacedClasses = new ArrayList<Class>();
	/** log */
	private Log log = LogFactory.getLog(EasyMockReplacer.class);
	
	/**
	 * PostProcess BeanFactory and replace configured classes wiht 
	 * EasyMocks proxys
	 * @param factory the Spring factory
	 * @throws BeansException if postprocessor fail
	 */
	
	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory)
			throws BeansException {
	
		for (Class clazz : replacedClasses) {
			replaceBean(clazz, factory);
		}

	}
	
	/**
	 * Replace Bean definition with a EasyMock Proxy 
	 * @param clazz clazz to replace
	 * @param factory factory to replace on
	 */
	private void replaceBean(Class clazz, 
			ConfigurableListableBeanFactory factory) {
		
		Object  mock = EasyMock.createMock(clazz);
		String[] names = factory.getBeanNamesForType(clazz);
		for (String name : names) {
			log.info("Registering bean " 
					+ name + " with mock " + clazz.getName());
			factory.registerSingleton(name, mock);
		}
	}

	/**
	 * Add Class to replace with EasyMock proxy
	 * @param clazz Class to replace.
	 * @return true if replaced
	 */
	public boolean add(Class clazz) {
		return replacedClasses.add(clazz);
	}
	/**
	 * Remove Class from replaced classes
	 * @param clazz Class to remove
	 * @return true if removed
	 */
	public boolean remove(Class clazz) {
		return replacedClasses.remove(clazz);
	}
}