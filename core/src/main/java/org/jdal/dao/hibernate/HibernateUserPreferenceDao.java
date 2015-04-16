/*
 * Copyright 2009-2015 Jose Luis Martin
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
package org.jdal.dao.hibernate;

import org.hibernate.criterion.Restrictions;
import org.jdal.dao.UserPreferenceDao;
import org.jdal.model.User;
import org.jdal.model.UserPreference;
import org.jdal.util.BeanUtils;

/**
 * Hibernate implementation for {@link UserPreferenceDao}
 * 
 * @author Jose Luis Martin
 * @since 1.2
 */
public class HibernateUserPreferenceDao extends HibernateDao<UserPreference, Long> implements UserPreferenceDao  {

	public HibernateUserPreferenceDao() {
		this(UserPreference.class);
	}

	/**
	 * @param persistentClass
	 */
	public HibernateUserPreferenceDao(Class<UserPreference> persistentClass) {
		super(persistentClass);
	}

	private static final String USER_PROPERTY = "user";
	private static final String NAME_PROPERTY = "name";

	/**
	 * {@inheritDoc}
	 */
	public UserPreference findUserPreference(User user, String name) {
		return (UserPreference) getSession().createCriteria(UserPreference.class)
				.add(Restrictions.eq(USER_PROPERTY, user))
				.add(Restrictions.eq(NAME_PROPERTY, name))
				.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	public String findUserPreferenceValue(User user, String name) {
		UserPreference p = findUserPreference(user, name);

		return p != null ? p.getValue() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public UserPreference createUserPreference() {
		return BeanUtils.instantiate(getEntityClass());
	}

}

