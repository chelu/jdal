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
package org.jdal.text;

import java.text.ParseException;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.springframework.format.Formatter;

/**
 * Formatter for periods of time in millis. Parse a 
 * print time periods in format "%d d %d h %d m %d s" for 
 * days, hours, minutes and sconds.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class PeriodFormatter implements Formatter<Number> {

	private static final String DAYS = "d";
	private static final String HOURS = "h";
	private static final String MINUTES = "m";
	private static final String SECONDS = "s";
	
	/**
	 * {@inheritDoc}
	 */
	public String print(Number number, Locale locale) {
		long period = number.longValue();
		long days = TimeUnit.MILLISECONDS.toDays(period);
		long left = period - TimeUnit.DAYS.toMillis(days);
		long hours  = TimeUnit.MILLISECONDS.toHours(left);
		left -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(left);
		left -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(left);
		
		StringBuilder sb = new StringBuilder();
		
		if (days != 0) 
			append(sb, days, DAYS);

		if (hours != 0)
			append(sb, hours, HOURS);
		
		if (minutes != 0)
			append(sb, minutes, MINUTES);
		
		if (seconds != 0)
			append(sb, seconds, SECONDS);
		
		return sb.toString();
	}

	/**
	 * Append period and time unit to a StringBuffer
	 * @param sb string buffer
	 * @param period time period
	 * @param unit time unit
	 */
	private void append(StringBuilder sb, long period, String unit) {
		if (sb.length() > 0)
			sb.append(" ");
		
		sb.append(period);
		sb.append(" ");
		sb.append(unit);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public Number parse(String text, Locale locale) throws ParseException {
		long value = 0;
		Scanner scanner = new Scanner(text);
		
		while(scanner.hasNext()) 
			value += parse(scanner.nextLong(), scanner.next("[dhms]"));
		
		scanner.close();
	
		return value;
		
	}

	/**
	 * convert period and unit to millis
	 * @param value period value
	 * @param unit time unit
	 * @return value in millis.
	 */
	private long parse(long value, String unit) {
		if (DAYS.equalsIgnoreCase(unit))
			return TimeUnit.DAYS.toMillis(value);
		else if (HOURS.equalsIgnoreCase(unit))
			return TimeUnit.HOURS.toMillis(value);
		else if (MINUTES.equalsIgnoreCase(unit))
			return TimeUnit.MINUTES.toMillis(value);
		else if (SECONDS.equalsIgnoreCase(unit))
			return TimeUnit.SECONDS.toMillis(value);
		
		return 0;
	}

}
