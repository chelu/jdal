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
package url;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.jdal.text.PeriodFormatter;
import org.junit.Test;


/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class PeriodFormatterTest {

	private static final long TEST_PERIOD = 93784000l;
	private static final String TEST_PERIOD_STRING = "1 d 2 h 3 m 4 s";
	@Test
	public void testPeriodParse() throws Exception {
		PeriodFormatter formatter = new PeriodFormatter();
		long millis = formatter.parse("1 d 2 h 3 m 4 s", Locale.getDefault()).longValue();
		assertEquals(TEST_PERIOD, millis); 
	}
	
	@Test
	public void testPeriodPrint() throws Exception {
		PeriodFormatter formatter = new PeriodFormatter();
		String value = formatter.print(TEST_PERIOD, Locale.getDefault());
		assertEquals(TEST_PERIOD_STRING, value); 
	}
}
