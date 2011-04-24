package info.joseluismartin.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.StringUtils;

/**
 * Base class for CriteriaBuilders, add some utility methods.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class AbstractCriteriaBuilder implements CriteriaBuilder {

	public abstract Criteria build(Criteria criteria, Object filter);
	
	/**
	 * Add a '=' Restriction on property
	 * @param criteria Criteria to add restriction
	 * @param property property path
	 * @param value restriction value
	 */
	protected void eq(Criteria criteria, String property, Object value) {
		if (value != null)
			criteria.add(Restrictions.eq(property, value));
	}
	
	
	/**
	 * Add a '<=' Restriction on property
	 * @param criteria Criteria to add restriction
	 * @param property property path
	 * @param value restriction value
	 */
	protected void le(Criteria criteria, String property, Object value) {
		if (value != null)
			criteria.add(Restrictions.le(property, value));
	}
	
	/**
	 * Add a '>=' Restriction on property
	 * @param criteria Criteria to add restriction
	 * @param property property path
	 * @param value restriction value
	 */
	protected void ge(Criteria criteria, String property, Object value) {
		if (value != null)
			criteria.add(Restrictions.ge(property, value));
	}
	
	/**
	 * Add a ilike Restriction adding wrapping value on '%' and replacing '*'
	 * for '%'
	 * @param criteria Criteria to add restriction
	 * @param property property path
	 * @param value text for the ilike restriction
	 */
	protected void like(Criteria criteria, String property, String value) {
		if (StringUtils.hasText(value)) {
			String toMatch = ((String) value).trim();
			toMatch = toMatch.replace('*', '%');
			toMatch = "%" + toMatch + "%";
			criteria.add(Restrictions.ilike(property, toMatch));
		}
	}

}
