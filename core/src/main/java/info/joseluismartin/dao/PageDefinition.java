package info.joseluismartin.dao;

import info.joseluismartin.Constants;

/**
 * Page Definition for use when requesting a Page
 * 
 * @author mjantunez - (mjantunez@matchmind.es)
 */
public class PageDefinition {
	
	/**
	 * sort
	 */
	private String sort = "ID";
	/**
	 * dir
	 */
	private String dir = Constants.ORDER_ASC;
	/**
	 * startIndex
	 */
	private int startIndex = 0;
	/**
	 * results
	 */
	private int results = 20;
	/**
	 * filter
	 */
	private Object  filter;
	
	/**
	 * @param sort the sort to set
	 * @param dir the dir to set
	 * @param startIndex the startIndex to set
	 * @param results the results to set
	 */
	public PageDefinition(String sort, String dir, int startIndex,
			int results) {
		this.sort = sort;
		this.dir = dir;
		this.startIndex = startIndex;
		this.results = results;
	}
	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}
	/**
	 * @param sort the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}
	/**
	 * @return the dir
	 */
	public String getDir() {
		return dir;
	}
	/**
	 * @param dir the dir to set
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}
	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}
	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	/**
	 * @return the results
	 */
	public int getResults() {
		return results;
	}
	/**
	 * @param results the results to set
	 */
	public void setResults(int results) {
		this.results = results;
	}
	/**
	 * @return the filter
	 */
	public Object getFilter() {
		return filter;
	}
	/**
	 * @param filter the filter to set
	 */
	public void setFilter(Object filter) {
		this.filter = filter;
	}
}
