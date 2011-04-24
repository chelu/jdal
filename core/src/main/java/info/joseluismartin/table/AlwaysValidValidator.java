package info.joseluismartin.table;

/**
 * @author calejo
 *
 */
public class AlwaysValidValidator implements Validator {

	/**
	 * Return a validation result that is always valid by default
	 * @param object object to validate
	 * @return always valid
	 */
	public ValidationResult validate(Object object) {
		ValidationResult res = new ValidationResult();
		res.setValid(true);
		return res;
	}

}
