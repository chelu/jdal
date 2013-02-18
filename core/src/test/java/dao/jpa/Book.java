package dao.jpa;

import java.util.Date;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.jdal.model.Entity;

@NamedQuery(name="booksByAuthorName", query="SELECT b FROM Book b WHERE b.author.name= :authorName")
@javax.persistence.Entity
@Table(name="books")
public class Book extends Entity {
	
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name="authorid")
	private Author author;
	@ManyToOne
	@JoinColumn(name="categoryid")
	private Category category;
	private String isbn = "";
	private Date publishedDate;
	
	public String toString() {
		return name;
	}
	
	/**
	 * @return the author
	 */
	public Author getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(Author author) {
		this.author = author;
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
	 * @return the publishedDate
	 */
	public Date getPublishedDate() {
		return publishedDate;
	}
	/**
	 * @param publishedDate the publishedDate to set
	 */
	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}
}
