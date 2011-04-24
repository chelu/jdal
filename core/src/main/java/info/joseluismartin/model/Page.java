package info.joseluismartin.model;

import java.util.List;

/**
 * Page model for use with Daos
 * 
 * @author Jose Luis Martin (chelu.es@gmail.com)
 */
public class Page {

	/**
	 * List of results
	 */
	private List results;
	
	/**
	 * Result count
	 */
	private int count;

	/**
	 * @return the results
	 */
	public List getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List results) {
		this.results = results;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

}
