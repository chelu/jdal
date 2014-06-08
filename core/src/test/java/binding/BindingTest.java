/*
 * Copyright 2009-2014 Jose Luis Martin.
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
package binding;

import java.util.Currency;

import junit.framework.Assert;

import org.jdal.beans.SimpleTypeConverter;
import org.jdal.ui.bind.AbstractBinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * Data binding tests.
 * 
 * @author Jose Luis Martin
 * @since 2.1
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class BindingTest {

	@Test
	public void testSimpleTypeConverter() {
		SimpleTypeConverter converter = new SimpleTypeConverter();
		String currency = converter.convertIfNecessary(Currency.getInstance("EUR"), String.class);
		Assert.assertEquals("EUR", currency);
	}
	
}

class Model {
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
