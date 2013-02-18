package org.jdal.samples.dao.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.jdal.dao.Filter;
import org.jdal.dao.jpa.JpaCriteriaBuilder;
import org.jdal.samples.model.Author;
import org.jdal.samples.model.Book;
import org.jdal.samples.model.Category;

/**
 * Criteria Builder for Book Filter
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class BookCriteriaBuilder implements  JpaCriteriaBuilder<Book> {

	/**
	 * {@inheritDoc}
	 */
	public CriteriaQuery<Book> build(CriteriaQuery<Book> criteria, CriteriaBuilder cb, Filter filter) {
		BookFilter f = (BookFilter) filter;
		
		Root<Book> root = criteria.from(Book.class);
		Path<Author> author = root.<Author>get("author");
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (StringUtils.isNotEmpty(f.getName()))
			predicates.add(cb.like(root.<String>get("name"), f.getName()));
		
		if (f.getCategory() != null)
			predicates.add(cb.equal(root.<Category>get("category"), f.getCategory()));
		
		if (f.getBefore() != null)
			predicates.add(cb.lessThanOrEqualTo(root.<Date>get("publishedDate"), f.getBefore()));
		
		if (f.getAfter() != null)
			predicates.add(cb.greaterThanOrEqualTo(root.<Date>get("publishedDate"), f.getAfter()));
		
		if (StringUtils.isNotEmpty(f.getAuthorName()))
			 predicates.add(cb.like(author.<String>get("name"), f.getAuthorName()));
		
		if (StringUtils.isNotEmpty(f.getAuthorSurname()))
			predicates.add(cb.like(author.<String>get("surname"), f.getAuthorSurname()));
		
		if (StringUtils.isNotEmpty(f.getIsbn()))
			predicates.add(cb.like(root.<String>get("isbn"), f.getIsbn()));
		
		
		if (predicates.size() > 0)
			criteria.where(cb.and(predicates.toArray(new Predicate[]{})));
		
		return criteria;
	}
}
