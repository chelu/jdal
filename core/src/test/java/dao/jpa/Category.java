package dao.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jdal.model.Entity;

@javax.persistence.Entity
@Table(name="categories")
public class Category extends Entity {
	
	private static final long serialVersionUID = 1L;
	
	@OneToMany(mappedBy="category", fetch=FetchType.LAZY)
	private Set<Book> books = new HashSet<Book>();
	
	public Category() {
		super();
	}

	public Category(String name) {
		this.name = name;
	}

	public String toString() {
		return getName();
	}

	/**
	 * @return the books
	 */
	public Set<Book> getBooks() {
		return books;
	}

	/**
	 * @param books the books to set
	 */
	public void setBooks(Set<Book> books) {
		this.books = books;
	}

}
