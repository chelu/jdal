/*
 * Copyright 2009-2012 the original author or authors.
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
package info.joseluismartin.dao.jpa;

import java.util.List;

import info.joseluismartin.dao.UserPreferenceDao;
import info.joseluismartin.model.User;
import info.joseluismartin.model.UserPreference;
import info.joseluismartin.util.BeanUtils;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * UserPreferenceDao implementation for JPA.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class JpaUserPreferenceDao extends JpaDao<UserPreference, Long> implements UserPreferenceDao {
	private static final String USER_PROPERTY = "user";
	private static final String NAME_PROPERTY = "name";
	
	public JpaUserPreferenceDao() {
		super();
	}

	public JpaUserPreferenceDao(Class<UserPreference> entityClass) {
		super(entityClass);
	}

	/**
	 * {@inheritDoc}
	 */
	public UserPreference findUserPreference(User user, String name) {
		
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<UserPreference> criteria = cb.createQuery(getEntityClass());
		Root<UserPreference> root = criteria.from(getEntityClass());
		criteria.where(cb.and(cb.equal(root.<User>get(USER_PROPERTY), user), 
				cb.equal(root.<String>get(NAME_PROPERTY), name)));
		
		TypedQuery<UserPreference> query = getEntityManager().createQuery(criteria);
		List<UserPreference> list = query.getResultList();
		
		return list.isEmpty() ? null : list.get(0);
			
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
