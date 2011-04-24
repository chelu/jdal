package info.joseluismartin.table;

/**
 * Validate view models from TableEditor
 * 
 * @author Manuel Jesus Antunez (mjantunez@matchmind.es)
 */
public interface Validator {
	
	/**
	 * @param object to validate
	 * @return ResultValidator
	 */
	ValidationResult validate(Object object);

}
