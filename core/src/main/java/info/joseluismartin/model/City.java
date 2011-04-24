package info.joseluismartin.model;

/**
 * A City
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public class City {
	
	/** id */
	private Long id;
	/** city name */
	private String name;
	/** population */
	private Long population;
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
	 * @return the population
	 */
	public Long getPopulation() {
		return population;
	}
	/**
	 * @param population the population to set
	 */
	public void setPopulation(Long population) {
		this.population = population;
	}
}