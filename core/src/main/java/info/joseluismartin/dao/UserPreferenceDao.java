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
package info.joseluismartin.dao;

import info.joseluismartin.model.User;
import info.joseluismartin.model.UserPreference;

/**
 * Data Access Object for User Preferences 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.2.1
 */
public interface UserPreferenceDao extends Dao<UserPreference, Long> {
	
	
	/**
	 * Find User Preference
	 * @param user User to looking for
	 * @param name the preference name
	 * @return UserPreference or null if none
	 */
	UserPreference findUserPreference(User user, String name);
	
	/**
	 * Find user preference value
	 * @param user user to looking for
	 * @param name the preference name
	 * @return String value or null if none
	 */
	String findUserPreferenceValue(User user, String name);

	/**
	 * @return new UserPreference
	 */
	UserPreference createUserPreference();
}
