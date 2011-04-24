/**
 * 
 */
package info.joseluismartin.dao.hibernate;

import org.hibernate.Criteria;

/**
 * Criteria builder for filters
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface CriteriaBuilder {

	/**
	 * Add Restrictions to Critera from filter
	 * @param criteria criteria to add restrictions
	 * @param filter Filter data
	 * @return criteria.
	 */
	Criteria build(Criteria criteria, Object filter);
}
