/*
 * Copyright 2009-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package serialization;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;

import javax.annotation.Resource;

import org.jdal.aop.SerializableTargetSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.SerializationUtils;

/**
 * 
 * @author Jose Luis Martin
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations="serialization.xml")
public class ProxySerializationTest {

	
	@Resource
	private Bean b1;
	@Resource 
	private Bean b2;
	@Autowired
	private TestUI testUI;
	@Autowired
	private BeanFactory beanFactory;
	
	@Test
	public void testProxySerialization() {
		Bean ser = (Bean) serialize(b1);
		assertNotNull(ser.getAuthorPageableTable());
		Bean other = (Bean) serialize(ser);
		assertNotNull(other.getAuthorPageableTable());
		Bean another = (Bean) serialize(b2);
		assertNotNull(another.getAuthorPageableTable());
	}
	
	private Object serialize(Object obj) {
		byte[] ser = SerializationUtils.serialize(obj);

		return SerializationUtils.deserialize(ser);
	}
	
	@Test
	public void testUISerialization() {
		byte[] ser = SerializationUtils.serialize(testUI);
		TestUI deserialized = (TestUI) SerializationUtils.deserialize(ser);
		Assert.assertEquals(testUI.getClass(), deserialized.getClass());
	}
	
	@Test
	public void testScopedProxy() {
		ProxyFactory pf = new ProxyFactory();
		pf.setTargetSource(new SerializableTargetSource(beanFactory, "b2", true));
		pf.setProxyTargetClass(true);
		pf.addInterface(Serializable.class);
		serialize(pf.getProxy());
	}

}
