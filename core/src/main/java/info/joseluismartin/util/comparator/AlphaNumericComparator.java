/*
 * Copyright 2009-2011 the original author or authors.
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
package info.joseluismartin.util.comparator;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class AlphaNumericComparator implements Comparator<String> {
	
	private static final Log log = LogFactory.getLog(AlphaNumericComparator.class);

	/**
	 * {@inheritDoc}
	 */
	public int compare(String o1, String o2) {
		try {
			Pattern pattern = Pattern.compile("([0-9]+)"); 
			Matcher matcher1 = pattern.matcher(o1);
			Matcher matcher2 = pattern.matcher(o2);
			
			if ((matcher1.find() && matcher2.find()) &&
				o1.startsWith(o2.substring(0, matcher2.start()))) {
				// two strings have numbers, and same prefix.
				// use numeric comparation
				Integer n1 = Integer.valueOf(matcher1.group());
				Integer n2 = Integer.valueOf(matcher2.group());
				return n1.compareTo(n2);
			}
			// else return string comparation
			return o1.compareToIgnoreCase(o2);
		} catch (Exception e) {
			log.error(e);
		}
		return 0; // On Exception object are equals
	}

}
