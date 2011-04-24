package info.joseluismartin.dao.filter;

import info.joseluismartin.dao.BeanFilter;

public class PatternFilter extends BeanFilter {
	
	public static final String PATTERN_FILTER_NAME = "patternFilter";
	private String pattern;
	
	public PatternFilter() {
		this(PATTERN_FILTER_NAME);
	}

	public PatternFilter(String filterName) {
		super(filterName);
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	
}
