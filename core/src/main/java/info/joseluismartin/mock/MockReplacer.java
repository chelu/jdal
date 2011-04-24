package info.joseluismartin.mock;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;


/**
 * Replace singletons in configurableListableBeanFactory  
 * with mocks
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public class MockReplacer implements BeanFactoryPostProcessor {
	
	/** log */
	private Log log = LogFactory.getLog(MockReplacer.class);
	/** Map with bean names -> replaced beans instances */
	private Map<String, Object> replacedBeans = new HashMap<String, Object>();

	/**
	 * implents
	 * {@link BeanFactoryPostProcessor#
	 * postProcessBeanFactory(ConfigurableListableBeanFactory)}
	 * @param factory the BeanFactory to postprocess
	 * @throws BeansException if fail
	 */
	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory)
			throws BeansException {
			// FIXME: Read Bean Definition and make sure 
			// that the bean can be replaced.
			// TODO: Is better replace the bean definition,
			// no simply register the bean.
			for (String name : replacedBeans.keySet()) {
				Object bean = replacedBeans.get(name);
				log.debug("Replacing Bean "
						+ name + " with instance of class " 
						+ bean.getClass());
				factory.registerSingleton(name, bean);
			}
	}
	
	/** 
	 * Add a replaced mock 
	 * @param name name of singleton
	 * @param obj mock
	 */
	public final void add(String name, Object obj) {
		replacedBeans.put(name, obj);
	}

	/**
	 * get the map with replaced beans
	 * @return replacedBeans
	 */
	public Map<String, Object> getReplacedBeans() {
		return replacedBeans;
	}

	/** 
	 * Set the replacedBeans Map
	 * @param replacedBeans Map with replaced beans
	 */
	public void setReplacedBeans(Map<String, Object> replacedBeans) {
		this.replacedBeans = replacedBeans;
	}
		
}
