package info.joseluismartin.model;

/** 
 * A Country
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public class Country {

	private Long id;
	private String name;
	private Long area;
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
	 * @return the area
	 */
	public Long getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(Long area) {
		this.area = area;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

}
