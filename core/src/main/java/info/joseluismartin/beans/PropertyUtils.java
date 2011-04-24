package info.joseluismartin.beans;

import org.apache.commons.lang.StringUtils;

public abstract class PropertyUtils {

	public static String PROPERTY_SEPARATOR = ".";
	
	public static String getPropertyName(String propertyPath) {
		if (propertyPath.contains(PROPERTY_SEPARATOR)) 
			return StringUtils.substringAfterLast(propertyPath, PROPERTY_SEPARATOR);
		
		return propertyPath;
	}
	
	public static String getPath(String propertyPath) {
		return StringUtils.substringBeforeLast(propertyPath, PROPERTY_SEPARATOR);
	}

	public static boolean isNested(String propertyPath) {
		return propertyPath.contains(PROPERTY_SEPARATOR);
	}
}
