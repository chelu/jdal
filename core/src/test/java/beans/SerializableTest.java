/*
 * Copyright 2009-2012 Jose Luis Martin.
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
package beans;

import java.io.IOException;

import junit.framework.Assert;

import org.jdal.util.Serializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test Serializable Proxies.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
 @RunWith(SpringJUnit4ClassRunner.class)
 @ContextConfiguration("serializable.xml")
public class SerializableTest {
	 
	 @Autowired
	 private SerializableBean sbean;

	 /**
	  * Test if @Serializable can apply on transactional proxies
	  */
	 @Test
	 public void testTransactionalProxy() throws IOException {
		 System.out.println(sbean.getTransactionalBean().toString());
		 byte[] bytes = Serializer.serialize(sbean);
		 SerializableBean deserialized = (SerializableBean) Serializer.deSerialize(bytes);
		 Assert.assertNotNull(deserialized.getTransactionalBean());
	 }
	 
}
