package aop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.jdal.annotation.SerializableProxy;
import org.jdal.aop.SerializableObject;
import org.jdal.aop.config.SerializableAnnotationBeanPostProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ClassUtils;
import org.springframework.util.SerializationUtils;

/**
 * Serializable proxy tests.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SerializableConfig.class)
public class SerializableProxyTest {
	
	@Autowired
	private ConfigurableBean configurableBean;
	@Autowired
	private AutowiredBean autowiredBean;
	@Autowired 
	private ApplicationContext context;
	
	
	@Test
	public void testSerializablePostProcessor() {
		byte[] ser = SerializationUtils.serialize(configurableBean);
		ConfigurableBean cb = (ConfigurableBean) SerializationUtils.deserialize(ser);
		assertEquals(cb.getNoSerializableBean(), configurableBean.getNoSerializableBean());
		ser = SerializationUtils.serialize(autowiredBean);
	    AutowiredBean ab = (AutowiredBean) SerializationUtils.deserialize(ser);
	    assertEquals(autowiredBean.getNoSerializableBean(), ab.getNoSerializableBean());
	}
	
	@Test 
	public void testAutoproxySerializableAdvisor() throws Exception {
		
		NoSerializableAnnotatedBean nsab = context.getBean(NoSerializableAnnotatedBean.class);
		
		assertTrue(ClassUtils.isAssignable(Advised.class, nsab.getClass()));
		Advised advised = (Advised) nsab;
		List<Class<?>> proxiedInterfaces = Arrays.asList(advised.getProxiedInterfaces());
		assertTrue(proxiedInterfaces.contains(SerializableObject.class));
		assertFalse(advised.getTargetSource().getTarget() instanceof Advised);
	
	}

}

@Configuration
@ImportResource("/aop/serializable.xml")
class SerializableConfig {
	
	@Bean
	public ConfigurableBean getConfigurableBean() {
		ConfigurableBean bean = new ConfigurableBean();
		bean.setNoSerializableBean(getNoSerializableBean());
		
		return bean;
	}
	
	@Bean
	public AutowiredBean getAutowiredBean() {
		AutowiredBean autowiredBean = new AutowiredBean();
		autowiredBean.setNoSerializableBean(getNoSerializableBean());
		
		return autowiredBean;
	}
	
	@Bean
	public NoSerializableBean getNoSerializableBean() {
		return new NoSerializableBean();
	}
	
	@Bean
	public SerializableAnnotationBeanPostProcessor serializableAnnotationBeanPostProcessor() {
		return new SerializableAnnotationBeanPostProcessor();
	}
	
}

class NoSerializableBean {

}

@SerializableProxy
class NoSerializableAnnotatedBean {
	
}

class ConfigurableBean implements Serializable {
	
	@SerializableProxy
	private NoSerializableBean noSerializableBean;

	public NoSerializableBean getNoSerializableBean() {
		return noSerializableBean;
	}

	public void setNoSerializableBean(NoSerializableBean noSerializableBean) {
		this.noSerializableBean = noSerializableBean;
	}
}

class AutowiredBean implements Serializable {
	
	@Autowired
	@SerializableProxy
	private NoSerializableBean noSerializableBean;

	public NoSerializableBean getNoSerializableBean() {
		return noSerializableBean;
	}

	public void setNoSerializableBean(NoSerializableBean noSerializableBean) {
		this.noSerializableBean = noSerializableBean;
	}
}