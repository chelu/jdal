package org.jdal.samples.dao.filter;

import info.joseluismartin.dao.BeanFilter;

import java.util.Date;

import org.jdal.samples.model.Category;

/**
 * Filter for Books
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class BookFilter extends BeanFilter {
	
	private String name = "";
	private String authorName = "";
	private String authorSurname ="";
	private Date before;
	private Date after;
	private String isbn = "";
	private Category category;
	
	public BookFilter() {
		this("bookFilter");
	}
	
	public BookFilter(String filterName) {
		super(filterName);
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * @return the isbn
	 */
	public String getIsbn() {
		return isbn;
	}
	/**
	 * @param isbn the isbn to set
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the authorName
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * @param authorName the authorName to set
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	/**
	 * @return the authorSurname
	 */
	public String getAuthorSurname() {
		return authorSurname;
	}

	/**
	 * @param authorSurname the authorSurname to set
	 */
	public void setAuthorSurname(String authorSurname) {
		this.authorSurname = authorSurname;
	}

	/**
	 * @return the before
	 */
	public Date getBefore() {
		return before;
	}

	/**
	 * @param before the before to set
	 */
	public void setBefore(Date before) {
		this.before = before;
	}

	/**
	 * @return the after
	 */
	public Date getAfter() {
		return after;
	}

	/**
	 * @param after the after to set
	 */
	public void setAfter(Date after) {
		this.after = after;
	}
	
	
}
